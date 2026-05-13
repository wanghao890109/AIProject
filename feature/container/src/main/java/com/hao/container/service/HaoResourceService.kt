package com.hao.container.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.hao.core.cache.CommonSPService
import com.hao.service.source.IResourceService

/**
 * Created by wanghao 2022/8/5
 * 服务器的资源配置
 */
@Route(path = "/resource/service")
class HaoResourceService : IResourceService {

    private val keySystemConfig = "system_config"
    private val keySystemMenuConfig = "system_menu_config"
    private val keySystemLocationsConfig = "system_locations_config"
    private val spService = CommonSPService("resource_config")

    override fun init(context: Context?) {

    }

    override fun loadResource() {
        loadSystemConfig()
        loadSystemMenuConfig()
        loadSystemLocations()
    }

    override fun <T : Any?> getResource(clz: Class<out T>?): T? {
//        if (clz == ResSystemConfig::class.java) {
//            return getResSystemConfig() as T
//        }
//        if (clz == ResMenuConfig::class.java) {
//            return getResMenuConfig() as T
//        }
//        if (clz == ResSystemLocations::class.java) {
//            return getResSystemLocations() as T
//        }
        return null
    }


    private fun loadSystemConfig() {
//        val configBuilder = ReqSystemConfig.Builder()
//        val configVersions = arrayListOf(
//            ConfigVersion(ConfigType.ConfigType_URL, 0L),
//            ConfigVersion(ConfigType.ConfigType_PartyTags, 0L),
//            ConfigVersion(ConfigType.ConfigType_UserTags, 0L),
//            ConfigVersion(ConfigType.ConfigType_RoomFace, 0L)
//        )
//        configBuilder.Versios.addAll(configVersions)
//        MainApi.getService().systemConfig(configBuilder.build()).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                spService.put(keySystemConfig, it)
//
//                val appHttpUrls = it.Urls.transform()
//                ConfigService.getInstance().putConfigCache(appHttpUrls)
//
//                LogUtil.i("HalaResourceService it:$it")
//            }, {
//                LogUtil.e("HalaResourceService e:$it")
//            })
    }

//    private fun getResSystemConfig(): ResSystemConfig {
//        val systemConfig: ResSystemConfig? =
//            spService.get(keySystemConfig, ResSystemConfig::class.java)
//        if (systemConfig == null) {
//            loadSystemConfig()
//            return ResSystemConfig.Builder().build()
//        }
//        return systemConfig
//    }

    private fun loadSystemMenuConfig() {
//        MainApi.getService().menuConfig(ReqMenuConfig.Builder().build())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                spService.put(keySystemMenuConfig, it)
//                LogUtil.i("HalaResourceService it:$it")
//            }, {
//                LogUtil.e("HalaResourceService e:$it")
//            })
    }

//    private fun getResMenuConfig(): ResMenuConfig {
//        val systemConfig: ResMenuConfig? =
//            spService.get(keySystemMenuConfig, ResMenuConfig::class.java)
//        if (systemConfig == null) {
//            loadSystemMenuConfig()
//            return ResMenuConfig.Builder().build()
//        }
//        return systemConfig
//    }

    private fun loadSystemLocations() {
//        MainApi.getService().systemLocations(ReqSystemLocations.Builder().build())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                spService.put(keySystemLocationsConfig, it)
//                LogUtil.i("HalaResourceService it:$it")
//            }, {
//                LogUtil.e("HalaResourceService e:$it")
//            })
    }

//    private fun getResSystemLocations(): ResSystemLocations {
//        val systemConfig: ResSystemLocations? =
//            spService.get(keySystemLocationsConfig, ResSystemLocations::class.java)
//        if (systemConfig == null) {
//            loadSystemLocations()
//            return ResSystemLocations.Builder().build()
//        }
//        return systemConfig
//    }


}