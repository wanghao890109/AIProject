package com.hao.container.init.module

import android.app.Application
import com.hao.statemanager.loader.StateRepository
import com.hao.common.utils.AppLifecycleManager
import com.hao.common.utils.NetWorkUtil
import com.hao.permission.XMPermissions
import com.hao.ui.state.CommonEmptyState
import com.hao.ui.state.CommonLoadingState
import com.hao.ui.state.CommonNetErrorState
import com.hao.ui.widget.refresh.MyRefreshHead
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class FirstInit : com.hao.core.moduleinit.ModuleInit() {
    override fun tag(): String {
        return "FirstInit"
    }

    override fun init(application: Application?) {

        NetWorkUtil.init(application)

        AppLifecycleManager.getInstance().init(application)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MyRefreshHead(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }

        XMPermissions.init(application)

        initState()
    }

    override fun asyncInit(application: Application?) {

    }

    private fun initState() {
        StateRepository.registerState(CommonLoadingState.STATE, CommonLoadingState::class.java)
        StateRepository.registerState(CommonNetErrorState.STATE, CommonNetErrorState::class.java)
        StateRepository.registerState(CommonEmptyState.STATE, CommonEmptyState::class.java)
    }
}