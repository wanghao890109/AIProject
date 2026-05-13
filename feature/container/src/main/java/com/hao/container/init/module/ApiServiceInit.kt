package com.hao.container.init.module

import android.app.Application
import android.os.Build
import com.hao.common.utils.NetWorkUtil
import com.hao.core.env.AppConfig
import com.hao.core.env.AppHttpUrls
import com.hao.core.net.ApiInitListener
import com.hao.core.net.ApiService
import com.hao.core.net.PbDeviceInfo
import com.hao.proto.DeviceInfo
import com.hao.service.account.AccountService
import com.hao.service.config.ConfigService
import com.hao.service.env.EnvironmentService

/**
 * Created by wanghao 2022/10/14
 * 网络初始化
 */
class ApiServiceInit : com.hao.core.moduleinit.ModuleInit() {
    override fun tag(): String {
        return "ApiServiceInit"
    }

    override fun init(application: Application?) {
        ApiService.setApiInitListener(object : ApiInitListener {
            override fun onGetUserId(): Long {
                return AccountService.getInstance().userId
            }

            override fun onGetSession(): String {
                return AccountService.getInstance().session
            }

            override fun onGetDevice(): DeviceInfo {
                return PbDeviceInfo.getDeviceInfo()
            }

            override fun onServerTime(unix: Long) {
                ConfigService.getInstance().serverTime = unix
            }
        })

        initHttpBaseUrl()
    }

    /**
     * 初始化baseUrl
     */
    private fun initHttpBaseUrl() {
        val appHttpUrls = ConfigService.getInstance().getConfigCache(AppHttpUrls::class.java)
        val appConfig = ConfigService.getInstance().getConfigCache(AppConfig::class.java)
        if (EnvironmentService.getInstance().isDebug) {
            //开发环境下，默认测试地址（如需使用正式地址，在登陆页手动设置）
            if (appConfig.httpBaseUrl.isNullOrEmpty()) {
                appConfig.httpBaseUrl = appHttpUrls.apiDevUrl
                ConfigService.getInstance().putConfigCache(appConfig)
            }
        } else {
            //正式发版包，使用正式环境
            if (appConfig.httpBaseUrl != appHttpUrls.apiReleaseUrl) {
                appConfig.httpBaseUrl = appHttpUrls.apiReleaseUrl
                ConfigService.getInstance().putConfigCache(appConfig)
            }
        }
    }

    override fun asyncInit(application: Application?) {

    }

}