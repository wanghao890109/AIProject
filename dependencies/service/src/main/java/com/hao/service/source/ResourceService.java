package com.hao.service.source;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by wanghao 2022/8/5
 */
public class ResourceService implements IResourceService{

    private IResourceService iResourceService;

    private static class Inner {
        private static ResourceService sInstance = new ResourceService();
    }

    private ResourceService() {
        this.iResourceService = ARouter.getInstance().navigation(IResourceService.class);
    }

    public static ResourceService getInstance() {
        return Inner.sInstance;
    }

    @Override
    public void loadResource() {
        iResourceService.loadResource();
    }

    @Override
    public <T> T getResource(Class<? extends T> clz) {
        return iResourceService.getResource(clz);
    }

    @Override
    public void init(Context context) {

    }
}
