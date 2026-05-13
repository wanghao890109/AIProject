package com.hao.service.env;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
/**
 * Created by wanghao 2022/8/1
 */
public interface IEnvironmentService extends IProvider {
    /**
     * 是否是debug包
     */
    boolean isDebug();

    boolean isTestEnv();

    Context getContext();

    String getVersion();

    int getVersionCode();

    String getDeviceId();

    String getChannelId();

    String getAppName();

    String getPackageName();

    int getAppIcon();

    boolean isGoogle();
}
