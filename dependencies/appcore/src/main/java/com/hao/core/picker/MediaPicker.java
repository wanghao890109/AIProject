package com.hao.core.picker;

import android.app.Activity;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

/**
 * Created by wanghao 2023/3/27
 */
public class MediaPicker {
    public static void iconPicker(Activity activity, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(true)
                .isPreviewImage(true)
                .setImageSpanCount(3)
             //   .isShowTitle(true)
             //   .isShowBottomBar(true)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCropEngine(new CropCircularEngine())
                .forResult(call);
    }

    public static void squarePicker(Activity activity, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(true)
                .isPreviewImage(true)
             //   .isShowTitle(true)
            //    .isShowBottomBar(true)
                .setImageSpanCount(3)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .setCropEngine(new CropSquareEngine())
                .forResult(call);
    }

    public static void videoPicker(Activity activity, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofVideo())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(true)
                .isPreviewImage(true)
              //  .isShowTitle(true)
             //   .isShowBottomBar(true)
                .setImageSpanCount(3)
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(call);
    }

    public static void photoPicker(Activity activity, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(true)
                .isPreviewImage(true)
             //   .isShowTitle(true)
              //  .isShowBottomBar(true)
                .setImageSpanCount(3)
                .setCropEngine(new CropNormalEngine())
                .setCompressEngine(new ImageFileCompressEngine())
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                .forResult(call);
    }

    public static void singlePhotoPicker(Activity activity, OnResultCallbackListener<LocalMedia> call) {
        PictureSelector.create(activity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .isDisplayCamera(true)
                .isPreviewImage(true)
            //    .isShowTitle(true)
            //    .isShowBottomBar(true)
                .setImageSpanCount(3)
                .setCropEngine(new CropNormalEngine())
                //.setCompressEngine(new ImageFileCompressEngine())
                .setSelectionMode(SelectModeConfig.SINGLE)
                .forResult(call);
    }
}
