package com.hao.core.env

import com.hao.service.env.EnvironmentService
import java.io.Serializable

/**
 * Created by wanghao 2022/7/27
 */

class ApiException(
    private val code: Int,
    override val message: String?
) :
    Throwable(message), Serializable {


    companion object {
        const val CODE_UNKNOWN = -1 //未知错误
        const val CODE_NET_NOT_CONNECTED = -2 //无网络
        const val CODE_SERVER_RETURN_ERROR = -3 //服务器返回的错误码

        const val CODE_NOT_LOGIN = -10000 //未登录

        const val CODE_PARTY_OWNER_JOIN_NEED_LEAVE = -20001 //房主切换房间前需退出
        const val CODE_PARTY_NOT_EXIST = -20002 //房间不存在

        const val CODE_REQUEST_CANCEL = 1 //请求被取消
        const val CODE_REQUEST_TIME_OUT = 2 //请求超时
        const val CODE_TCP_NOT_CONNECTED = 3 //TCP无网络
        const val CODE_NO_RESISTER_PROTO = 4 //未注册pb对应关系
        const val CODE_PROTO_ENCODE_DECODE_ERROR = 5 //pb解析失败


        const val CODE_VOICE_ERROR = 1000 //语音连接失败
        const val CODE_VOICE_ERROR_PERMISSION = 1001 //语音连接无权限

    }

    fun getCode(): Int {
        return code
    }

    override fun toString(): String {
        if (code == CODE_REQUEST_CANCEL) {
            return ""
        }
        return if (EnvironmentService.getInstance().isDebug) {
            "$code : $message"
        } else {
            "$message"
        }
    }
}
