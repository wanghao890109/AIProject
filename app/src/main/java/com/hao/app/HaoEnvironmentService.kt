package com.hao.app

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.common.utils.UuidUtils
import com.hao.core.env.AppConfig
import com.hao.core.env.AppHttpUrls
import com.hao.service.config.ConfigService
import com.hao.service.env.EnvironmentService
import com.hao.service.env.IEnvironmentService

/**
 * Created by wanghao 2022/8/2
 */

@Route(path = "/environment/service")
class HaoEnvironmentService : IEnvironmentService {

    private var context: Context? = null

    private var channel: String? = null

    override fun init(context: Context?) {
        this.context = context
    }

    override fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun isTestEnv(): Boolean {
        val appConfig = ConfigService.getInstance().getConfigCache(AppConfig::class.java)
        val appHttpUrls = ConfigService.getInstance().getConfigCache(AppHttpUrls::class.java)
        return appConfig.httpBaseUrl != appHttpUrls.apiReleaseUrl
    }

    override fun getContext(): Context {
        return context!!
    }

    override fun getAppName(): String {
        return context!!.getString(R.string.app_name)
    }

    override fun getPackageName(): String {
        return BuildConfig.APPLICATION_ID;
    }

    override fun getAppIcon(): Int {
        return R.mipmap.ic_launcher
    }

    override fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    override fun getDeviceId(): String {
        return UuidUtils.getUuid(EnvironmentService.getInstance().context) ?: ""
    }

    override fun getChannelId(): String {
        if (channel == null) {
            //channel = ChannelReaderUtil.getChannel(context)
            if (isGoogle) {
                channel = "google"
            }
        }
        return channel ?: "测试"
    }

    override fun isGoogle(): Boolean {
        return false
    }

}