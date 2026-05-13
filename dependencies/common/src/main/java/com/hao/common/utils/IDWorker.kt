package com.hao.common.utils

import java.util.concurrent.atomic.AtomicInteger
/**
 * Created by wanghao 2022/7/27
 */
class IDWorker {

    private var id: AtomicInteger = AtomicInteger(1)

    companion object {
        private var worker: IDWorker? = null
            get() {
                if (field == null) {
                    field = IDWorker()
                }
                return field
            }

        @Synchronized
        fun get(): IDWorker {
            return worker!!
        }
    }

    fun getId(): Int {
        return id.getAndIncrement()
    }
}