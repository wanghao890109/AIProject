package com.hao.ui.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by yaocheng on 2017/5/10.
 */

public class ProgressDrawable extends Drawable {
    public Drawable mDrawableNormal;
    public Drawable mDrawablePress;

    public ProgressDrawable(Drawable dNor, Drawable dPre) {
        mDrawableNormal = dNor;
        mDrawablePress = dPre;
    }

    public void setProgress(float values) {
        int alpha = (int) (values * 255);
        mDrawableNormal.setAlpha(alpha);
        mDrawablePress.setAlpha(255 - alpha);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        mDrawableNormal.draw(canvas);
        mDrawablePress.draw(canvas);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mDrawableNormal.setBounds(bounds);
        mDrawablePress.setBounds(bounds);
    }

    @Override
    public int getIntrinsicHeight() {
        Drawable d = mDrawableNormal == null ? mDrawablePress : mDrawableNormal;
        if (d != null) {
            return d.getIntrinsicHeight();
        }
        return super.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        Drawable d = mDrawableNormal == null ? mDrawablePress : mDrawableNormal;
        if (d != null) {
            return d.getIntrinsicWidth();
        }
        return super.getIntrinsicWidth();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

}

