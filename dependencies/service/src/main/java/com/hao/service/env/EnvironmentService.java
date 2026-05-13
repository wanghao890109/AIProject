package com.hao.service.env;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by wanghao 2022/8/1
 */
public class EnvironmentService implements IEnvironmentService {

    private Context mContext;
    private IEnvironmentService iEnvironmentService;

    private static class Inner {
        private static EnvironmentService sInstance = new EnvironmentService();
    }

    public static EnvironmentService getInstance() {
        return Inner.sInstance;
    }

    private EnvironmentService() {
        iEnvironmentService = ARouter.getInstance().navigation(IEnvironmentService.class);
    }

    @Override
    public boolean isDebug() {
        return iEnvironmentService.isDebug();
    }

    @Override
    public boolean isTestEnv() {
        return iEnvironmentService.isTestEnv();
    }

    @Override
    public Context getContext() {
        return iEnvironmentService.getContext();
    }

    @Override
    public String getAppName() {
        return iEnvironmentService.getAppName();
    }

    @Override
    public String getPackageName() {
        return iEnvironmentService.getPackageName();
    }

    @Override
    public int getAppIcon() {
        return iEnvironmentService.getAppIcon();
    }

    @Override
    public String getVersion() {
        return iEnvironmentService.getVersion();
    }

    @Override
    public int getVersionCode() {
        return iEnvironmentService.getVersionCode();
    }

    @Override
    public String getDeviceId() {
        return iEnvironmentService.getDeviceId();
    }

    @Override
    public String getChannelId() {
        return iEnvironmentService.getChannelId();
    }

    @Override
    public boolean isGoogle() {
        return iEnvironmentService.isGoogle();
    }

    @Override
    public void init(Context context) {
      //  mContext = context;
    }
}
