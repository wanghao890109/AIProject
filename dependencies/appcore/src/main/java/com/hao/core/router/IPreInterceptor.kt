package com.hao.core.router

import com.alibaba.android.arouter.facade.Postcard

/**
 * Created by wanghao 2023/6/21
 */
interface IPreInterceptor {
    fun intercept(postcard: Postcard): Boolean
}