package com.hao.core.socket

import com.hao.common.utils.IDWorker
import com.hao.core.net.PbDeviceInfo
import com.hao.service.account.AccountService
import com.hao.proto.PbHead
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import okio.ByteString
import java.io.IOException

/**
 * Created by wanghao 2021/2/3 16:34
 */
object SocketUtil {
    fun newSendMessage(cmd: Int, userId: Long, bytes: ByteArray?): SocketMessage {
        val id = IDWorker.get().getId()
        val headBuilder = PbHead.Builder()
            .cmd(cmd)
            .uid(userId)
            .mid(id)
            .pam(0)
            .dev(PbDeviceInfo.getDeviceInfo())
            .token(AccountService.getInstance().token)
        bytes?.let {
            headBuilder.pbBody(okio.ByteString.of(*it))
        }
        val head = headBuilder.build()

        //LogUtil.i("Socket send head: $head")
        return SocketMessage(id, head.encode())
    }

    fun decode(type: Class<*>, data: ByteString): Message<*, *>? {
        val protoAdapter = ProtoAdapter.get(type)
        return try {
            protoAdapter.decode(data) as Message<*, *>?
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}