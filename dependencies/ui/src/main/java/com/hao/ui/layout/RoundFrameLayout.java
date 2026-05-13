package com.hao.ui.layout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.ui.R;


/**
 * Created by wanghao2 on 2019/1/8.
 */

public class RoundFrameLayout extends FrameLayout {

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private final float DEFAULT_RADIUS = 10.0f;
    float radius = DEFAULT_RADIUS;
    float aspectRatio = 0;
    public RoundFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context, attrs);
    }

    public RoundFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context, attrs);
    }

    private void initLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs != null) {
            Resources.Theme theme = context.getTheme();
            TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout, 0, 0);
            radius = typedArray.getDimension(R.styleable.RoundFrameLayout_radius, DEFAULT_RADIUS);
            aspectRatio = typedArray.getFloat(R.styleable.RoundFrameLayout_aspectRatio, 0);
            typedArray.recycle();
        }
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRatio > 0f) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / aspectRatio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        path.addRoundRect(new RectF(0, 0, w, h), radius, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }

}
