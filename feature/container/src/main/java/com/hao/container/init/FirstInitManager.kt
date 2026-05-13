package com.hao.container.init

import android.app.Application
import com.hao.container.init.module.ARouterInit
import com.hao.container.init.module.ApiServiceInit
import com.hao.container.init.module.FirstInit
import com.hao.core.moduleinit.ModuleInitManager

/**
 * Created by wanghao 2022/7/27
 */
class FirstInitManager private constructor() {

    companion object {
        private var manager: FirstInitManager? = null
            get() {
                if (field == null) {
                    field = FirstInitManager()
                }
                return field
            }

        @Synchronized
        fun get(): FirstInitManager {
            return manager!!
        }
    }

    private val moduleInitManager = ModuleInitManager()

    init {
        moduleInitManager.registerModuleInit(ARouterInit())
        moduleInitManager.registerModuleInit(FirstInit())
        moduleInitManager.registerModuleInit(ApiServiceInit())
    }

    fun appInit(application: Application) {
        moduleInitManager.dispatchAppInit(application)

    }
}