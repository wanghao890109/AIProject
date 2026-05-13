package com.hao.ui.widget.circle;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.appcompat.widget.AppCompatImageView;
/**
 * created by wanghao on 2022-12-02
 * setOutlineProvider 支持动图显示
 */
public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOutlineProvider();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size;
        if (width == 0 || height == 0) {
            size = Math.max(width, height);
        } else {
            size = Math.min(width, height);
        }
        setMeasuredDimension(size, size);
    }

    private void setOutlineProvider() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int w = view.getWidth(), h = view.getHeight();
                    if (w == 0 || h == 0) {
                        return;
                    }
                    int left = 0, top = 0, right = w, bottom = h;

                    right = bottom = Math.min(w, h);
                    outline.setOval(left, top, right, bottom);
                }
            });
            setClipToOutline(true);
            invalidate();
        }
    }
}
