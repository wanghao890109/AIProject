package com.hao.ui.widget.imageview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.CheckResult;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.hao.common.glide.GlideApp;
import com.hao.common.glide.GlideRequest;
import com.hao.ui.R;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * created by wanghao on 2019-11-27
 * description：图片加载view
 */
public class CompatImageView extends AppCompatImageView {
    public static boolean isLogging = false;
    private static final String TAG = "CompatImageView";
    private static final int CROSS_FADE_TIME = 500;

    public static final int NONE = 0;
    public static final int CIRCLE = 1;
    public static final int SQUARE = 2;

    public static final int ALL = 0;
    public static final int HIDE_TOP = 1;
    public static final int HIDE_RIGHT = 2;
    public static final int HIDE_BOTTOM = 3;
    public static final int HIDE_LEFT = 4;
    public static final int ONLY_LEFT_TOP = 5;
    public static final int ONLY_RIGHT_TOP = 6;
    public static final int ONLY_RIGHT_BOTTOM = 7;
    public static final int ONLY_LEFT_BOTTOM = 8;

    @IntDef(value = {
            ALL,
            HIDE_TOP,
            HIDE_RIGHT,
            HIDE_BOTTOM,
            HIDE_LEFT,
            ONLY_LEFT_TOP,
            ONLY_RIGHT_TOP,
            ONLY_RIGHT_BOTTOM,
            ONLY_LEFT_BOTTOM,})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RoundType {
    }

    private float mIvRadius;//图片圆角
    private @RoundType
    int mRoundType;
    private float[] mRadiusArray;
    //    @Deprecated
//    private int mIvBlurRadius;//
    private int mIvCircleType;//圆形or方形
    private boolean mIvFade;//是否渐显
    private int mIvFadeTime;//渐显时间，默认CROSS_FADE_TIME
    private float mIvStrokeWidth;//变宽宽度
    private int mIvStrokeColor;//变宽颜色

    private Drawable mIvPlaceHolder;//占位图
    private Drawable mIvErrorDrawable;//加载失败图
    private int mOverrideW, mOverrideH;
    private boolean mFormat8888;

    private String mIvErrorUrl;//加载失败时加载的图片URL

    private RequestOptions requestOptions;

    private RectF mStrokeRect;
    private Paint mClipPaint;
    private Path mPath = new Path();
    private PorterDuffXfermode mMode;
    private RequestListener<Drawable> loopCountListener;

    public CompatImageView(Context context) {
        this(context, null);
    }

    public CompatImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mStrokeRect = new RectF();
        mClipPaint = new Paint();
        mClipPaint.setAntiAlias(true);
        mMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        initAttrs(attrs, context);
        mInit(context);
    }

    private void initAttrs(AttributeSet attrs, Context context) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CompatImageView);

        mIvRadius = array.getDimension(R.styleable.CompatImageView_iv_radius, 0);
        mRoundType = array.getInt(R.styleable.CompatImageView_iv_enableCorner, 0);
