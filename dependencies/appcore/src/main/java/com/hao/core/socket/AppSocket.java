package com.hao.core.socket;

import com.android.socket.client.core.pojo.OriginalData;
import com.android.socket.client.core.utils.SLog;
import com.android.socket.client.sdk.WhSocket;
import com.android.socket.client.sdk.client.ConnectionInfo;
import com.android.socket.client.sdk.client.WhSocketOptions;
import com.android.socket.client.sdk.client.action.SocketActionAdapter;
import com.android.socket.client.sdk.client.connection.IConnectionManager;
import com.hao.common.utils.GenericsUtils;
import com.hao.common.utils.LogUtil;
import com.hao.common.utils.NetWorkUtil;
import com.hao.common.utils.ThreadUtil;
import com.hao.core.env.ApiException;
import com.hao.core.thrower.ExceptionThrower;
import com.hao.proto.GateCmd;
import com.hao.proto.PbHead;
import com.hao.proto.PushCmd;
import com.hao.proto.RoomCmd;
import com.hao.proto.UtilRet;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.WireEnum;

import java.io.IOException;
import java.nio.ByteOrder;

import okio.ByteString;

public class AppSocket {

    private long userId;
    private String connectUrl;
    private ConnectionInfo connectionInfo;
    private IConnectionManager manager;
    private MessageQueue messageQueue = new MessageQueue();
    private ExceptionThrower thrower = new ExceptionThrower();
    private PushReceiver pushReceiver = null;

    public AppSocket(long userId, String connectUrl) {
        this.userId = userId;
        this.connectUrl = connectUrl;
    }

    public void init() {
        disConnect();
        String delimiter = ":";
        if (!connectUrl.contains(delimiter)) {
            return;
        }
        SLog.setIsDebug(true);
        String[] splits = connectUrl.split(delimiter);
        String ip = splits[0];
        int port = Integer.valueOf(splits[1]);
        LogUtil.i("Socket-  ip:" + ip + " ,port: " + port);
        connectionInfo = new ConnectionInfo(ip, port);
        WhSocketOptions options = new WhSocketOptions.Builder()
                .setReaderProtocol(new SocketProtocol())
                .setReadByteOrder(ByteOrder.LITTLE_ENDIAN)
                .setConnectionHolden(false)
                //.setPulseFrequency(10*1000)
                .setReconnectionManager(new SocketReconnect())
                .build();

        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        manager = WhSocket.open(connectionInfo).option(options);
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        manager.registerReceiver(adapter);

        //调用通道进行连接
        connect();
    }

    public IConnectionManager getManager() {
        return manager;
    }

    public boolean isConnect() {
        if (connectionInfo == null || getManager() == null) {
            return false;
        }
        return getManager().isConnect();
    }

    public void connect() {
        if (!isConnect()) {
            if (getManager() != null) {
                getManager().connect();
            }
        }
    }

    public void disConnect() {
        if (getManager() != null) {
            getManager().disconnect();
        }
    }

    public <T> int send(byte[] bytes, SocketObserver<T> observer) {

        if (!NetWorkUtil.isNetConnected()) {
            observer.onError(new ApiException(ApiException.CODE_NET_NOT_CONNECTED, "Please check whether the network is abnormal"));
            return 0;
        }
        if (!isConnect()) {
            observer.onError(new ApiException(ApiException.CODE_TCP_NOT_CONNECTED, "Connection abnormality, trying to reconnect"));
            return 0;
        }
        WireEnum cmd = observer.getCmd();
        if (cmd == null) {
            observer.onError(new ApiException(ApiException.CODE_NO_RESISTER_PROTO, "cmd 未填写"));
            return 0;
        }
        SocketMessage message = SocketUtil.INSTANCE.newSendMessage(observer.getCmd().getValue(), userId, bytes);
        getManager().send(message);

        messageQueue.put(message.getId(), observer);
        return message.getId();
    }

    private SocketActionAdapter adapter = new SocketActionAdapter() {
        @Override
        public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
            ProtoAdapter<PbHead> pbAdapter = ProtoAdapter.get(PbHead.class);
            try {
                PbHead message = pbAdapter.decode(data.getBodyBytes());
                int cmd = message.cmd;
                int messageId = message.mid;
                int code = message.pam;
                ByteString pbData = message.pbBody;

//                if (BuildConfig.DEBUG) {
//                    if (cmd != GateCmd.TCP_HEART_BEAT_CMD.getValue()) {
//                        LogUtil.i("Socket-receive: " + message);
//                        RoomCmd roomCmd = RoomCmd.fromValue(cmd);
//                        if (roomCmd != null) {
//                            LogUtil.i("Socket-cmd: " + roomCmd);
//                        } else {
//                            PushCmd pushCmd = PushCmd.fromValue(cmd);
//                            if (pushCmd != null) {
//                                LogUtil.i("Socket-cmd: " + pushCmd);
//                            }
//                        }
//                    }
//                }

                if (messageId == 0) { //push
                    pushReceiver.onPush(cmd, pbData);
                } else {
                    SocketObserver observer = messageQueue.remove(messageId);
                    if (observer == null) {
                        thrower.onThrow(new ApiException(ApiException.CODE_NO_RESISTER_PROTO, "注册observer失败"));
                        return;
                    }
                    try {
                        Class type = GenericsUtils.INSTANCE.getSuperClassGenericType(observer.getClass());
                        if (type == Object.class) {
                            ThreadUtil.runOnMainThread(() -> observer.onError(new ApiException(ApiException.CODE_PROTO_ENCODE_DECODE_ERROR, "pb解析失败(data is Any), $observer")));
                        } else {
                            if (code == 0) {
                                ProtoAdapter dataAdapter = ProtoAdapter.get(type);
                                Object object = dataAdapter.decode(pbData);
                                ThreadUtil.runOnMainThread(() -> observer.onResult(object));
                            } else {
                                ProtoAdapter<UtilRet> dataAdapter = ProtoAdapter.get(UtilRet.class);
                                UtilRet error = dataAdapter.decode(message.pbBody);
                                LogUtil.i("Socket-  code : " + error.retCode + "  msg:" + error.retTxt + "  cmd:" + observer.getCmd());
                                ThreadUtil.runOnMainThread(() -> {
                                    ApiException apiException = new ApiException(error.retCode, error.retTxt);
                                    boolean onThrow = thrower.onThrow(apiException);
                                    if (onThrow) {
                                        if (observer.getInterruptException()) {
                                            //统一处理后，业务层不再处理
                                        } else {
                                            //如果业务层，仍想处理，则抛给业务层
                                            observer.onError(apiException);
                                        }
                                    } else {
                                        observer.onError(apiException);
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        ThreadUtil.runOnMainThread(() -> {
                            observer.onError(new ApiException(ApiException.CODE_PROTO_ENCODE_DECODE_ERROR, "pb解析失败 $observer"));
                        });
                    }
                }
            } catch (IOException e) {
                thrower.onThrow(new ApiException(ApiException.CODE_UNKNOWN, "PbHead解析失败"));
            }
        }
    };

    public void setPushReceiver(PushReceiver pushReceiver) {
        this.pushReceiver = pushReceiver;
    }

    public interface PushReceiver {
        void onPush(int cmd, ByteString data);
    }
}
