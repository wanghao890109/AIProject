//package com.piproductions.hala.utils
//
///**
// * zhaogaofeng 代替sp存储，mmkv效率较高，内存较小，支持多进程存储
// */
//object MMKVInfoUtils {
//
//    private const val HttpRequestToken = "HttpRequestToken"
//    private const val HttpRequestSessionString = "HttpRequestSessionString"
//
//    //    private const val HttpRequestSessionLong = "HttpRequestSessionLong"
//    private const val HalaKey = "HalaKey"
//    private const val HalaUserId = "HalaUserId"
//    private const val HalaAddr = "HalaAddr"
//    private const val HalaGmeOpenId = "HalaGmeOpenId"
//    private const val HalaResConfig = "HalaResConfig"
//    private const val HalaRecordConfig = "HalaRecordConfig"
//    private const val HalaResMenuConfig = "HalaResMenuConfig"
//    private const val HalaSystemMessageReadTime = "HalaSystemMessageReadTime"
//    private const val HalaLocation = "HalaLocation"
//    private const val HalaOtherAccountUid = "HalaOtherAccountUid"
//
//    private const val HalaUserInfo = "HalaUserInfo"
//    private const val HalaInitAppToken = "HalaInitAppToken"
//    private const val HalaIsFirstShowLocation = "HalaIsFirstShowLocation"
//
////    private const val HalaIsUploadCid = "HalaIsUploadCid"
//    private const val HalaCidString = "HalaCidString"
//    /**
//     * Token
//     */
//    fun putTokenString(str: String?) {
//        MMKVUtils.putString(HttpRequestToken, str)
//    }
//
//    fun getTokenString(): String {
//        val token = MMKVUtils.getString(HttpRequestToken, "")
//        return if (token.isNullOrBlank()) {
//            ""
//        } else token
//    }
//
//    /**
//     * SessionId
//     */
//    fun putSessionId(sessionId: String) {
////        MMKVUtils.putLong(HttpRequestSessionLong, sessionId)
//        MMKVUtils.putString(HttpRequestSessionString, sessionId)
//    }
//
//    /**
//     *  返回Long类型的Sessionid
//     */
////    fun getSessionIdLong(): Long {
//////        return MMKVUtils.getLong(HttpRequestSessionLong, 0)
////    }
//
//    /**
//     * 返回String字符串的 SessionId
//     */
//    fun getSessionIdString(): String {
//        var sessionID = MMKVUtils.getString(HttpRequestSessionString, "")
//        return if (sessionID.isNullOrBlank()) {
//            ""
//        } else sessionID
//    }
//
//    /**
//     * key
//     */
//
//    fun putKeyString(key: String) {
//        MMKVUtils.putString(HalaKey, key)
//    }
//
//    fun getKeyString(): String {
//        var key = MMKVUtils.getString(HalaKey, "")
//        return if (key.isNullOrBlank()) {
//            ""
//        } else key
//    }
//
//    /**
//     * UserId
//     */
//    fun putUserId(openId: Long) {
//        MMKVUtils.putLong(HalaUserId, openId)
//    }
//
//    fun getUserId(): Long {
//        return MMKVUtils.getLong(HalaUserId, 0)
//    }
//
//
//    /**
//     * 鉴权地址
//     */
//    fun putAddrString(addr: String) {
//        MMKVUtils.putString(HalaAddr, addr)
//    }
//
//    fun getAddrString(): String {
//        var addr = MMKVUtils.getString(HalaAddr, "")
//        return if (addr.isNullOrBlank()) {
//            ""
//        } else addr
//    }
//
//    /**
//     * GmeOpenId 腾讯语音sdk的id
//     */
//    fun getGmeOpenIdString(): String {
//        var addr = MMKVUtils.getString(HalaGmeOpenId, "")
//        return if (addr.isNullOrBlank()) {
//            ""
//        } else addr
//    }
//
//    fun putGmeOpenIdString(gmeOpenId: String?) {
//        MMKVUtils.putString(HalaGmeOpenId, gmeOpenId)
//    }
//
//    /**
//     * 系统配置
//     */
//    fun getResConfigString(): String {
//        var resConfig = MMKVUtils.getString(HalaResConfig, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    fun putResConfigString(resConfig: String?) {
//        MMKVUtils.putString(HalaResConfig, resConfig)
//    }
//
//    fun getHalaRecordConfig(): Boolean {
//        return MMKVUtils.getBoolean(HalaRecordConfig, false)
//    }
//
//    fun putHalaRecordConfig(record: Boolean) {
//        MMKVUtils.putBoolean(HalaRecordConfig, record)
//    }
//
//    /**
//     * 我的界面菜单配置
//     */
//    fun getResMenuConfigString(): String {
//        var resConfig = MMKVUtils.getString(HalaResMenuConfig, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    fun putResMenuConfigString(resConfig: String?) {
//        MMKVUtils.putString(HalaResMenuConfig, resConfig)
//    }
//
//
//    /**
//     * 读消息的时间
//     */
//    fun putSystemMessageReadTime(time: Long) {
//        MMKVUtils.putLong(HalaSystemMessageReadTime, time)
//    }
//
//    fun getSystemMessageReadTime(): Long {
//        return MMKVUtils.getLong(HalaSystemMessageReadTime, 0)
//    }
//
//    /**
//     * 地区配置配置
//     */
//    fun getLocationString(): String {
//        var resConfig = MMKVUtils.getString(HalaLocation, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    fun putLocationString(location: String?) {
//        MMKVUtils.putString(HalaLocation, location)
//    }
//    fun getHalaOtherAccountUid(): String {
//        var resConfig = MMKVUtils.getString(HalaOtherAccountUid, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    fun putHalaOtherAccountUid(halaOtherAccountUid: String?) {
//        MMKVUtils.putString(HalaOtherAccountUid, halaOtherAccountUid)
//    }
//
//    /**
//     * 用户信息
//     */
//    fun putHalaUserInfo(halaUserInfo: String) {
//        MMKVUtils.putString(HalaUserInfo, halaUserInfo)
//    }
//
//    fun getHalaUserInfo(): String {
//        var resConfig = MMKVUtils.getString(HalaUserInfo, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    /**
//     * 用户信息
//     */
//    fun putHalaInitAppToken(halaInitAppToken: String) {
//        MMKVUtils.putString(HalaInitAppToken, halaInitAppToken)
//    }
//
//    fun getHalaInitAppToken(): String {
//        var resConfig = MMKVUtils.getString(HalaInitAppToken, "")
//        return if (resConfig.isNullOrBlank()) {
//            ""
//        } else resConfig
//    }
//
//    /**
//     * 用户信息
//     */
//    fun setIsFirstShowLocation(halaIsFirstShowLocation: Boolean) {
//        MMKVUtils.putBoolean(HalaIsFirstShowLocation, halaIsFirstShowLocation)
//    }
//
//    fun isFirstShowLocation(): Boolean {
//       return MMKVUtils.getBoolean(HalaIsFirstShowLocation, true)
//    }
//
////    /**
////     * 是否上传cid
////     */
////    fun setIsUploadCid(isUploadCid: Boolean) {
////        MMKVUtils.putBoolean(HalaIsUploadCid, isUploadCid)
////    }
////
////    fun isUploadCid(): Boolean {
////        return MMKVUtils.getBoolean(HalaIsUploadCid, true)
////    }
//
//    fun setHalaCidString(cidString: String?) {
//        MMKVUtils.putString(HalaCidString, cidString)
//    }
//
//    fun getHalaCidString(): String {
//        var  cidString=MMKVUtils.getString(HalaCidString, "")
//        return if (cidString.isNullOrBlank()) {
//            ""
//        } else cidString
//    }
////
////    fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
////        return sharedPreferences.decodeBool(key, defaultValue)
////    }
////
////    fun putFloat(key: String?, value: Float) {
////        sharedPreferences.encode(key, value)
////    }
////
////    fun getFloat(key: String?, defaultValue: Float): Float {
////        return sharedPreferences.decodeFloat(key, defaultValue)
////    }
////
////    fun putLong(key: String?, value: Long) {
////        sharedPreferences.encode(key, value)
////    }
////
////    fun getLong(key: String?, defaultValue: Long): Long {
////        return sharedPreferences.decodeLong(key, defaultValue)
////    }
////
////    /**
////     * 移除某个key值已经对应的值
////     *
////     * @param
////     * @param key
////     */
////    fun remove(key: String?) {
////        sharedPreferences.remove(key)
////    }
//}