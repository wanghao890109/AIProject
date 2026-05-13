package com.hao.service.config;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 *
 * 配置 or 开关
 */
public interface IConfigService extends IProvider {

    void setServerTime(Long unix);

    long getServerTime();

    <T> void putConfigCache(T t);

    <T> T getConfigCache(Class<? extends T> clz);
}
