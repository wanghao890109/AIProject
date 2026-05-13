package com.hao.ui.ext

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hao.common.utils.UIUtils
import com.hao.ui.R
import com.hao.common.glide.GlideApp
import com.hao.common.glide.GlideRequest

/**
 * @author 李敬卫 2021/11/11
 *
 */
fun ImageView.load(
    url: Any?,
    @DrawableRes placeholder: Int = R.color.transparent,
    @DrawableRes error: Int = R.color.default_image,
) {
    if (!isViewDestroy(context)) {
        GlideApp.with(context).load(url).placeholder(placeholder).fallback(error).error(error).into(this)
    }
}

fun ImageView.load(
    url: Any?,
    thumbnailUrl: String,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
) {
    if (!isViewDestroy(context)) {
       val thumbnailRequest= GlideApp.with(context).load(thumbnailUrl)
        GlideApp.with(context).load(url).thumbnail(thumbnailRequest).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(placeholder).fallback(error).error(error).into(this)
    }
}


fun ImageView.load(
    url: String,
    width: Int,
    height: Int,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
) {
    if (!isViewDestroy(context)) {
        GlideApp.with(context).load(url).dontAnimate().override(width, height).placeholder(placeholder).error(error)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(this)
    }
}

/**
 * 加载圆图
 */
fun ImageView.loadCircle(
    url: Any?,
    @DrawableRes placeholder: Int = R.color.transparent,
    @DrawableRes error: Int = R.color.default_image,
) {
    if (!isViewDestroy(context)) {
        val options = RequestOptions().placeholder(placeholder).error(error).fallback(error).circleCrop()
        // .transform(CircleCrop())
        GlideApp.with(context).load(url).apply(options).into(this)
    }

}

/**
 * 加载bitmap
 */
fun ImageView.loadBitmap(
    url: Any?
) {
    if (!isViewDestroy(context)) {
        GlideApp.with(context).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                this@loadBitmap.setImageBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {

            }
        })
    }

}

/**
 * 加载圆角
 */
fun ImageView.loadRoundCorner(
    url: Any?,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
    corner: Int = 8,
) {
    if (!isViewDestroy(context)) {
        val options = RequestOptions().placeholder(placeholder).error(error).fallback(error)
            .transform(CenterCrop(), RoundedCorners(UIUtils.dip2px(context, corner.toDouble())))

        GlideApp.with(context).load(url).thumbnail(loadThumbnail(context, placeholder, corner))
            .thumbnail(loadThumbnail(context, error, corner)).apply(options).into(this)
    }
}

/**
 * 加载圆角
 */
fun ImageView.loadRoundCorner(
    url: Any?,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
    width: Int,
    height: Int,
    corner: Int = 8,
) {
    if (!isViewDestroy(context)) {
        scaleType = ImageView.ScaleType.FIT_XY
        val options = RequestOptions().placeholder(placeholder).error(error).fallback(error).override(width, height)
            .transform(CenterCrop(), RoundedCorners(UIUtils.dip2px(context, corner.toDouble())))

        GlideApp.with(context).load(url).thumbnail(loadThumbnail(context, placeholder, width, height, corner))
            .thumbnail(loadThumbnail(context, error, width, height, corner)).apply(options).into(this)
    }
}

fun ImageView.loadAvatarCircle(
    url: String?,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
) {
    if (!isViewDestroy(context)) {
        loadCircle(url, placeholder, error)
    }
}

fun ImageView.loadAvatarCircleCallback(url: String?, requestListener: RequestListener<Drawable>) {
    if (!isViewDestroy(context)) {
        val options = RequestOptions().circleCrop()
        // .transform(CircleCrop())
        GlideApp.with(context).load(url).apply(options).listener(requestListener).into(this)
    }
}

fun ImageView.loadCallback(
    url: String?,
    target: CustomTarget<Drawable>
) {
    if (!isViewDestroy(context)) {
        GlideApp.with(context).load(url).into(target)
    }
}

fun ImageView.loadAvatarRoundCorner(
    url: String?,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
    corner: Int = 8,
) {
    loadRoundCorner(url, placeholder, error, corner)
}

fun ImageView.loadAvatar(
    url: String?,
    @DrawableRes placeholder: Int = R.color.default_image,
    @DrawableRes error: Int = R.color.default_image,
) {
    load(url, placeholder = placeholder, error = error)
}


fun loadThumbnail(context: Context, placeholder: Int, corner: Int): GlideRequest<Drawable> {
    val options = RequestOptions().centerCrop().transform(CenterCrop(), RoundedCorners(UIUtils.dip2px(context, corner.toDouble())))
    return GlideApp.with(context).load(placeholder).apply(options)
}

fun loadThumbnail(
    context: Context, placeholder: Int, width: Int,
    height: Int, corner: Int,
): GlideRequest<Drawable> {
    val options = RequestOptions().centerCrop().override(width, height)
        .transform(CenterCrop(), RoundedCorners(UIUtils.dip2px(context, corner.toDouble())))
    return GlideApp.with(context).load(placeholder).apply(options)
}

fun isViewDestroy(context: Context): Boolean {
    if (context is Activity) {
//        LogUtil.e("ImageViewload Activity is")
        var activity = context as Activity
        if (activity.isDestroyed()) {
//            LogUtil.e("ImageViewload activity isDestroyed")
            return true
        }
    } else if (context is Fragment) {
//        LogUtil.e("ImageViewload Fragment is")
        var fragment = context as Fragment
        var activity = fragment.getActivity()
        if (activity?.isDestroyed == true) {
//            LogUtil.e("ImageViewload Fragment== null or getActivity isDestroyed")
            return true
        }
    }
    return false

}