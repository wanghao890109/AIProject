package com.hao.core.cache;


import com.hao.common.cache.SPCacheUtil;
import com.hao.core.cache.impl.ICacheService;
import com.hao.service.env.EnvironmentService;

/**
 * Created by wanghao 2022/10/14
 * 公共的cache服务，用于存储单独文件的
 */
public class CommonSPService implements ICacheService {

    private SPCacheUtil spCacheUtil;

    public CommonSPService(String spName) {
        this.spCacheUtil = new SPCacheUtil(EnvironmentService.getInstance().getContext(), spName);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key key
     * @param t   value
     */
    @Override
    public <T> void put(String key, T t) {
        if (spCacheUtil != null) {
            spCacheUtil.put(key, t);
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key key
     * @param t   defaultObject
     * @return Object
     */
    @Override
    public <T> T get(String key, T t) {
        if (spCacheUtil != null) {
            return spCacheUtil.get(key, t);
        }

        return null;
    }

    @Override
    public <T> T get(String key, Class<T> t) {
        if (spCacheUtil != null) {
            return spCacheUtil.get(key, t);
        }
        return null;
    }

    @Override
    public void remove(String key) {
        if (spCacheUtil != null) {
            spCacheUtil.remove(key);
        }
    }

    @Override
    public void clear() {
        if (spCacheUtil != null) {
            spCacheUtil.clear();
        }
    }
}
