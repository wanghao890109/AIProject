package com.hao.login.viewmodel

import android.annotation.SuppressLint
import com.hao.core.viewmodel.BaseViewModel
import com.hao.service.account.LoginType
/**
 * Created by wanghao 2022/7/27
 */
class LoginViewModel : BaseViewModel() {

    @SuppressLint("CheckResult")
    fun sendSms(countryCode: String, phone: String) {

    }

    @SuppressLint("CheckResult")
    fun checkSms(countryCode: String, phone: String, code: String) {

    }

    @SuppressLint("CheckResult")
    fun login(entity: String, sign: String, meta: String, phone: String, loginType: LoginType) {

    }

    @SuppressLint("CheckResult")
    fun uploadPhoto(data: ByteArray) {

    }

    @SuppressLint("CheckResult")
    fun getUserBase(userId: Long) {

    }

}