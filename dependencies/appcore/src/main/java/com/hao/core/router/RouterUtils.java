package com.hao.core.router;//package com.haiyaa.module.core.router;
//
//import androidx.annotation.Nullable;
//
//import com.alibaba.android.arouter.core.Warehouse;
//import com.alibaba.android.arouter.facade.model.RouteMeta;
//import com.alibaba.android.arouter.utils.TextUtils;
//
//import java.util.Map;
//
//public final class RouterUtils {
//
//    public static @Nullable
//    Map<String, Integer> getParamsType(String path) {
//        if (TextUtils.isEmpty(path)) {
//            return null;
//        }
//        Map<String, RouteMeta> routes = Warehouse.routes;
//        if (routes == null || routes.isEmpty()) {
//            return null;
//        }
//        RouteMeta routeMeta = routes.get(path);
//        if (routeMeta == null) {
//            return null;
//        }
//        return routeMeta.getParamsType();
//    }
//
//}
