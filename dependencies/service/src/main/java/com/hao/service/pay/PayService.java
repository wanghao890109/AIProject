package com.hao.service.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.HashSet;

/**
 * Created by wanghao 2022/8/1
 */
public class PayService implements IPayService {

    private IPayService iPayService;

    private static class Inner {
        private static PayService sInstance = new PayService();
    }

    public static PayService getInstance() {
        return Inner.sInstance;
    }

    private PayService() {
        iPayService = ARouter.getInstance().navigation(IPayService.class);
    }

    @Override
    public void init(Context context) {

    }

    private static final int MSG_PAY_SUCCEED = 1;
    private static final int MSG_PAY_ERROR = 2;
    private static final int MSG_LISTENER_ADD = 3;
    private static final int MSG_LISTENER_REMOVE = 4;

    private final Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PAY_SUCCEED:
                    for (PayListener payListener : mListeners) {
                        payListener.onSucceed((String)msg.obj, 0, "");
                    }
                    break;
                case MSG_PAY_ERROR:
                    for (PayListener payListener : mListeners) {
                        payListener.onSucceed((String)msg.obj, msg.arg1, "");
                    }
                    break;
                case MSG_LISTENER_ADD:
                    mListeners.add((PayListener) msg.obj);
                    break;
                case MSG_LISTENER_REMOVE:
                    mListeners.remove(msg.obj);
                    break;
            }
        }
    };
    private HashSet<PayListener> mListeners = new HashSet<>();

    @Override
    public void startPay(Activity activity, String productId) {
        iPayService.startPay(activity, productId);
    }

    /**
     * 注册监听
     */
    public void register(PayListener listener) {
        Message.obtain(mUIHandler, MSG_LISTENER_ADD, listener).sendToTarget();
    }

    /**
     * 取消监听
     */
    public void unregister(PayListener listener) {
        Message.obtain(mUIHandler, MSG_LISTENER_REMOVE, listener).sendToTarget();
    }

}
