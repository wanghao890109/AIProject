package com.hao.common.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hao.common.utils.JsonUtil;
import com.hao.common.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by wanghao 2022/10/14
 */
public class SPCacheUtil {
    private SharedPreferences sharedPreferences;


    public SPCacheUtil(@NonNull Context context, @NonNull String spName) {
        sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key key
     * @param t   value
     */
    public <T> void put(String key, T t) {
        if (sharedPreferences == null || TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (t instanceof String) {
            editor.putString(key, (String) t);
        } else if (t instanceof Integer) {
            editor.putInt(key, (Integer) t);
        } else if (t instanceof Boolean) {
            editor.putBoolean(key, (Boolean) t);
        } else if (t instanceof Float) {
            editor.putFloat(key, (Float) t);
        } else if (t instanceof Long) {
            editor.putLong(key, (Long) t);
        } else {
            editor.putString(key, JsonUtil.toJson(t));
        }
        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key key
     * @param t   defaultObject
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T t) {
        if (sharedPreferences == null || TextUtils.isEmpty(key)) {
            return t;
        }

        if (t instanceof String) {
            return (T) sharedPreferences.getString(key, (String) t);
        } else if (t instanceof Integer) {
            return (T) (Integer) sharedPreferences.getInt(key, (Integer) t);
        } else if (t instanceof Boolean) {
            return (T) (Boolean) sharedPreferences.getBoolean(key, (Boolean) t);
        } else if (t instanceof Float) {
            return (T) (Float) sharedPreferences.getFloat(key, (Float) t);
        } else if (t instanceof Long) {
            return (T) (Long) sharedPreferences.getLong(key, (Long) t);
        } else {
            T value = (T) get(key, t.getClass());
            if (value != null) {
                return value;
            }
        }
        return t;
    }

    public <T> T get(String key, Class<T> clz) {
        if (sharedPreferences == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String val = sharedPreferences.getString(key, "");
        try {
            return JsonUtil.fromJson(val, clz);
        }catch (Exception e){
            LogUtil.e("JsonUtil.fromJson", e);
        }
        return null;
    }

    /**
     * 强制存到文件里commit
     */
    public void commit() {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.commit();
    }

    public void remove(String key) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public Map<String, ?> getAll(){
        if (sharedPreferences != null) {
            return sharedPreferences.getAll();
        }
        return new HashMap<>();
    }

    public void clear() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }
}
