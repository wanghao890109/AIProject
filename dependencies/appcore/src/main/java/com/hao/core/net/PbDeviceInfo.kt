package com.hao.core.net

import android.os.Build
import com.hao.common.utils.NetWorkUtil
import com.hao.proto.DeviceInfo
import com.hao.service.env.EnvironmentService


object PbDeviceInfo {

    fun getDeviceInfo(): DeviceInfo {
        val environment = EnvironmentService.getInstance()
        val baseBuilder = DeviceInfo.Builder()
        baseBuilder.osVersion = Build.VERSION.SDK_INT.toString()
        baseBuilder.devName = Build.MODEL.toString().lowercase()
        baseBuilder.deviceId = environment.deviceId
        baseBuilder.appVersion = environment.version
        baseBuilder.verCode = environment.versionCode.toString()
        baseBuilder.channelId = environment.channelId
        baseBuilder.net = NetWorkUtil.getConnectionType(environment.context).toString()
        baseBuilder.platform = 1
        baseBuilder.latitude = 0.0
        baseBuilder.latitude = 0.0
        return baseBuilder.build()
    }
}