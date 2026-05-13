package com.hao.core.ui.webview.action;

import com.google.gson.JsonElement;

import java.io.Serializable;

/**
 * Created by wanghao 2023/5/22
 */
public class WebViewAction implements Serializable {
    public String getAction() {
        return action;
    }

    public JsonElement getData() {
        return data;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    private String action;
    private JsonElement data;

    @Override
    public String toString() {
        return "WebAction{" +
                "action='" + action + '\'' +
                ", data=" + data +
                '}';
    }
}
