package com.hao.core.socket

import com.android.socket.client.core.protocol.IReaderProtocol
import com.android.socket.client.core.utils.SLog
import java.nio.ByteBuffer
import java.nio.ByteOrder


/**
 * Created by wanghao 2021/2/3 14:49
 */
class SocketProtocol : IReaderProtocol {

    override fun getBodyLength(header: ByteArray?, byteOrder: ByteOrder?): Int {
        if (header == null || header.size < headerLength) {
            SLog.i("OkSocket ,  header == null || header.size < headerLength")
            return 0
        }
        val bb: ByteBuffer = ByteBuffer.wrap(header)
        bb.order(byteOrder)
        val getInt = bb.int

        SLog.i("OkSocket ,getBodyLength : $getInt")
        return getInt - 4
    }

    override fun getHeaderLength(): Int {
        return 4
    }
}