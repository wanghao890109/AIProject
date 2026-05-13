package com.hao.login.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.hao.common.utils.AppLifecycleManager
import com.hao.common.utils.LogUtil
import com.hao.core.router.RouterCallBack
import com.hao.proto.LoginInfo
import com.hao.proto.UserBase
import com.hao.proto.UserInfo
import com.hao.proto.WelthInfo
import com.hao.service.account.IAccountService
import com.hao.service.account.LoginType
import com.squareup.wire.Message
/**
 * Created by wanghao 2022/7/27
 */
@Route(path = "/account/service")
class HaoAccountService : IAccountService {

    private val handler: Handler = Handler(Looper.getMainLooper())

    //private var loginInfo: LoginInfo? = LoginSpService.getRetLogin()
    private var loginInfo: LoginInfo? = null

    override fun isLogin(): Boolean {
        return loginInfo?.user?.userId ?: 0 > 0
    }

    override fun gotoLogin() {
        val url = "/login/start"
        ARouter.getInstance().build(url).navigation(null, object : RouterCallBack() {
            override fun onArrival(postcard: Postcard?) {
                val postcard = ARouter.getInstance().build(url)
                LogisticsCenter.completion(postcard)
                val loginClazz = postcard.destination
                handler.postDelayed({
                    AppLifecycleManager.getInstance().clearActivities(loginClazz)
                }, 300)
            }
        })
    }

    override fun login(loginInfo: Any, type: LoginType) {
        LogUtil.i("登陆 ${loginInfo}")
        if (loginInfo is LoginInfo) {
            this.loginInfo = loginInfo
          //  LoginSpService.putRetLogin(loginInfo)
        }
    }

    override fun logout() {
        LogUtil.i("退出登陆")
        loginInfo = null
       // LoginSpService.putRetLogin(null)
    }

    override fun update(userInfo: Any) {
        LogUtil.i("更新用户数据")
        if (userInfo is LoginInfo) {
            loginInfo = userInfo
        } else if (userInfo is UserInfo) {
            loginInfo?.user = userInfo
        } else if (userInfo is UserBase) {
            loginInfo?.user?.userBase = userInfo
        } else if (userInfo is WelthInfo) {
            loginInfo?.user?.userWelth = userInfo
        }

        if (loginInfo != null) {
          //ß  LoginSpService.putRetLogin(loginInfo)
        }
    }

    override fun getToken(): String {
        return loginInfo?.tokenId ?: ""
    }

    override fun getSession(): String {
        return loginInfo?.tokenId ?: ""
    }

    override fun getUserId(): Long {
        return loginInfo?.user?.userId ?: 0
    }

    override fun getAccId(): Long {
        return loginInfo?.user?.userBase?.acctId ?: 0
    }

    override fun getUserIcon(): String {
        return loginInfo?.user?.userBase?.avatar ?: ""
    }

    override fun getUserName(): String {
        return loginInfo?.user?.userBase?.nickName ?: ""
    }

    override fun <T> getAccountInfo(clz: Class<out T>): T? {
        var any: Message<*,*> ? = null
        if (clz == LoginInfo::class.java) {
            any = loginInfo
        } else if (clz == UserInfo::class.java) {
            any = loginInfo?.user
        } else if (clz == UserBase::class.java) {
            any = loginInfo?.user?.userBase
        }
        return any?.newBuilder()?.build() as T
    }

    override fun fetchAccountInfo() {
//        LoginApi.getService().getUserInfo(UserIdQuery(userId, 0, 1))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                AccountService.getInstance().update(it)
//            }, {
//                ToastUtils.showShort(it.message)
//            })
    }

    override fun init(context: Context) {}
}