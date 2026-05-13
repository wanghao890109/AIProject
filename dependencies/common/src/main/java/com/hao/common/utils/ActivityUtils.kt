package com.hao.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Created by wanghao 2021/1/28 16:41
 */
object ActivityUtils {

    @SuppressLint("WrongConstant")
    fun fixOrientation(activity: Activity?): Boolean {
        try {
            val field: Field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o: ActivityInfo = field.get(activity) as ActivityInfo
            o.screenOrientation = -1
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isTranslucentOrFloating(activity: Activity): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes =
                Class.forName("com.android.internal.R\$styleable").getField("Window")[null] as IntArray
            val ta: TypedArray = activity.obtainStyledAttributes(styleableRes)
            val m: Method = ActivityInfo::class.java.getMethod("isTranslucentOrFloating", TypedArray::class.java)
            m.setAccessible(true)
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.setAccessible(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    fun activityIsDestroyed(context: Context?): Boolean {
        return if (context is Activity) {
            context.isDestroyed || context.isFinishing
        } else false
    }
}