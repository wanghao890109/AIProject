package com.hao.service.pay;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 */
public interface IPayService extends IProvider {

    void startPay(Activity activity, String productId);


}
