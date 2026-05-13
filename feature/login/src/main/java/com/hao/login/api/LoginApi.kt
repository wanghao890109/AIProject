package com.hao.login.api

import com.hao.core.net.ApiService

/**
 * Created by wanghao 2022/7/27
 */
object LoginApi {
    
    fun getService(): LoginService {
        return ApiService.instance.create(LoginService::class.java)
    }

}