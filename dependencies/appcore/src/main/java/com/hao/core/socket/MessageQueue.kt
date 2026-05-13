package com.hao.core.socket

import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.hao.common.utils.ThreadUtil
import com.hao.core.env.ApiException
import java.util.concurrent.*

/**
 * Created by wanghao 2020/12/23 14:59
 */
class MessageQueue {

    private val messageQueue = ConcurrentHashMap<Int, SocketObserver<*>>()

    private var handlerThread: HandlerThread? = null
    private var threadHandler: Handler? = null

    constructor() {
        handlerThread = HandlerThread("socket message")
        handlerThread?.start()
        threadHandler = Handler(handlerThread!!.looper) {
            remove(it.what)?.let {
                ThreadUtil.runOnMainThread {
                    doError(it)
                }
            }
            true
        }

    }

    fun put(messageId: Int, observer: SocketObserver<*>) {
        if (messageId > Int.MAX_VALUE) {
            Log.i("MessageQueue", "不认识的消息：${messageId}")
            return
        } else if (messageId <= 0) {
            Log.i("MessageQueue", "服务器发送的push消息：${messageId}")
            return
        }
        messageQueue[messageId] = observer
        threadHandler?.sendEmptyMessageDelayed(messageId, 7000)
    }

    fun remove(messageId: Int): SocketObserver<*>? {
        if (threadHandler?.hasMessages(messageId) == true) {
            threadHandler?.removeMessages(messageId)
        }
        return messageQueue.remove(messageId)
    }

    private fun doError(observer: SocketObserver<*>) {
        observer.onError(
            //if (BuildConfig.DEBUG) {
            if (true) {
                ApiException(ApiException.CODE_REQUEST_TIME_OUT, "TCP request timeout $observer")
            } else {
                ApiException(ApiException.CODE_REQUEST_TIME_OUT, "TCP request timeout")
            }

        )
    }
}