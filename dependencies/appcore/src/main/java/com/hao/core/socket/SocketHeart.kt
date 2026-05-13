package com.hao.core.socket

import com.android.socket.client.core.iocore.interfaces.IPulseSendable
import com.hao.proto.GateCmd
import com.hao.proto.NilProto
import com.hao.proto.PbHead
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by wanghao 2021/2/3 14:44
 */
class SocketHeart(val userId: Long) : IPulseSendable {
    private var data: ByteArray? = null

    init {
        val cmd = GateCmd.TCP_HEART_BEAT_CMD.value
        val messageId = 0
        val pbBody = NilProto.Builder().build().encode()
        data = PbHead.Builder()
            .cmd(cmd)
            .uid(userId)
            .mid(messageId)
            .pbBody(okio.ByteString.of(*pbBody))
            .build()
            .encode()
    }


    override fun parse(): ByteArray {
        if (data == null) {
            return ByteArray(0)
        }
        val bb: ByteBuffer = ByteBuffer.allocate(4 + data!!.size)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.putInt(data!!.size + 4)
        bb.put(data)
        return bb.array()
    }
}