package com.hao.core.cache;


/**
 * Created by wanghao 2022/10/14
 * 基于SharedPreferences的缓存工具类
 */
public class SPCacheService extends CommonSPService {

    protected static final String COMMON_CONFIG = "app_common_config";


    private static class Inner {
        private static SPCacheService sInstance = new SPCacheService();
    }

    private SPCacheService() {
        super(COMMON_CONFIG);
    }

    /**
     * @return 返回当前CacheService的实例
     */
    public static SPCacheService getInstance() {
        return Inner.sInstance;
    }

}
