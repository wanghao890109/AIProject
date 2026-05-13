package com.hao.common.ext

import android.content.Context

fun String?.ellipsis(context: Context, maxLength: Int): String {
    if (isNullOrBlank()) {
        return ""
    }
    return if (length > maxLength) {
        substring(0, maxLength - 1) + context.getString(com.hao.common.R.string.ellipsis)
    } else {
        this
    }
}

fun String?.ellipsis(context: Context): String {
    return ellipsis(context,20)
}