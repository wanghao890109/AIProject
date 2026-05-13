package com.hao.core.socket


import com.android.socket.client.core.iocore.interfaces.ISendable
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by wanghao 2021/2/3 14:44
 */
class SocketMessage(val id: Int, val data: ByteArray) : ISendable {

    override fun parse(): ByteArray {
        val bb: ByteBuffer = ByteBuffer.allocate(4 + data.size)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        bb.putInt(data.size+4)
        bb.put(data)
        val array =  bb.array()

     //   SLog.i("SocketMessage  data.size:"+data.size+ " data:"+ BytesUtils.toHexStringForLog(data))

       // SLog.i("SocketMessage  array.size:"+array.size+ " array:"+ BytesUtils.toHexStringForLog(array))
        return array
    }
}