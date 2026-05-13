package com.hao.service.source;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 */
public interface IResourceService extends IProvider {


    void loadResource();

    <T> T getResource(Class<? extends T> clz);

    @Override
    default void init(Context context) {

    }

}
