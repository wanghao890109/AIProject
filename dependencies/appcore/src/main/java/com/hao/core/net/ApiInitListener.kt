package com.hao.core.net

import com.hao.proto.DeviceInfo


/**
 * Created by wanghao 2022/7/27
 */
interface ApiInitListener {
    fun onGetUserId(): Long
    fun onGetSession(): String
    fun onGetDevice(): DeviceInfo
    fun onServerTime(unix: Long)
}