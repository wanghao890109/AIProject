package com.hao.container.router

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.webkit.URLUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService
import com.hao.common.utils.LogUtil

/**
 * Created by wanghao 2023/6/21
 */
@Route(path = "/scheme/pathreplace")
class PathReplaceServiceImpl : PathReplaceService {

    override fun init(context: Context) {

    }

    override fun forString(path: String): String {
        return transformPath(path)
    }

    override fun forUri(uri: Uri): Uri {
        var newUri = uri
        try {
            val newUriPath = transformPath(uri.toString())
            if (!TextUtils.isEmpty(newUriPath)) {
                newUri = Uri.parse(newUriPath)
            }
        } catch (ignored: Exception) {
        }
        return newUri
    }

    /**
     * 转成本地路径
     * haiyaa://gb/user/recharge -> /user/recharge
     */
    private fun transformPath(url: String): String {
        val mainContainer = "/main/container"
//        if (isH5(url)) {
//             return "/view/web"
//        } else {
//            val scheme = "haiyaa://"
//            val host = "gb"
//            if (url.startsWith(scheme)) {
//                val uri = Uri.parse(url)
//                if (uri.host.equals(host)) {
//                    return uri.path ?: mainContainer
//                }
//            }
//        }
        return url
    }

    private fun isH5(url: String): Boolean {
        if (url.startsWith("http", true) || url.startsWith("https", true)) {
            return true
        }
        return false
    }
}