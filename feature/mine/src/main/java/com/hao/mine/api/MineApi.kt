package com.hao.mine.api

import com.hao.core.net.ApiService


/**
 * Created by Li 2022/10/25
 */
object MineApi {

    fun getService(): MineService {
        return ApiService.instance.create(MineService::class.java)
    }

}