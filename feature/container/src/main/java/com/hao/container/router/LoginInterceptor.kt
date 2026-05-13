package com.hao.container.router

import android.content.Intent
import android.net.Uri
import com.alibaba.android.arouter.facade.Postcard
import com.hao.common.utils.AppLifecycleManager
import com.hao.core.router.IPreInterceptor
import com.hao.service.account.AccountService

/**
 * http、https 用浏览器打开
 */
class LoginInterceptor :IPreInterceptor {
    override fun intercept(postcard: Postcard): Boolean {
        val url = postcard?.uri?.toString()
//        if (url?.startsWith("/login", true) == false) {
//            if (!AccountService.getInstance().isLogin) {
//
//                return true
//            }
//        }
        return false
    }
}