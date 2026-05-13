package com.hao.container.router

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.TextUtils
import com.hao.common.utils.ConvertUtils
import com.hao.common.utils.LogUtil
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity

/**
 * Created by wanghao 2023/6/21
 */

open class UrlSchemeActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.i("内链跳转  UrlSchemeActivity onCreate")
        parseScheme()
    }


    private fun parseScheme() {
        intent.scheme?.let {
            if (isH5(it)) {
                //跳转h5
                intent.data?.let {
                    val postcard = ARouter.getInstance().build("/view/web")
                    TextUtils.splitQueryParameters(it)?.forEach { params ->
                        if (params.key == "toolbar") {
                            postcard.withInt(params.key, ConvertUtils.toInt(params.value))
                        } else if (params.key == "heightRatio") {
                            postcard.withFloat(params.key, ConvertUtils.toFloat(params.value))
                        } else {
                            postcard.withString(params.key, params.value)
                        }
                    }
                    postcard.withString("url", it.toString())
                    postcard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    postcard.navigation(this, object : NavCallback() {
                        override fun onArrival(postcard: Postcard) {
                            finish()
                        }
                    })
                }
            } else {
                intent.data?.let {
                    val postcard = ARouter.getInstance().build(it)
                    TextUtils.splitQueryParameters(it)?.forEach { params ->
                        postcard.withString(params.key, params.value)
                    }
                    postcard.navigation(this, object : NavCallback() {
                        override fun onArrival(postcard: Postcard) {
                            finish()
                        }
                    })
                }
            }
        } ?: kotlin.run {
            finish()
        }
    }

    /**
     * 转成ARouter格式的路由
     * haiyaa://feedback，haiyaa://gb/group/feedback
     */
    private fun transformLocalPath(uri: Uri): String {
        var url = uri.toString()
        val scheme = "haiyaa://"
        val host = "gb"
        if (url.contains(scheme) && !url.contains(host)) {
            val endIndex = scheme.length
            val path = url.substring(endIndex)
            url = if (path.isNotEmpty()) {
                "${scheme}${host}/${path}"
            } else {
                ""
            }
        }
        return url
    }

    private fun isH5(scheme: String): Boolean {
        if (scheme == "http" || scheme == "https") {
            return true
        }
        return false
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}