package com.hao.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.hao.ui.R;

public class AutoExpandChild extends View {
    private int mMiniHeight = 0;

    public AutoExpandChild(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandChild, 0, 0);
            mMiniHeight = typedArray.getDimensionPixelOffset(R.styleable.ExpandChild_mini_height, 0);
            typedArray.recycle();
        }
        if (getVisibility() == VISIBLE) {
            setVisibility(INVISIBLE);
        }
    }

    public AutoExpandChild(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoExpandChild(Context context) {
        this(context, null);
    }

    public void setMiniHeight(int mMiniHeight) {
        this.mMiniHeight = mMiniHeight;
    }


    //    /**
//     * Draw nothing.
//     *
//     * @param canvas an unused parameter.
//     */
//    @Override
//    public void draw(Canvas canvas) {
//    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
//        super.draw(canvas);
    }

    /**
     * Compare to: {@link View#getDefaultSize(int, int)}
     * If mode is AT_MOST, return the child size instead of the parent size
     * (unless it is too big).
     */
    private static int getDefaultSize2(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private final static Rect sRect = new Rect();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int screenHeight = getRootView().getHeight();
        getWindowVisibleDisplayFrame(sRect);
        final int heightDifference = screenHeight - sRect.bottom;

        int measuredHeight = heightDifference - mMiniHeight;
        if (measuredHeight <= 0) {
            measuredHeight = 0;
        }
        setMeasuredDimension(
                getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec),
                measuredHeight);
//        if (UIUtils.isSoftKeyBoardShow(this)) {
//            final int screenHeight = getRootView().getHeight();
//            getWindowVisibleDisplayFrame(sRect);
//            final int heightDifference = screenHeight - sRect.bottom;
//
//            int measuredHeight = heightDifference - mMiniHeight;
//            if (measuredHeight <= 0) {
//                measuredHeight = 0;
//            }
//            setMeasuredDimension(
//                    getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec),
//                    measuredHeight);
//        } else {
//            setMeasuredDimension(
//                    getDefaultSize2(getSuggestedMinimumWidth(), widthMeasureSpec),
//                    getDefaultSize2(getSuggestedMinimumHeight(), heightMeasureSpec));
//        }

    }
}