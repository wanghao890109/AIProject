package com.hao.service.message;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by wanghao 2022/8/1
 */
public interface IMessageService extends IProvider {

    boolean isNotify();
    void isNotify(boolean notify);

    boolean isSound();
    void isSound(boolean sound);

    boolean isVibration();
    void isVibration(boolean vibration);

    void sendMessage(String content);
}
