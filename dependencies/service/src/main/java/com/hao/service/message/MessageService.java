package com.hao.service.message;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by wanghao 2022/8/1
 */
public class MessageService implements IMessageService {

    private IMessageService iMessageService;

    private static class Inner {
        private static MessageService sInstance = new MessageService();
    }

    public static MessageService getInstance() {
        return Inner.sInstance;
    }

    private MessageService() {
        iMessageService = ARouter.getInstance().navigation(IMessageService.class);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public boolean isNotify() {
        return iMessageService.isNotify();
    }

    @Override
    public void isNotify(boolean notify) {
        iMessageService.isNotify(notify);
    }

    @Override
    public boolean isSound() {
        return iMessageService.isSound();
    }

    @Override
    public void isSound(boolean sound) {
        iMessageService.isSound(sound);
    }

    @Override
    public boolean isVibration() {
        return iMessageService.isVibration();
    }

    @Override
    public void isVibration(boolean vibration) {
        iMessageService.isVibration(vibration);
    }

    @Override
    public void sendMessage(String content) {
        iMessageService.sendMessage(content);
    }
}
