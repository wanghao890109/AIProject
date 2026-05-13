package com.hao.common.utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetWorkUtil {

    private static Application sApplication;
    public static void init(Application application) {
        sApplication = application;
    }
    /**
     * 检测网络是否连接
     */
    public static boolean isNetConnected() {

        ConnectivityManager cm = (ConnectivityManager) sApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = cm.getAllNetworks();
                    if (networks != null) {
                        NetworkInfo networkInfo;
                        for (Network mNetwork : networks) {
                            networkInfo = cm.getNetworkInfo(mNetwork);
                            if (networkInfo != null && networkInfo.isConnected()) {
                                return true;
                            }
                        }
                    }
                } else {
                    NetworkInfo[] infos = cm.getAllNetworkInfo();
                    if (infos != null) {
                        for (NetworkInfo ni : infos) {
                            if (ni != null && ni.isConnected()) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.e("isNetConnected");
            }

        }
        return false;
    }

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测3G是否连接
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    public static int getConnectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            try {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo != null) {

                    switch (networkInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:
                            return 1;
                        case ConnectivityManager.TYPE_WIFI:
                            return 2;
                        case ConnectivityManager.TYPE_WIMAX:
                            return 3;
                        case ConnectivityManager.TYPE_ETHERNET:
                            return 4;
                        case ConnectivityManager.TYPE_BLUETOOTH:
                            return 5;
                    }
                }
            } catch (Throwable e) {

            }

        }
        return 0;
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取运营商名字
     *
     * @return int
     */
    public static String getOperatorName(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // getSimOperatorName就可以直接获取到运营商的名字
        return telephonyManager.getSimOperatorName();
    }
}
