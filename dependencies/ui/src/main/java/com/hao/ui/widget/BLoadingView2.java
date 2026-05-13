package com.hao.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;


/**
 * Author wangyu1
 * Data 2019/4/12
 * Description
 **/
public class BLoadingView2 extends View {
    private Paint mBitmapPaint;
    private float currentR = 20;
    private float circleX = 300;
    private float circleY = 300;
    private float maxR = 50;
    private int alpha = 200;
    private boolean start = false;
    public ValueAnimator mValueAnimator;

    public BLoadingView2(Context context) {
        this(context, null);
    }

    public BLoadingView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BLoadingView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    // 初始化画笔paint
    private void initPaint() {

        mBitmapPaint = new Paint();
        // 防抖动
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setColor(Color.BLACK);
//        mBitmapPaint.setStyle(Paint.Style.STROKE);
//        mBitmapPaint.setStrokeWidth(10);
    }

    public void setColor(int color) {
        if (mBitmapPaint != null) {
            mBitmapPaint.setColor(color);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!start) {
            startAnimator();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentR > maxR) {
            currentR = 20;
        }

        mBitmapPaint.setAlpha(alpha);
//        circleX = UIUtils.getScreenWidth(getContext()) / 2 - UIUtils.dip2px(getContext(), 20);
        circleX = getWidth() / 2;
        circleY = getHeight() / 2;
        canvas.drawCircle(circleX, circleY, currentR, mBitmapPaint);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimator();
        } else {
            releaseAnimator();
            invalidate();
        }
    }

    private void releaseAnimator() {
        alpha = 200;
        currentR = 20;
        if (mValueAnimator != null) {
            mValueAnimator.end();
            mValueAnimator.cancel();
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.removeAllListeners();
            mValueAnimator = null;
        }
    }

    private void startAnimator() {
        releaseAnimator();
        mValueAnimator = ValueAnimator.ofInt(200, 0);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x = (int) animation.getAnimatedValue();
                alpha = x;
                currentR = 20 + 30 * (200 - x) / 200;
                postInvalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                start = false;
                postInvalidate();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                start = true;
            }
        });
        mValueAnimator.start();
    }

}
