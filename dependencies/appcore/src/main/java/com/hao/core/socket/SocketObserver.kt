package com.hao.core.socket

import com.hao.core.env.ApiException
import com.squareup.wire.WireEnum


/**
 * Created by wanghao 2020/11/27 22:09
 *
 * interruptException true  交给底层统一处理，拦截业务层.  false , 底层处理后，业务层仍想处理
 */
abstract class SocketObserver<T>(open val cmd: WireEnum? = null, open var interruptException: Boolean = true) {
    constructor(cmd: WireEnum) : this(cmd, true)

    var what = -1
    abstract fun onResult(data: T)

    abstract fun onError(e: ApiException)

}