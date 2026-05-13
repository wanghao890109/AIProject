package com.hao.service.verify;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by wanghao 2022/8/1
 */
public class VerifyService implements IVerifyService {

    private IVerifyService iVerifyService;

    private static class Inner {
        private static VerifyService sInstance = new VerifyService();
    }

    public static VerifyService getInstance() {
        return Inner.sInstance;
    }

    private VerifyService() {
        iVerifyService = ARouter.getInstance().navigation(IVerifyService.class);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void verify(int verifyType, VerifyListener listener) {
        iVerifyService.verify(verifyType, listener);
    }

    @Override
    public void verify(int verifyType, String account, VerifyListener listener) {
        iVerifyService.verify(verifyType, account, listener);
    }
}
