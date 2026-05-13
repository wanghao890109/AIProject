package com.hao.mine.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.hao.mine.api.MineApi
import com.hao.common.utils.ToastUtils
import com.hao.core.env.ApiException
import com.hao.core.net.ApiObserver
import com.hao.core.viewmodel.BaseViewModel
import com.hao.service.account.AccountService
import com.hao.proto.*

class MineViewModel : BaseViewModel() {

    var minePage = MutableLiveData<UserPage?>()

    /**
     * 我的中心
     */
    @SuppressLint("CheckResult")
    fun getUserPage() {
        MineApi.getService().getUserPage(UserIdQuery(AccountService.getInstance().userId, 0, 0))
            .compose(subscribe(object : ApiObserver<UserPage>() {
                override fun onNext(t: UserPage) {
                    dismissLoading()
                    update(t)
                    minePage.value = t
                }

                override fun onError(exception: ApiException) {
                    dismissLoading()
                    ToastUtils.showShort(exception.message)
                    minePage.value = null
                }

                private fun update(t: UserPage) {
                    val userInfo = AccountService.getInstance().getAccountInfo(UserInfo::class.java)
                    userInfo.userWelth = t.uwelth
                    userInfo.userBase = t.user
                    AccountService.getInstance().update(t.uwelth)
                }

            }))
    }

}