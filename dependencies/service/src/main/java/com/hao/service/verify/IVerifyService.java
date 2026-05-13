package com.hao.service.verify;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 */
public interface IVerifyService extends IProvider {
    /**
     * @param verifyType LoginType
     * @param listener
     */
    void verify(int verifyType, VerifyListener listener);


    void verify(int verifyType, String account, VerifyListener listener);

}
