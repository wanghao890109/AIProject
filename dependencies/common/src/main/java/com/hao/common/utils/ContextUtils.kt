package com.hao.common.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

object ContextUtils {
    fun isViewDestroy(context: Context): Boolean {
        if (context is Activity) {
//        LogUtil.e("ImageViewload Activity is")
            var activity = context as Activity
            if (activity.isDestroyed()) {
//            LogUtil.e("ImageViewload activity isDestroyed")
                return true
            }
        } else if (context is Fragment) {
//        LogUtil.e("ImageViewload Fragment is")
            var fragment = context as Fragment
            var activity = fragment.getActivity()
            if (activity?.isDestroyed == true) {
//            LogUtil.e("ImageViewload Fragment== null or getActivity isDestroyed")
                return true
            }
        }
        return false

    }
}