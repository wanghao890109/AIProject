package com.hao.ui.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.hao.ui.clip.ClipImageBorderView;
import com.hao.ui.clip.ClipZoomImageView;

/**
 * 剪切的长宽
 */
public class ClipImageLayout extends RelativeLayout {
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private int mHorizontalPadding = 0;// 框左右的边距，这里左右边距为0，为�?��屏幕宽度的正方形�?
    private ClipImageBorderView.ClipShap clipShap;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    public void setImageDrawable(Drawable drawable) {
        mZoomImageView.setImageDrawable(drawable);
    }

    public void setShap(ClipImageBorderView.ClipShap clipShap) {
        this.clipShap = clipShap;
        mClipImageView.setShap(clipShap);
        mZoomImageView.setShap(clipShap);
    }

    public void setImageBitmap(Bitmap bitmap) {
        Log.e("ClipImageActivity", "setImageBitmap");
        mZoomImageView.setImageBitmap(bitmap);
    }

    /**
     * 对外公布设置边距的方�?单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        if (clipShap == ClipImageBorderView.ClipShap.Rectangle) {
            return mZoomImageView.clipRectangle();
        }
        return mZoomImageView.clip();
    }
}
