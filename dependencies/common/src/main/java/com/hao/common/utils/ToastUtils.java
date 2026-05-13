package com.hao.common.utils;

import androidx.annotation.StringRes;


/**
 * Toast 工具类
 */

public class ToastUtils {

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        com.blankj.utilcode.util.ToastUtils.showShort(message);
    }


    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(@StringRes int message) {
        com.blankj.utilcode.util.ToastUtils.showShort(message);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        com.blankj.utilcode.util.ToastUtils.showLong(message);
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(int message) {
        com.blankj.utilcode.util.ToastUtils.showLong(message);
    }

}
