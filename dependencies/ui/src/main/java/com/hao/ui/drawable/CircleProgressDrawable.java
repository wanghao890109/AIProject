package com.hao.ui.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.FloatRange;

import com.hao.common.utils.UIUtils;


/**
 * Created by yaocheng on 2017/6/27.
 */

public class CircleProgressDrawable extends Drawable {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    {
        mPaint.setAntiAlias(true);
    }
    private float curProgress;
    private RectF mRectF = new RectF();

    public CircleProgressDrawable(Context context) {
        this(context, 2, Color.RED);
    }

    public CircleProgressDrawable(Context context, int width , int color) {
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(UIUtils.dip2px(context, width));
    }

    public void snapProgress(@FloatRange(from = 0.0, to = 1.0) float progress) {
        setProgress(progress);
    }

    public float getCurProgress() {
        return curProgress;
    }

    public void setProgress(@FloatRange(from = 0.0, to = 1.0) float progress) {
        if (curProgress != progress) {
            curProgress = progress;
            invalidateSelf();
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float strokePadding = mPaint.getStrokeWidth() / 2;
        mRectF.left = bounds.left + strokePadding;
        mRectF.right = bounds.right - strokePadding;
        mRectF.top = bounds.top + strokePadding;
        mRectF.bottom = bounds.bottom - strokePadding;
    }

    private void doDraw(Canvas canvas) {

        canvas.drawArc(mRectF, 0, curProgress * 360, false, mPaint);

    }

    @Override
    public void draw(Canvas canvas) {
        doDraw(canvas);
    }


    @Override
    public void setAlpha(int i) {
        mPaint.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
