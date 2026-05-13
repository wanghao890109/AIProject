package com.hao.container.app

import android.app.Application
import android.content.res.Configuration
import com.hao.container.init.FirstInitManager
import com.hao.common.utils.LogUtil

class HaoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirstInitManager.get().appInit(this)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LogUtil.e("onConfigurationChanged")

    }
}