package com.hao.container.router

import android.text.TextUtils
import com.alibaba.android.arouter.facade.Postcard
import com.hao.common.utils.AppLifecycleManager
import com.hao.common.utils.ToastUtils
import com.hao.core.router.IPreInterceptor
import com.hao.service.pay.PayService

class PayInterceptor : IPreInterceptor {

    override fun intercept(postcard: Postcard): Boolean {
        if (postcard.path == "/pay/product") {
            var productId = ""
            try {
                if (postcard.extras != null) {//本地路由过来
                    productId = postcard.extras.getString("productId", "")
                }
                if (postcard.uri != null) {//外部路由
                    productId = postcard.uri.getQueryParameter("productId")?:""
                }
            } catch (e : Exception) {
                ToastUtils.showShort("PayInterceptor :$e")
            }

            if (!TextUtils.isEmpty(productId)) {
                AppLifecycleManager.getInstance().presentActivity?.let {
                    PayService.getInstance().startPay(it,productId)
                }
            }
            return true
        }
        return false
    }
}