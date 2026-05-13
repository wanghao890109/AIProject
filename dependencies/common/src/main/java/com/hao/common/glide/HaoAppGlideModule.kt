package com.hao.common.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.hao.common.utils.LogUtil
import com.hao.common.glide.progress.ProgressInterceptor
import okhttp3.OkHttpClient
import java.io.InputStream


@GlideModule
open class HaoAppGlideModule : AppGlideModule() {

    val DISK_CACHE_DIR = "image_disk_cache"

    /** 500 MB of cache. */
    val DISK_CACHE_SIZE = (500 * 1024 * 1024).toLong()

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        LogUtil.i("MyAppGlideModule, applyOptions")
        val diskCacheFactory = ExternalPreferredCacheDiskCacheFactory(
            context,
            DISK_CACHE_DIR,
            DISK_CACHE_SIZE
        )
        builder.setDiskCache(diskCacheFactory)
    }

    override fun isManifestParsingEnabled(): Boolean {
        return super.isManifestParsingEnabled()
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(ProgressInterceptor())
            .build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }

}