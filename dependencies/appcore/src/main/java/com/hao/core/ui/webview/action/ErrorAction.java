package com.hao.core.ui.webview.action;

/**
 * Created by wanghao 2023/5/26
 */
public class ErrorAction {
    public int error;
    public String msg;

    public ErrorAction(int error, String msg) {
        this.error = error;
        this.msg = msg;
    }
}
