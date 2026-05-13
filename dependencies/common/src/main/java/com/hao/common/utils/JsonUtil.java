package com.hao.common.utils;

import android.text.Html;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hao.common.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;

public class JsonUtil {

    public static <T> String toJson(T t) {
        if (t == null) {
            return "";
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> tClass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json, tClass);
        }catch (Exception e){
            LogUtil.e("JsonUtil.fromJson", e);
        }
        return null;
    }

    public static <T> T fromJson(String JSONData, TypeToken<T> type) {
        Gson gson = new GsonBuilder().create();
        try {
            return (T) gson.fromJson(JSONData, type.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return new Gson().fromJson(json, type);
    }

    public static <T> T fromJson(JsonObject json, Type type) {
        if (json != null) {
            return null;
        }
        return new Gson().fromJson(json, type);
    }

    public static <T> T fromFastJson(JSONObject json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        return JSON.toJavaObject(json, clazz);
    }

    public static <T> T toArrayObjectByT(String jsonStr, Type type) {
        Gson gson = new Gson();
        T o = null;
        try {
            if (jsonStr != null && !"".equals(jsonStr)) {
                o = gson.fromJson(jsonStr, type);
            }
        } catch (Exception e) {
        }
        return o;
    }

    public static Object toArrayObject(String jsonStr, Type type) {
        Gson gson = new Gson();
        Object o = null;
        try {
            if (jsonStr != null && !"".equals(jsonStr)) {
                o = gson.fromJson(jsonStr, type);
            }
        } catch (Exception e) {
        }
        return o;
    }

    /**
     * 将对象转为json
     */
    public static Object toObject(String jsonStr, Type type) {
        Gson gson = new Gson();
        Object o = null;
        try {
            if (jsonStr != null && !"".equals(jsonStr)) {
                o = gson.fromJson(jsonStr, type);
            }
        } catch (Exception e) {
        }
        return o;
    }


    public static <T> T toObjectT(String jsonStr, Type type) {
        Gson gson = new Gson();
        T o = null;
        try {
            if (jsonStr != null && !"".equals(jsonStr)) {
                o = gson.fromJson(jsonStr, type);
            }
        } catch (Exception e) {
        }
        return o;
    }


    public static String getString(JSONObject root, String key) {
        try {
            if (!root.containsKey(key)) {
                return "";
            } else {
                return Html.fromHtml(root.getString(key)).toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static <T> T disposeVirtualCases(String strJSON, Class<T> clazz) {
        try {
            return new GsonBuilder().create().fromJson(strJSON, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getInt(org.json.JSONObject root, String key) {
        try {
            if (root.isNull(key)) {
                return -1;
            } else {
                return root.getInt(key);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getString(org.json.JSONObject root, String key) {
        try {
            if (root.isNull(key)) {
                return "";
            } else {
                return Html.fromHtml(root.getString(key)).toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static double getDouble(org.json.JSONObject root, String key) {
        try {
            if (root.isNull(key)) {
                return -1;
            } else {
                return root.getDouble(key);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static long getLong(org.json.JSONObject root, String key) {
        try {
            if (root.isNull(key)) {
                return -1;
            } else {
                return root.getLong(key);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public static JSONArray getJsonArray(org.json.JSONObject root, String key) {
        try {
            return root.getJSONArray(key);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static boolean getBoolean(org.json.JSONObject root, String key) {
        try {
            String str = getString(root, key);
            return str.equals("true");
        } catch (Exception e) {
            return false;
        }
    }

    public static org.json.JSONObject getJsonObject(String jsonStr) {
        try {
            return new org.json.JSONObject(jsonStr);
        } catch (Exception e) {
            return new org.json.JSONObject();
        }
    }

    public static org.json.JSONObject getJsonObject(org.json.JSONObject js, String key) {
        try {
            return js.getJSONObject(key);
        } catch (Exception e) {
            return new org.json.JSONObject();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(String json, String key, T defaultValue) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            if (!jsonObject.has(key)) {
                return defaultValue;
            }
            return (T) jsonObject.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }
}
