package com.hao.service.account;


import androidx.annotation.UiThread;

/**
 * Created by wanghao 2022/8/1
 */
public interface AccountListener {
    /**
     * 用户登录回调
     */
    @UiThread
    void onLogin(IAccountService sender, LoginType type);

    /**
     * 退出登录的回调,在UI线程
     */
    @UiThread
    void onLogout(IAccountService sender);

    /**
     * 用户登录或者退出登录时不会被call到，用户信息更新时才会被call到。
     */
    @UiThread
    void onUpdated(IAccountService sender);
}
