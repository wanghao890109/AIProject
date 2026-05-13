package com.hao.container.init.module

import android.app.Application
import com.hao.service.account.AccountListener
import com.hao.service.account.AccountService
import com.hao.service.account.IAccountService
import com.hao.service.account.LoginType
import com.hao.service.source.ResourceService

/**
 * Created by wanghao 2022/8/5
 */
class ResourceInit : com.hao.core.moduleinit.ModuleInit() {
    override fun tag(): String {
        return "ResourceInit"
    }

    override fun init(application: Application?) {
        AccountService.getInstance().register(object : AccountListener {
            override fun onLogin(sender: IAccountService?, type: LoginType?) {
                ResourceService.getInstance().loadResource()
            }

            override fun onLogout(sender: IAccountService?) {
            }

            override fun onUpdated(sender: IAccountService?) {
                ResourceService.getInstance().loadResource()
            }
        })
        ResourceService.getInstance().loadResource()
    }

    override fun asyncInit(application: Application?) {

    }
}