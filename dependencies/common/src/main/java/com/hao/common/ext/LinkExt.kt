package com.hao.common.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.hao.common.utils.ToastUtils

fun String.link(context: Context?) {
    if (TextUtils.isEmpty(this)) {
        ToastUtils.showShort("link is error")
        return
    }
    val uri = Uri.parse(this)
    if (uri.scheme == null) {
        ToastUtils.showShort("scheme is error")
        return
    }
    if (uri.scheme?.startsWith("haiyaa") == false && uri.scheme?.startsWith("http", true) == false) {
        ToastUtils.showShort("scheme is error")
        return
    }
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(this))
    intent.setPackage(context?.packageName)
    context?.startActivity(intent)
}
