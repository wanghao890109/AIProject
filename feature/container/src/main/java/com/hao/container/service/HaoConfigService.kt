package com.hao.container.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.core.cache.SPCacheService
import com.hao.core.env.AppConfig
import com.hao.core.env.AppHttpUrls
import com.hao.service.config.IConfigService

/**
 * Created by wanghao 2022/8/5
 * 全局配置 or 开关
 */
@Route(path = "/config/service")
class HaoConfigService : IConfigService {


    override fun init(context: Context?) {

    }

    override fun setServerTime(unix: Long?) {

    }

    override fun getServerTime(): Long {
        return 0
    }

    override fun <T : Any?> putConfigCache(t: T) {
        if (t is AppHttpUrls) {
            SPCacheService.getInstance().put(AppHttpUrls::class.simpleName, t)
        } else if (t is AppConfig) {
            SPCacheService.getInstance().put(AppConfig::class.simpleName, t)
        }
    }

    override fun <T : Any?> getConfigCache(clz: Class<out T>?): T? {
        if (clz == AppHttpUrls::class.java) {
            return SPCacheService.getInstance().get(AppHttpUrls::class.simpleName, AppHttpUrls()) as T
        } else if (clz == AppConfig::class.java) {
            return SPCacheService.getInstance().get(AppConfig::class.simpleName, AppConfig()) as T
        }
        return null
    }


}