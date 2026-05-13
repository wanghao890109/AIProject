package com.hao.core.ui.webview.action;

import com.google.gson.JsonObject;
import com.hao.core.ui.webview.WebViewUtils;

public abstract class BaseAction {

    public JsonObject toJson() {
        return WebViewUtils.toJson(this);
    }
}
