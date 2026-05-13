package com.hao.permission;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.Stack;

class ActivityHelper {

    private static final Stack<Activity> mActivityList = new Stack<>();

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivityList.add(activity);
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                mActivityList.remove(activity);
            }
        });
    }

    /**
     * 当前Activity
     */
    static Activity getCurrentActivity() {
        int size = mActivityList.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = mActivityList.get(i);
            if (isActivityAlive(activity)) {
                return activity;
            }
        }
        return null;
    }

    public static boolean isActivityAlive(final Activity activity) {
        return activity != null && !activity.isFinishing()
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed());
    }
}
