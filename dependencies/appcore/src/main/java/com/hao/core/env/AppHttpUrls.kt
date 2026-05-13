package com.hao.core.env


import java.io.Serializable

/**
 * Created by wanghao 2022/10/14
 */
class AppHttpUrls : Serializable {

//    val apiDevUrl = "https://api-test.heiheiyuyin.com:9899"
//    val apiReleaseUrl = "https://campfire-api.heiheiyuyin.com"
    val apiDevUrl = "https://testsg.haiyaapi.com"
    val apiReleaseUrl = "https://sg.haiyaapi.com"

    var privacyURL: String = "https://www.haiyaa.app/policy/privacypolicy.html"//隐私协议
    var agreementURL: String = "https://www.haiyaa.app/policy/agreement.html"  // 用户协议
    var defaultIcon: String = "" // 默认头像
    var officialWeb: String = "" //官网
    var dataDeletionURL: String = "" //用户协议
}