//        mIvBlurRadius = (int) array.getDimension(R.styleable.YppImageView_iv_blurRadius, 0);
        mIvCircleType = array.getInt(R.styleable.CompatImageView_iv_circleType, NONE);
        mIvPlaceHolder = array.getDrawable(R.styleable.CompatImageView_iv_placeHolder);
        mIvErrorDrawable = array.getDrawable(R.styleable.CompatImageView_iv_errorDrawable);
        mIvFade = array.getBoolean(R.styleable.CompatImageView_iv_fade, false);
        mIvFadeTime = array.getInt(R.styleable.CompatImageView_iv_fadeTime, CROSS_FADE_TIME);
        mIvStrokeWidth = array.getDimension(R.styleable.CompatImageView_iv_strokeWidth, 0);
        mIvStrokeColor = array.getColor(R.styleable.CompatImageView_iv_strokeColor, getResources().getColor(R.color.white));

        array.recycle();

        setOutlineProvider();
    }

    private void mInit(Context context) {
        requestOptions = new RequestOptions();
    }

    public void resetRequestOptions() {
        requestOptions = new RequestOptions();
    }

    /**
     * 开始通过glide加载图片
     */
    @SuppressLint("CheckResult")
    private void startLoadImage(GlideRequest<Drawable> glideRequest, Object model) {
        checkOptionsNotNull();

        //size
        if (mOverrideW != 0 && mOverrideH != 0) {
            requestOptions.override(mOverrideW, mOverrideH);
        }
        glideRequest.apply(requestOptions);
        //placeholder
        if (mIvPlaceHolder != null) {
            glideRequest.placeholder(mIvPlaceHolder);
        }
        //error
        if (!TextUtils.isEmpty(mIvErrorUrl)) {
            glideRequest.error(GlideApp.with(this).load(mIvErrorUrl));
        } else if (mIvErrorDrawable != null) {
            glideRequest.error(mIvErrorDrawable);
        }
        //fade
        if (mIvFade) {
            glideRequest.transition(DrawableTransitionOptions.withCrossFade(mIvFadeTime));
        }

        if (mFormat8888) {
            glideRequest.format(DecodeFormat.PREFER_ARGB_8888);
        }
        glideRequest.into(this);
    }

    @Nullable
    public void load(@Nullable String string) {
        if (activityIsDestroyed(getContext())) return;
        startLoadImage(GlideApp.with(getContext()).load(string), string);
    }

    @Nullable
    public void load(@Nullable Drawable drawable) {
        if (activityIsDestroyed(getContext())) return;
        startLoadImage(GlideApp.with(getContext()).load(drawable), drawable);
    }

    @Nullable
    public void load(@RawRes @DrawableRes @Nullable Integer id) {
        if (activityIsDestroyed(getContext())) return;
        Object model = id;
        try {
            if (id != null) {
                model = getResources().getResourceTypeName(id);
            }
        } catch (Exception e) {
            Log.i(TAG, "can not get resourceName from id");
        }
        startLoadImage(GlideApp.with(getContext()).load(id), model);
    }

    @Nullable
    public void load(@Nullable File file) {
        if (activityIsDestroyed(getContext())) return;
        startLoadImage(GlideApp.with(getContext()).load(file), file);
    }

    @Nullable
    public void load(@Nullable Uri uri) {
        if (activityIsDestroyed(getContext())) return;
        startLoadImage(GlideApp.with(getContext()).load(uri), uri);
    }

    @Nullable
    public void load(@Nullable Object o) {
        if (activityIsDestroyed(getContext())) return;
        startLoadImage(GlideApp.with(getContext()).load(o), o);
    }

    /**
     * 取消load
     */
    public void clear() {
        if (activityIsDestroyed(getContext())) return;
        GlideApp.with(getContext()).clear(this);
    }

    private boolean activityIsDestroyed(Context context) {
        if (context == null) return true;
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity) context).isDestroyed() || ((Activity) context).isFinishing();
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return false;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public <T> CompatImageView addOption(@NonNull Option<T> option, @NonNull T value) {
        checkOptionsNotNull();
        requestOptions.set(option, value);
        return this;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @CheckResult
    public CompatImageView diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        checkOptionsNotNull();
        requestOptions.diskCacheStrategy(strategy);
        return this;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @CheckResult
    public CompatImageView priority(@NonNull Priority priority) {
        checkOptionsNotNull();
        requestOptions.priority(priority);
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView transformation(@NonNull Transformation<Bitmap> transformation) {
        checkOptionsNotNull();
        requestOptions.transform(transformation);
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView transformations(@NonNull Transformation<Bitmap>... transformations) {
        checkOptionsNotNull();
        requestOptions.transforms(transformations);
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView skipMemoryCache(boolean skip) {
        checkOptionsNotNull();
        requestOptions.skipMemoryCache(skip);
        return this;
    }

    /**
     * 解码格式
     * decode(Bitmap.class) 解码为静态图展示 相当于asBitmap()
     */
    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView decode(@NonNull Class<?> resourceClass) {
        checkOptionsNotNull();
        requestOptions.decode(resourceClass);
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView centerCrop() {
        checkOptionsNotNull();
        requestOptions.centerCrop();
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView fitCenter() {
        checkOptionsNotNull();
        requestOptions.fitCenter();
        return this;
    }

    @CheckResult
    public CompatImageView setErrorUrl(String errorUrl) {
        if (!TextUtils.isEmpty(errorUrl)) {
            this.mIvErrorUrl = errorUrl;
            this.mIvErrorDrawable = null;
        }
        return this;
    }

    @CheckResult
    public CompatImageView setErrorDrawable(@Nullable Drawable error) {
        this.mIvErrorDrawable = error;
        this.mIvErrorUrl = "";
        return this;
    }

    @CheckResult
    public CompatImageView setErrorDrawable(@DrawableRes int error) {
        this.mIvErrorDrawable = AppCompatResources.getDrawable(this.getContext(), error);
        this.mIvErrorUrl = "";
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView setPlaceHolder(@Nullable Drawable placeHolder) {
        this.mIvPlaceHolder = placeHolder;
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView setPlaceHolder(@DrawableRes int placeHolder) {
        this.mIvPlaceHolder = AppCompatResources.getDrawable(this.getContext(), placeHolder);
        return this;
    }

    @SuppressLint("CheckResult")
    @CheckResult
    public CompatImageView setSize(int width, int height) {
        if (width != 0 && height != 0) {
            mOverrideW = width;
            mOverrideH = height;
        }

        return this;
    }

    @CheckResult
    public CompatImageView format8888() {
        this.mFormat8888 = true;
        return this;
    }

    private void checkOptionsNotNull() {
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
        }
    }

    private boolean isDisplayCircle() {
        return CIRCLE == mIvCircleType;
    }

    private boolean isDisplaySquare() {
        return SQUARE == mIvCircleType;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isDisplaySquare() || isDisplayCircle()) {//方形
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int size;
            if (width == 0 || height == 0) {
                size = Math.max(width, height);
            } else {
                size = Math.min(width, height);
            }
            setMeasuredDimension(size, size);
            mOverrideW = mOverrideH = size;
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @CheckResult
    public CompatImageView isSquare() {
        if (SQUARE == mIvCircleType) return this;

        this.mIvCircleType = SQUARE;
        return this;
    }

    @CheckResult
    public CompatImageView isCircle() {
        if (CIRCLE == mIvCircleType) return this;

        this.mIvCircleType = CIRCLE;
        setOutlineProvider();
        return this;
    }

    @CheckResult
    public CompatImageView setCircleType(int type) {
        if (type == mIvCircleType) return this;

        this.mIvCircleType = type;
        setOutlineProvider();
        return this;
    }

    @CheckResult
    public CompatImageView setRadius(int radius) {
        if (radius == mIvRadius) return this;

        this.mIvRadius = radius;
        setOutlineProvider();
        return this;
    }

    @CheckResult
    public CompatImageView setRadius(float radius, @RoundType int roundType) {
        //设置相同不重新裁剪圆角
        if (radius == mIvRadius && roundType == mRoundType) return this;

        mIvRadius = radius;
        mRoundType = roundType;
        setOutlineProvider();
        return this;
    }

    @CheckResult
    public CompatImageView setStrokeColor(@ColorInt int color) {
        this.mIvStrokeColor = color;
        return this;
    }

    @CheckResult
    public CompatImageView setStrokeWidth(int strokeWidth) {
        this.mIvStrokeWidth = strokeWidth;
        return this;
    }

    @CheckResult
    public CompatImageView setStroke(int strokeWidth, @ColorRes int colorId) {
        this.mIvStrokeWidth = strokeWidth;
        this.mIvStrokeColor = getResources().getColor(colorId);
        return this;
    }

    @CheckResult
    public CompatImageView setStrokeDimen(@DimenRes int resId, @ColorRes int colorId) {
        this.mIvStrokeWidth = getResources().getDimension(resId);
        this.mIvStrokeColor = getResources().getColor(colorId);

        return this;
    }

    @CheckResult
    public CompatImageView setFade(boolean mIvFade) {
        this.mIvFade = mIvFade;
        return this;
    }

    @CheckResult
    public CompatImageView setFadeTime(int mIvFadeTime) {
        this.mIvFadeTime = mIvFadeTime;
        return this;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (needDrawStroke()) {
            int width = canvas.getWidth(), height = canvas.getHeight();
            mStrokeRect.set(mIvStrokeWidth, mIvStrokeWidth,
                    width - mIvStrokeWidth, height - mIvStrokeWidth);

            //saveLayer消耗性能
            int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
            canvas.drawColor(mIvStrokeColor);//不直接drawStroke，因为圆角时会有间隙
            mClipPaint.setColor(mIvStrokeColor);
            mClipPaint.setStyle(Paint.Style.FILL);
            mClipPaint.setXfermode(mMode);

            drawStroke(canvas, width, height, mIvStrokeWidth);

            mClipPaint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }
    }

    private void drawStroke(Canvas canvas, int width, int height, float halfStrokeWith) {
        if (isDisplayCircle()) {
            float radius = Math.min(width, height);
            canvas.drawCircle(radius / 2, radius / 2, radius / 2 - halfStrokeWith, mClipPaint);
        } else {
            if (mRadiusArray != null) {
                drawRoundRect(canvas, mStrokeRect, mRadiusArray, mClipPaint);
            } else if (mIvRadius <= 0) {
                canvas.drawRect(mStrokeRect, mClipPaint);
            } else {
                canvas.drawRoundRect(mStrokeRect, mIvRadius, mIvRadius, mClipPaint);
            }
        }
    }

    private void drawRoundRect(Canvas canvas, RectF rect, float[] radiusArray, Paint paint) {
        mPath.reset();
        mPath.addRoundRect(rect, radiusArray, Path.Direction.CW);
        canvas.drawPath(mPath, paint);
    }

    private void setOutlineProvider() {
        if (!needOutLine()) {//圆形或有圆角
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int w = view.getWidth(), h = view.getHeight();
                    if (w == 0 || h == 0) {
                        return;
                    }
                    int left = 0, top = 0, right = w, bottom = h;

                    if (isDisplayCircle()) {//圆形
                        right = bottom = Math.min(w, h);
                        outline.setOval(left, top, right, bottom);
                    } else {
                        if (mRoundType == ALL) {
                            mRadiusArray = new float[]{mIvRadius, mIvRadius, mIvRadius, mIvRadius, mIvRadius, mIvRadius, mIvRadius, mIvRadius};
                        } else if (mRoundType == HIDE_TOP) {////2个圆角
                            top -= mIvRadius;
                            mRadiusArray = new float[]{0, 0, 0, 0, mIvRadius, mIvRadius, mIvRadius, mIvRadius};
                        } else if (mRoundType == HIDE_RIGHT) {
                            right += mIvRadius;
                            mRadiusArray = new float[]{mIvRadius, mIvRadius, 0, 0, 0, 0, mIvRadius, mIvRadius};
                        } else if (mRoundType == HIDE_BOTTOM) {
                            bottom += mIvRadius;
                            mRadiusArray = new float[]{mIvRadius, mIvRadius, mIvRadius, mIvRadius, 0, 0, 0, 0};
                        } else if (mRoundType == HIDE_LEFT) {
                            left -= mIvRadius;
                            mRadiusArray = new float[]{0, 0, mIvRadius, mIvRadius, mIvRadius, mIvRadius, 0, 0};
                        } else if (mRoundType == ONLY_LEFT_TOP) {// //1个圆角
                            right += mIvRadius;
                            bottom += mIvRadius;
                            mRadiusArray = new float[]{mIvRadius, mIvRadius, 0, 0, 0, 0, 0, 0};
                        } else if (mRoundType == ONLY_RIGHT_TOP) {
                            left -= mIvRadius;
                            bottom += mIvRadius;
                            mRadiusArray = new float[]{0, 0, mIvRadius, mIvRadius, 0, 0, 0, 0};
                        } else if (mRoundType == ONLY_RIGHT_BOTTOM) {
                            left -= mIvRadius;
                            top -= mIvRadius;
                            mRadiusArray = new float[]{0, 0, 0, 0, mIvRadius, mIvRadius, 0, 0};
                        } else if (mRoundType == ONLY_LEFT_BOTTOM) {
                            right += mIvRadius;
                            top -= mIvRadius;
                            mRadiusArray = new float[]{0, 0, 0, 0, 0, 0, mIvRadius, mIvRadius};
                        }

                        outline.setRoundRect(left, top,
                                right, bottom, mIvRadius);
                    }
                }
            });
            setClipToOutline(true);
            invalidate();
        }
    }

    private boolean needOutLine() {
        return mIvRadius > 0 || isDisplayCircle();
    }

    private boolean needDrawStroke() {
        return mIvStrokeWidth > 0;
    }
}
