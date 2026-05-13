package com.hao.core.cache.impl;

/**
 * Created by wanghao 2022/10/14
 */
public interface ICacheService {

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key key
     * @param t   value
     */
    <T> void put(String key, T t);


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key key
     * @param t   defaultValue
     * @return 值
     */
    <T> T get(String key, T t);

    /**
     * 直接获取反序列化后的对象实例
     *
     * @param key key
     * @param t   对象Class
     * @return 反序列化实例
     */
    <T> T get(String key, Class<T> t);

    /**
     * 移除某个缓存
     *
     * @param key
     */
    void remove(String key);


    /**
     * 清除所有缓存
     */
    void clear();
}
