package com.hao.container.init.module

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.hao.container.BuildConfig
import com.hao.core.moduleinit.ModuleInit

class ARouterInit : ModuleInit() {
    override fun tag(): String {
        return "ARouterInit"
    }

    override fun init(application: Application?) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
//            ARouter.openDebug()
        }
        ARouter.init(application)
    }

    override fun asyncInit(application: Application?) {

    }
}