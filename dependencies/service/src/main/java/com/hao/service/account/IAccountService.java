package com.hao.service.account;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 */
public interface IAccountService extends IProvider {

    /**
     * @return 是否登录
     */
    boolean isLogin();

    /**
     * 跳转到登录页
     */
    void gotoLogin();

    /**
     * 登录
     *
     * @param loginInfo 登录信息
     * @param type     登录的类型
     */
    void login(Object loginInfo, LoginType type);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 更新信息
     *
     * @param userInfo 待更新的账户信息
     */
    void update(Object userInfo);

    /**
     * @return 返回token
     */
    String getToken();

    /**
     * @return 返回session
     */
    String getSession();


    /**
     * @return 返回用户ID
     */
    long getUserId();

    long getAccId();

    String getUserIcon();

    String getUserName();


    <T> T getAccountInfo(Class<? extends T> clz);

    /**
     * 更新用户信息
     */
    void fetchAccountInfo();
}
