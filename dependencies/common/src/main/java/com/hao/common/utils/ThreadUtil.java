package com.hao.common.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by wanghao 2021/2/3 15:43
 */
public class ThreadUtil {

    private static Handler sMainHandler;

    /**
     * 是否为主线程
     */
    public static boolean checkMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 将 Runnable 运行在主线程
     */
    public static void runOnMainThread(Runnable runnable) {
        checkMainHandlerIsNull();
        sMainHandler.post(runnable);
    }

    public static void remove(Runnable runnable) {
        checkMainHandlerIsNull();
        sMainHandler.removeCallbacks(runnable);
    }

    private static void checkMainHandlerIsNull() {
        if (sMainHandler == null) {
            sMainHandler = new Handler(Looper.getMainLooper());
        }
    }


    public static void runOnMainThread(Runnable runnable, long delay) {
        checkMainHandlerIsNull();
        sMainHandler.postDelayed(runnable, delay);
    }

}