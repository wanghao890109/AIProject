package com.hao.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Pair;

import androidx.core.content.FileProvider;

import java.io.File;


public class ApkUtils {

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        return isInstalled(context, "com.tencent.mobileqq");
    }

    /**
     * 判断微信是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvailable(Context context) {
        return isInstalled(context, "com.tencent.mm");
    }


    /**
     * 判断球球是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQIUQIUInstalled(Context context) {
        return isInstalled(context, "com.ztgame.bob");
    }


    public static boolean isInstalled(Context c, String pkgName) {
        PackageManager mPm = c.getPackageManager();
        if (mPm == null) {
            return false;
        } else {
            PackageInfo pkginfo = null;

            try {
                pkginfo = mPm.getPackageInfo(pkgName, 0);
            } catch (PackageManager.NameNotFoundException e) {
            } catch (Exception e) {

            }

            return pkginfo != null;
        }
    }

    public static boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }


    public static boolean startInstallActivity(Context c, File file) {
        Uri uri;
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(c, c.getPackageName() + ".provider", file);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }

        try {
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");//设置intent的数据类型
            c.startActivity(installIntent);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 获取指定包名的版本号
     *
     * @param context     本应用程序上下文
     * @param packageName 你想知道版本信息的应用程序的包名
     * @return
     * @throws Exception
     */
    public static Pair<String, Integer> getVersionName(Context context, String packageName) {
        try {
            // 获取PackageManager的实例
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(packageName, 0);
            return new Pair<>(packInfo.versionName, packInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Pair<String, Integer> getHeyVersionName(Context context) {
        return getVersionName(context, "com.ztgame.bigbang.app.hey");
    }
}
