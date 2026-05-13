package com.hao.core.net

import com.hao.core.env.ApiException


/**
 * Created by wanghao 2022/7/27
 */

abstract class BaseObserver<T, R>(var interruptException: Boolean = false) {

    abstract fun onCompleted()

    abstract fun onError(exception: ApiException)

    abstract fun onNext(t: R)

    abstract fun apply(t: T): R
}
