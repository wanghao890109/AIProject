package com.hao.core.net

import com.hao.core.env.ApiException


/**
 * Created by wanghao 2022/7/27
 */

abstract class ApiObserver<T>(interruptException: Boolean = false) : BaseObserver<T, T>(interruptException) {

    override fun onCompleted() {

    }

    override fun onError(exception: ApiException) {

    }

    override fun apply(t: T): T {
        return t
    }
}
