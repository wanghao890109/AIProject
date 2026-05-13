//package com.hao.common.utils
//
//import android.content.Context
//import android.os.Parcelable
//import com.tencent.mmkv.MMKV
//
///**
// * zhaogaofeng 代替sp存储，mmkv效率较高，内存较小，支持多进程存储
// */
//object MMKVUtils {
//    private const val KEY_PROCESS = "HalaProcessKv"
//
//    /**
//     * 初始化mmkv
//     *
//     * @param context
//     */
//    fun init(context: Context?) {
//        MMKV.initialize(context)
//    }
//
//    /**
//     * 支持多进行
//     *
//     * @return
//     */
//    private val sharedPreferences: MMKV
//        private get() = MMKV.mmkvWithID(KEY_PROCESS, MMKV.MULTI_PROCESS_MODE)
//
//    fun putString(key: String?, value: String?) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun getString(key: String?): String? {
//        return sharedPreferences.decodeString(key, "")
//    }
//
//    fun getString(key: String?, defaultValue: String?): String? {
//        return sharedPreferences.decodeString(key, defaultValue)
//    }
//
//    fun putInt(key: String?, value: Int) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun getInt(key: String?, defaultValue: Int): Int {
//        return sharedPreferences.decodeInt(key, defaultValue)
//    }
//
//    fun putBoolean(key: String?, value: Boolean) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
//        return sharedPreferences.decodeBool(key, defaultValue)
//    }
//
//    fun putFloat(key: String?, value: Float) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun getFloat(key: String?, defaultValue: Float): Float {
//        return sharedPreferences.decodeFloat(key, defaultValue)
//    }
//
//    fun putLong(key: String?, value: Long) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun getLong(key: String?, defaultValue: Long): Long {
//        return sharedPreferences.decodeLong(key, defaultValue)
//    }
//
//    fun putParcelable(key: String?, value: Parcelable) {
//        sharedPreferences.encode(key, value)
//    }
//
//    fun <T : Parcelable?> getParcelable(key: String?, tClass: Class<T>?): T? {
//        return sharedPreferences.decodeParcelable(key, tClass)
//    }
//
//    /**
//     * 移除某个key值已经对应的值
//     *
//     * @param
//     * @param key
//     */
//    fun remove(key: String?) {
//        sharedPreferences.remove(key)
//    }
//
//    fun clear() {
//        sharedPreferences.clearAll()
//    }
//}