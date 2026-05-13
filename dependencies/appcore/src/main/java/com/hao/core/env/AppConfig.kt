package com.hao.core.env

import java.io.Serializable

/**3500 - 350  = 3200
 * Created by wanghao 2022/10/14
 */
class AppConfig : Serializable {
    var language: Int = 1//DeviceInfo.Languages.zh.value //语言
    var isAcceptAgreement: Boolean = true// 是否同意隐私协议
    var httpBaseUrl: String = "" //当前http环境
}