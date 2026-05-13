package com.hao.service.account;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.android.arouter.launcher.ARouter;

import java.util.HashSet;

/**
 * Created by wanghao 2022/8/1
 */
public class AccountService implements IAccountService {

    private final IAccountService wrappedAccountService;
    private static final int MSG_LOGIN = 1;
    private static final int MSG_LOGOUT = 2;
    private static final int MSG_UPDATE = 3;
    private static final int MSG_LISTENER_ADD = 4;
    private static final int MSG_LISTENER_REMOVE = 5;

    private final Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOGIN:
                    for (AccountListener accountListener : mListeners) {
                        accountListener.onLogin(AccountService.this, (LoginType) msg.obj);
                    }
                    break;
                case MSG_LOGOUT:
                    for (AccountListener accountListener : mListeners) {
                        accountListener.onLogout(AccountService.this);
                    }
                    break;
                case MSG_UPDATE:
                    for (AccountListener accountListener : mListeners) {
                        accountListener.onUpdated(AccountService.this);
                    }
                    break;
                case MSG_LISTENER_ADD:
                    mListeners.add((AccountListener) msg.obj);
                    break;
                case MSG_LISTENER_REMOVE:
                    mListeners.remove(msg.obj);
                    break;
            }
        }
    };


    private HashSet<AccountListener> mListeners = new HashSet<>();

    private static class Inner {
        private static AccountService sInstance = new AccountService();
    }

    private AccountService() {
        this.wrappedAccountService = ARouter.getInstance().navigation(IAccountService.class);
    }

    /**
     * @return 返回当前AccountService的实例
     */
    public static AccountService getInstance() {
        return Inner.sInstance;
    }

    @Override
    public boolean isLogin() {
        return wrappedAccountService.isLogin();
    }

    @Override
    public void gotoLogin() {
        wrappedAccountService.gotoLogin();
    }

    @Override
    public void login(Object loginInfo, LoginType type) {
        wrappedAccountService.login(loginInfo, type);
        Message.obtain(mUIHandler, MSG_LOGIN, type).sendToTarget();
    }

    @Override
    public void logout() {
        wrappedAccountService.logout();
        Message.obtain(mUIHandler, MSG_LOGOUT).sendToTarget();
    }

    @Override
    public void update(Object userInfo) {
        wrappedAccountService.update(userInfo);
        Message.obtain(mUIHandler, MSG_UPDATE).sendToTarget();
    }

    @Override
    public String getToken() {
        return wrappedAccountService.getToken();
    }

    @Override
    public String getSession() {
        return wrappedAccountService.getSession();
    }

    @Override
    public long getUserId() {
        return wrappedAccountService.getUserId();
    }

    @Override
    public long getAccId() {
        return wrappedAccountService.getAccId();
    }

    @Override
    public String getUserIcon() {
        return wrappedAccountService.getUserIcon();
    }

    @Override
    public String getUserName() {
        return wrappedAccountService.getUserName();
    }

    @Override
    public <T> T getAccountInfo(Class<? extends T> clz) {
        return wrappedAccountService.getAccountInfo(clz);
    }

    @Override
    public void fetchAccountInfo() {
        wrappedAccountService.fetchAccountInfo();
    }

    /**
     * 注册监听
     */
    public void register(AccountListener listener) {
        Message.obtain(mUIHandler, MSG_LISTENER_ADD, listener).sendToTarget();
    }

    /**
     * 取消监听
     */
    public void unregister(AccountListener listener) {
        Message.obtain(mUIHandler, MSG_LISTENER_REMOVE, listener).sendToTarget();
    }

    @Override
    public void init(Context context) {

    }
}
