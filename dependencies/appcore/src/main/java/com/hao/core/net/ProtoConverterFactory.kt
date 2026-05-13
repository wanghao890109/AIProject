package com.hao.core.net

import com.hao.common.utils.LogUtil
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import kotlin.jvm.Throws

/**
 * Created by wanghao 2022/7/27
 */
class ProtoConverterFactory(private val mConverterFactoryListener: ConverterFactoryListener?) : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type !is Class<*>) {
            return null
        }
        val classType = type
        return if (!Message::class.java.isAssignableFrom(classType)) {
            null
        } else ProtoResponseBodyConverter<Message<*, *>>(classType)
    }

    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type !is Class<*>) {
            return null
        }
        val classType = type
        if (!Message::class.java.isAssignableFrom(classType)) {
            return null
        }
        if (type !is Class<*>) {
            return null
        }

        return ProtoRequestBodyConverter<Message<*, *>>(classType)
    }

    internal inner class ProtoRequestBodyConverter<T : Message<*, *>?>(
        private val classType: Class<*>
    ) : Converter<T, RequestBody> {
        private val MEDIA_TYPE: MediaType = "application/x-protobuf".toMediaType()

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody {
            LogUtil.d("HTTP-> req:$value")
            var bytes: ByteArray? = value!!.encode()
            if (mConverterFactoryListener != null) {
                bytes = mConverterFactoryListener.encodeReqBase(classType, bytes)
                if (bytes != null) {
                    return RequestBody.create(MEDIA_TYPE, bytes)
                }
            }
            return RequestBody.create(MEDIA_TYPE, bytes!!)
        }

    }

    internal inner class ProtoResponseBodyConverter<T : Message<*, *>?>(private val classType: Class<*>) : Converter<ResponseBody, T> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T {
            return try {
                if (mConverterFactoryListener != null) {
                    return mConverterFactoryListener.decodeRetBase(classType, value.bytes()) as T
                }
                val adapter = ProtoAdapter.get(classType) as ProtoAdapter<T>
                adapter.decode(value.bytes())
            } catch (e: Exception) {
                LogUtil.e("http 解PB失败 $classType")
                throw RuntimeException("PB解析失败 , ${e.message}", e)
            } finally {
                value.close()
            }
        }
    }

    interface ConverterFactoryListener {
        @Throws(IOException::class)
        fun encodeReqBase(classType: Class<*>?, bytes: ByteArray?): ByteArray?

        @Throws(IOException::class)
        fun decodeRetBase(classType: Class<*>?, bytes: ByteArray?): Any
    }

}