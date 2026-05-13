package com.hao.service.config;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by wanghao 2022/8/1
 */
public class ConfigService implements IConfigService {

    private IConfigService iConfigService;

    private static class Inner {
        private static ConfigService sInstance = new ConfigService();
    }

    public static ConfigService getInstance() {
        return Inner.sInstance;
    }

    private ConfigService() {
        iConfigService = ARouter.getInstance().navigation(IConfigService.class);
    }

    @Override
    public void setServerTime(Long unix) {
        iConfigService.setServerTime(unix);
    }

    @Override
    public long getServerTime() {
        return iConfigService.getServerTime();
    }

    @Override
    public <T> void putConfigCache(T t) {
        iConfigService.putConfigCache(t);
    }

    @Override
    public <T> T getConfigCache(Class<? extends T> clz) {
        return iConfigService.getConfigCache(clz);
    }

    @Override
    public void init(Context context) {

    }
}
