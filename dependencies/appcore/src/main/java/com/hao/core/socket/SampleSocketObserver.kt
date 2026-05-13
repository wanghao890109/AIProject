package com.hao.core.socket

import com.hao.core.env.ApiException
import com.squareup.wire.WireEnum

open class SampleSocketObserver<T>(override val cmd: WireEnum? = null, override var interruptException: Boolean = true) : SocketObserver<T>(cmd, interruptException){

    constructor(cmd: WireEnum) : this(cmd, true)

    override fun onResult(data: T) {

    }

    override fun onError(e: ApiException) {

    }
}