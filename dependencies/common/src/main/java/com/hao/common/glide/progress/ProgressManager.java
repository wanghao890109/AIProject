package com.hao.common.glide.progress;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hao.common.utils.LogUtil;
import com.hao.common.glide.GlideApp;
import com.hao.common.glide.GlideRequest;

import java.io.File;

public class ProgressManager {

    private ProgressManager() {
    }

    private static volatile ProgressManager instance = null;

    public static ProgressManager getInstance() {
        if (instance == null) {
            synchronized (ProgressManager.class) {
                if (instance == null) {
                    instance = new ProgressManager();
                }
            }
        }
        return instance;
    }

    private CustomTarget customerTarget;

    public void loadProgress(Context mContext, ImageView ivView, final String url, ProgressListener progressListener) {
        ProgressInterceptor.addListener(url, progressListener);
        GlideRequest<File> glideRequest = GlideApp.with(mContext).downloadOnly().load(url);
        customerTarget = new CustomTarget<File>() {
            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                if (ProgressInterceptor.getListener(url) != null) {
                    ProgressInterceptor.getListener(url).onLoadResult(false, null);
                }
                ProgressInterceptor.removeListener(url);
            }

            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                if (ProgressInterceptor.getListener(url) != null) {
                    ProgressInterceptor.getListener(url).onLoadResult(true, resource);
                    ivView.setImageBitmap(BitmapFactory.decodeFile(resource.getAbsolutePath()));
                }

                LogUtil.w("download ->PreviewImageViewHolder onResourceReady->>>>>>>>>" + resource.getAbsolutePath());
                ProgressInterceptor.removeListener(url);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                LogUtil.w("download ->PreviewImageViewHolder onLoadCleared->>>>>>>>>");
            }
        };

        glideRequest.into(customerTarget);
    }


    public void cancel(Context mContext) {
        GlideApp.with(mContext).clear(customerTarget);
    }
}
