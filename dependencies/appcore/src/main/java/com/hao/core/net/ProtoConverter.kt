package com.hao.core.net


import com.hao.common.utils.LogUtil
import com.hao.core.env.ApiException
import com.hao.proto.PbHead
import com.hao.proto.UtilRet
import com.squareup.wire.ProtoAdapter
import okio.ByteString
import java.lang.RuntimeException

/**
 * Created by wanghao 2022/7/27
 */

class ProtoConverter constructor(val listener: ApiInitListener?) :
    ProtoConverterFactory.ConverterFactoryListener {

    override fun encodeReqBase(classType: Class<*>?, bytes: ByteArray?): ByteArray {

        return if (classType == PbHead::class.java) bytes!! else {
            val reqBase = PbHead.Builder()
            reqBase.pbBody = ByteString.of(*bytes!!)
            listener?.let {
                val deviceBase = it.onGetDevice()
                val userId = it.onGetUserId()
                val session = it.onGetSession()
                reqBase.uid(userId)
                reqBase.token(session)
                reqBase.dev(deviceBase)
            }
            val pbHead = reqBase.build()
            //Logger.i("Token error， pbHead ${pbHead}")
            pbHead.encode()
        }
    }

    override fun decodeRetBase(classType: Class<*>?, bytes: ByteArray?): Any {
        if (classType == null) {
            throw RuntimeException()
        }
        val adapter = ProtoAdapter.get(PbHead::class.java)
        if (bytes == null || bytes.isEmpty()) {
            throw ApiException(ApiException.CODE_SERVER_RETURN_ERROR, "0-byte body")
        }
        val retBase = adapter.decode(bytes!!)

        val code = retBase.pam
        if (code != null && code != 0) {
            val adapter = ProtoAdapter.get(UtilRet::class.java)
            val retBase = adapter.decode(retBase.pbBody)
            val exception = ApiException(code, "${retBase.retTxt}")
            //listener?.onThrow(exception)
            LogUtil.e("HTTP-> code:$code   error:$retBase")
            throw exception
        }
        return if (classType == PbHead::class.java) {
            LogUtil.d("HTTP-> code:$code   ret:$retBase")
            retBase
        } else {
            val dataAdapter = ProtoAdapter.get(classType)
            val any = dataAdapter.decode(retBase.pbBody.toByteArray())
            LogUtil.d("HTTP-> code:$code   data:$any")
            any
        }
    }
}