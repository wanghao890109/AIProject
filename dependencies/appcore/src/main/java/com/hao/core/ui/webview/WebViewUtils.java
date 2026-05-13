package com.hao.core.ui.webview;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hao.core.ui.webview.action.ErrorAction;
import com.hao.core.ui.webview.action.WebViewAction;

/**
 * Created by wanghao 2023/5/26
 */
public class WebViewUtils {

    private static Gson gson = new Gson();

    public static JsonElement errorJsonElement(int error, String msg){
        ErrorAction errorAction = new ErrorAction(error, msg);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(gson.toJson(errorAction));
        return jsonElement;
    }

    public static WebViewAction errorWebAction(int code, String msg){
        JsonElement jsonElement = errorJsonElement(code, msg);
        WebViewAction action = new WebViewAction();
        action.setAction(WebViewConst.ACTION_ERROR);
        action.setData(jsonElement);
        return action;
    }

    public static String webActionToString(WebViewAction webAction) {
        return gson.toJson(webAction);
    }

    public static <T> JsonObject toJson(T t) {
        String dataJson = gson.toJson(t);
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(dataJson);
        if (jsonElement.isJsonObject()){
            return jsonElement.getAsJsonObject();
        }
        return new JsonObject();
    }
}
