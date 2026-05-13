package com.hao.core.net

import com.hao.common.utils.LogUtil
import com.hao.core.env.AppConfig
import com.hao.core.env.AppHttpUrls
import com.hao.service.config.ConfigService
import com.hao.service.env.EnvironmentService
import okhttp3.Cache
import okhttp3.Interceptor
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*
import kotlin.jvm.Throws

/**
 * Created by wanghao 2022/7/27
 */
object ApiService {

    private var apiInitListener: ApiInitListener? = null

    val instance: Retrofit by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        initRetrofit()
    }

    fun setApiInitListener(apiInitListener: ApiInitListener) {
        ApiService.apiInitListener = apiInitListener
    }

    /**
     * 拦截器  给所有的请求添加消息头
     */
    private val mInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/octet-stream")

            apiInitListener?.let {
                val userId = it.onGetUserId()
                val session = it.onGetSession()
//                builder.addHeader("X-Authentication-Userid", userId.toString())
//                builder.addHeader("X-Authentication-Token", session)
                // builder.addHeader("X-Authentication-DeviceId", device.deviceId)
            }
            return chain.proceed(builder.build())
        }
    }

    private fun initRetrofit(): Retrofit {
        // log拦截器  打印所有的log
        val interceptor = HttpLoggingInterceptor { message ->
            if (EnvironmentService.getInstance().isDebug) {
                LogUtil.i("okhttp $message")
            }
        }
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        //设置 请求的缓存
        val cacheFile = File(EnvironmentService.getInstance().context.getCacheDir(), "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(mInterceptor)
            .sslSocketFactory(
                createSSLSocketFactory()!!,
                TrustAllManager()
            )
            .hostnameVerifier(TrustAllHostnameVerifier())
            .cache(cache)
            .build()


        var retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(getBaseUrl())
            .addConverterFactory(ProtoConverterFactory(ProtoConverter(apiInitListener)))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit!!
    }

    private fun getBaseUrl(): String {
        val appConfig = ConfigService.getInstance().getConfigCache(AppConfig::class.java)
        if (appConfig.httpBaseUrl.isNullOrEmpty()) {
            //兜底处理，以防appConfig.httpBaseUrl在ApiServiceInit中没有设置
            val appHttpUrls = ConfigService.getInstance().getConfigCache(AppHttpUrls::class.java)
            return appHttpUrls.apiReleaseUrl
        }
        return appConfig.httpBaseUrl
    }

    private fun createSSLSocketFactory(): SSLSocketFactory? {

        var sSLSocketFactory: SSLSocketFactory? = null

        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, arrayOf<TrustManager>(TrustAllManager()), SecureRandom())
            sSLSocketFactory = sc.socketFactory
        } catch (e: Exception) {
        }

        return sSLSocketFactory
    }

    private class TrustAllManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {

        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {

        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String, session: SSLSession): Boolean {
            return true
        }
    }
}