package com.hao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 用于监听layout变化
 * @author wanghao2
 *
 */
public class ResizeRelativeLayout extends RelativeLayout
{

    public ResizeRelativeLayout(Context context)
    {
        super(context);
    }

    public ResizeRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ResizeRelativeLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        if (null != listener)
        {
            listener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    private SizeChangedListener listener;

    public void setOnSizeChangedListener(SizeChangedListener listener)
    {
        this.listener = listener;
    }

    public interface SizeChangedListener
    {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
