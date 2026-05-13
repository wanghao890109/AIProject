package com.hao.core.thrower

import com.hao.common.utils.ToastUtils
import com.hao.core.env.ApiException
import com.hao.proto.ErrCode
import com.hao.service.account.AccountService


/**
 * Created by wanghao 2022/7/27
 */
class ExceptionThrower {

    fun onThrow(e: ApiException): Boolean {

        when (e.getCode()) {
            ApiException.CODE_SERVER_RETURN_ERROR -> {
                ToastUtils.showLong("Return data exception：" + e.message)
            }
            ErrCode.UrlError.value -> {
                ToastUtils.showLong("Url error：${e.getCode()}")
            }
            ErrCode.NotSvc.value -> {
                ToastUtils.showLong("Opening soon, so stay tuned")
                return true
            }
            ErrCode.TokenFail.value,
            ApiException.CODE_NOT_LOGIN -> {//登陆过期
                //token过期，跳转到登陆页
                ToastUtils.showShort(e.message)
                AccountService.getInstance().logout()
                AccountService.getInstance().gotoLogin()
                return true
            }
            ErrCode.AccFreeze.value -> { //账号被封

            }
            ErrCode.CoinNotEnough.value -> {
                ToastUtils.showLong("${e.message}")
            }
        }
        return false
    }

}