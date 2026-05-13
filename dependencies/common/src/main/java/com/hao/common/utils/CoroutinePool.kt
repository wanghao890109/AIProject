package com.hao.common.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object CoroutinePool {
    private const val THREAD_POOL_SIZE = 1
    private val threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE, object : ThreadFactory {
        private val threadCount = AtomicInteger(0)
        override fun newThread(r: Runnable?): Thread =
            Thread(r, "hao-${threadCount.getAndIncrement()}")
    })

    val scope: CoroutineScope
        get() = CoroutineScope(threadPool.asCoroutineDispatcher())

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    fun <T> launch(thread: () -> T, uiThread: (T) -> Unit) {
        CoroutinePool.scope.launch {
            val it = thread.invoke()
            runOnUiThread {
                uiThread.invoke(it)
            }
        }
    }

    fun postToMain(invoke: () -> Unit) {
        uiHandler.post(invoke)
    }

    fun runOnUiThread(invoke: () -> Unit) {
        if (isMainThread()) {
            invoke.invoke()
        } else {
            postToMain(invoke)
        }
    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }
}