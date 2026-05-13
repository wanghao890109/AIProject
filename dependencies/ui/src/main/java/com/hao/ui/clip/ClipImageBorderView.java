package com.hao.ui.clip;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 *
 */
public class ClipImageBorderView extends View {
    /**
     * 水平方向与View的边�?
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边�?
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 边框的颜色，默认为白�?
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽�?单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;
    private ClipShap clipShap;

    public ClipImageBorderView(Context context) {
        this(context, null);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 计算矩形区域的宽�?
        mWidth = getWidth() - 2 * mHorizontalPadding;
//		// 计算距离屏幕垂直边界 的边�?
        mVerticalPadding = (getHeight() - mWidth) / 2;
//		mPaint.setColor(Color.parseColor("#aa000000"));
//		mPaint.setStyle(Style.FILL);
//		// 绘制左边1
//		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
//		// 绘制右边2
//		canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
//				getHeight(), mPaint);
//		// 绘制上边3
//		canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
//				mVerticalPadding, mPaint);
//		// 绘制下边4
//		canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
//				getWidth() - mHorizontalPadding, getHeight(), mPaint);
//		// 绘制外边�?
//		mPaint.setColor(mBorderColor);
//		mPaint.setStrokeWidth(mBorderWidth);
//		mPaint.setStyle(Style.STROKE);
//		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
//				- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
        //设置背景色
//		canvas.drawARGB(255, 139, 197, 186);
//
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
        //正常绘制黄色的圆形
        mPaint.setColor(Color.parseColor("#aa000000"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        //使用CLEAR作为PorterDuffXfermode绘制蓝色的矩形
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setColor(0xFF66AAFF);
        if (clipShap == ClipShap.Rectangle) {
            int widthImage = getWidth();
            int heightImage = (int) (widthImage * 2 / 3);
            canvas.drawRect(mHorizontalPadding, getHeight() / 2 - heightImage / 2, widthImage - mHorizontalPadding, getHeight() / 2 + heightImage / 2, mPaint);
        } else {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
        }
        //最后将画笔去除Xfermode
        mPaint.setXfermode(null);
//        mPaint.setColor(Color.WHITE);
//        mPaint.setStrokeWidth(mBorderWidth);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setAntiAlias(true);
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mHorizontalPadding, mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public void setShap(ClipShap clipShap) {
        this.clipShap = clipShap;
    }

    public enum ClipShap {
        Circle, Rectangle
    }
}
