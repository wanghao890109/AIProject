package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

@SuppressLint("SetJavaScriptEnabled")
public class RoudBridgeWebView extends BridgeWebView  {



	private int x, y;
	private int vWidth, vHeight;
	private float[] radiusArray = {30f, 30f, 30f, 30f, 30f, 30f, 30f, 30f};

	public RoudBridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoudBridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RoudBridgeWebView(Context context) {
		super(context);
	}

	public RoudBridgeWebView(Context context,  float[] radiusArray) {
		super(context);
		this.radiusArray = radiusArray;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		vWidth = getMeasuredWidth();
		vHeight = getMeasuredHeight();
	}


	@Override
	public void onDraw(Canvas canvas) {
		Log.e("jiejing", "onDraw");
		x = this.getScrollX();
		y = this.getScrollY();
		Path path = new Path();
		path.addRoundRect(new RectF(0, y, x + vWidth, y + vHeight), radiusArray, Path.Direction.CW);        // 使用半角的方式，性能比较好
		canvas.clipPath(path);
		super.onDraw(canvas);
	}
}
