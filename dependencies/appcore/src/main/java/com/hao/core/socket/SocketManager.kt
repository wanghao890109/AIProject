package com.hao.core.socket

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.android.socket.client.sdk.client.ConnectionInfo
import com.android.socket.client.sdk.client.action.SocketActionAdapter
import com.hao.common.utils.LogUtil
import com.hao.core.base.SampleActivityLifecycleCallbacks
import com.hao.core.env.ApiException
import com.hao.service.env.EnvironmentService
import com.hao.proto.*
import okio.ByteString


/**
 * Created by wanghao 2020/11/27 14:42
 */
class SocketManager {

    init {
        val application = EnvironmentService.getInstance().context as Application
        application.registerActivityLifecycleCallbacks(object :
            SampleActivityLifecycleCallbacks() {
            override fun onActivityResumed(activity: Activity) {
                if (!isDestroy) {
                    connect()
                }
            }
        })

        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    LogUtil.i("SocketManager , onAvailable")
                    connect()
                }
            })
        } else {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            application.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                        try {
                            val activeNetwork = connectivityManager.activeNetworkInfo
                            if (activeNetwork != null) {
                                if (activeNetwork.isConnected) {
                                    connect()
                                } else {
                                    LogUtil.i("SocketManager , 当前没有可用网络")
                                }
                            }
                        } catch (e: java.lang.Exception) {
                            LogUtil.e("SocketManager , 网络状态获取错误", e)
                        }
                    }
                }
            }, intentFilter)
        }
    }

    companion object {
        private var manager: SocketManager? = null
            get() {
                if (field == null) {
                    field = SocketManager()
                }
                return field
            }

        @Synchronized
        fun get(): SocketManager {
            return manager!!
        }
    }

    val connectStateChange = MutableLiveData<Boolean>()
    private var pushReceiver: PushReceiver? = null

    private var socket: AppSocket? = null
    private var userId: Long = 0L
    private var gateLogin = false
    private var isDestroy = true

    fun connect(userId: Long, gateAddress: String) {
        if (userId != 0L && this.userId == userId) {
            return
        }
        if (gateAddress.isNullOrEmpty()) {
            return
        }
        isDestroy = false
        this.userId = userId
        socket?.disConnect()
        socket = AppSocket(userId, gateAddress)
        socket?.init()
        socket?.manager?.registerReceiver(object : SocketActionAdapter() {
            override fun onSocketDisconnection(
                info: ConnectionInfo?,
                action: String?,
                e: Exception?
            ) {
                disConnect()
            }

            override fun onSocketConnectionFailed(
                info: ConnectionInfo?,
                action: String?,
                e: Exception?
            ) {
                disConnect()
            }

            override fun onSocketConnectionSuccess(info: ConnectionInfo?, action: String?) {
                tcpLogin()
            }
        })
        socket?.setPushReceiver { cmd, data ->
            when (cmd) {
                GateCmd.TCP_HEART_BEAT_CMD.value -> {//心跳推送
                    manager?.socket?.manager?.pulseManager?.feed()
                    //容错处理，防止网络状态异常，当有心跳当时候校正一下
                    tryCheckConnectState()
                }
                else -> {
                    pushReceiver?.onPush(cmd, data)
                }
            }
        }
    }

    fun isConnect(): Boolean {
        return (socket?.isConnect ?: false) && gateLogin
    }

    fun destroy() {
        isDestroy = true
        disConnect()
    }

    private fun disConnect() {
        userId = 0L
        gateLogin = false
        socket?.disConnect()
        connectStateChange.postValue(false)
    }

    private fun tcpLogin() {
        if (gateLogin) {
            return
        }

        val checkAccount = CheckAccount("$userId", 0, "", 0)
        socket?.send(
            DeviceTcpLogin.Builder().accCheck(checkAccount).build().encode(),
            object : SocketObserver<UtilRet>(UserCenterCmd.TCP_TOKEN_LOGIN) {
                override fun onResult(any: UtilRet) {
                    gateLogin = true
                    //开始心跳
                    manager?.socket?.manager?.pulseManager?.setPulseSendable(
                        SocketHeart(userId)
                    )?.pulse()
                    connectStateChange.value = true
                }

                override fun onError(e: ApiException) {
                    gateLogin = false
                    connectStateChange.value = false
                }
            }
        )
    }

    //容错处理，防止网络状态异常，当有心跳当时候校正一下
    private fun tryCheckConnectState() {
        if (connectStateChange.value == false) {
            connectStateChange.value = true
        }
    }

    fun connect() {
        if (!isDestroy && !isConnect()) {
            socket?.connect()
        }
    }

    fun <T> send(bytes: ByteArray, observer: SocketObserver<T>) {
        if (isDestroy) {
            LogUtil.i("Socket-  send isDestroy")
            return
        }
        if (!isConnect()) {
            LogUtil.i("Socket-  send !isConnect")
            socket?.connect()
        }
        socket?.send(bytes, observer)
    }

    fun setPushReceiver(pushReceiver: PushReceiver) {
        this.pushReceiver = pushReceiver
    }

    interface PushReceiver {
        fun onPush(cmd: Int, data: ByteString)
    }
}