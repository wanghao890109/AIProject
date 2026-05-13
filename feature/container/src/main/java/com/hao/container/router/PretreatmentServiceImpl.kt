package com.hao.container.router

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.enums.TypeKind
import com.alibaba.android.arouter.facade.service.PretreatmentService
import com.alibaba.android.arouter.utils.TextUtils
import com.hao.common.utils.LogUtil
import com.hao.core.router.IPreInterceptor

/**
 * Created by wanghao 2023/6/21
 * 启动时
 */
@Route(path = "/scheme/pretreatment")
class PretreatmentServiceImpl : PretreatmentService {

    private val preInterceptors: Array<IPreInterceptor> = arrayOf(
        PayInterceptor()
    )

    override fun init(context: Context?) {

    }

    override fun onPretreatment(context: Context?, postcard: Postcard?): Boolean {
        val path: String? = postcard?.path
        LogUtil.i("PretreatmentServiceImpl : $postcard")
        //遍历参数如果为null 赋值为""
        postcard?.apply {
            extras?.apply {
                keySet()?.forEach {
                    if (get(it) == null) {
                     //   putEmptyString(it)
                    }
                }
            }
            uri?.let { it ->
                TextUtils.splitQueryParameters(it)?.forEach {
                    LogUtil.i("PretreatmentServiceImpl it: $it.value")
                    if (TextUtils.isEmpty(it.value)) {
                       // putEmptyString(it.key)
                    }
                }
            }
        }
        postcard?.let {
            for (preInterceptor in preInterceptors) {
                if (!postcard.isGreenChannel && preInterceptor.intercept(it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun Postcard.putEmptyString(key: String?) {
        if (key == null) {
            return
        }
//        RouterUtils.getParamsType(path)?.let {
//            LogUtil.d("PretreatmentServiceImpl putEmptyString path=$path key=$key ordinal=${it[key]}")
//            if (TypeKind.STRING.ordinal == it[key]) {
//                withString(key, "")
//            }
//        }
    }
}