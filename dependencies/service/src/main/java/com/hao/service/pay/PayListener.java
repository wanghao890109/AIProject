package com.hao.service.pay;


import androidx.annotation.UiThread;

/**
 * Created by wanghao 2022/8/1
 */
public interface PayListener {
    /**
     * 用户登录回调
     */
    @UiThread
    void onSucceed(String orderId, int code, String error);


}
