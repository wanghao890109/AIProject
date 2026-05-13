package com.hao.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.hao.ui.utils.UIShapeBuilder;
import com.hao.common.utils.UIUtils;
import com.hao.ui.R;


public class UnreadTextView extends AppCompatTextView {

    private long mFirstUpdateTime;
    private int mCurrentUnreadMsgCount;
    private static final long DELAY_MIN_TIME = 10L;
    private static final long DELAY_MAX_TIME = 2000L;
    private static final int MAX_COUNT = 99;
    private Runnable mUpdateRunnable = this::setUnreadText;
    private int circleBackgroundColor;
    private float circleWidth;
    private float circleRadius;
    private float circlePaddingHorizontal;

    public UnreadTextView(Context context) {
        this(context, null);
    }

    public UnreadTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnreadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleDotView);
        circleBackgroundColor = typedArray.getColor(R.styleable.CircleDotView_circleBackground, ContextCompat.getColor(context, R.color.red_font_color_ff2f67));
        circleWidth = typedArray.getDimension(R.styleable.CircleDotView_circleWidth, UIUtils.dip2px(context, 16));
        circleRadius = typedArray.getDimension(R.styleable.CircleDotView_circleRadius, UIUtils.dip2px(context, 10));
        circlePaddingHorizontal = typedArray.getDimension(R.styleable.CircleDotView_circlePaddingHorizontal, UIUtils.dip2px(context, 4));
        typedArray.recycle();
        // LogUtil.i("UnreadTextView circleWidth=" + circleWidth + ",circleRadius=" + circleRadius);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        setGravity(Gravity.CENTER);
        setMinHeight((int) (circleWidth + 0.5));
        setMinWidth((int) (circleWidth + 0.5));
        setPadding((int) (circlePaddingHorizontal + 0.5), 0, (int) (circlePaddingHorizontal + 0.5), 0);
        setBackground(new UIShapeBuilder().setCornerRadius(UIShapeBuilder.ANGLE_ALL, circleRadius).setColor(circleBackgroundColor).build());
        getPaint().setFakeBoldText(true);
        setIncludeFontPadding(false);
//        try {
//            Typeface typeface = TtfUtils.getInstance().getFuturaFont(context);
//            if (typeface != null) {
//                setTypeface(typeface);
//            }
//        } catch (Resources.NotFoundException ignored) {
//        }
        setVisibility(GONE);
    }

    public void updateUnreadCount(int unreadCount) {
        updateUnreadCount(unreadCount, true);
    }


    /**
     * @param unreadCount
     * @param immediate   即时更新
     */
    public void updateUnreadCount(int unreadCount, boolean immediate) {
        if (unreadCount == mCurrentUnreadMsgCount) {
            return;
        }
        mCurrentUnreadMsgCount = unreadCount;
        if (mFirstUpdateTime == 0L) {
            mFirstUpdateTime = System.currentTimeMillis();
        }
        removeCallbacks(mUpdateRunnable);
        if (immediate) {
            post(mUpdateRunnable);
            return;
        }
        long delayTime = System.currentTimeMillis() - mFirstUpdateTime;
        if (delayTime > DELAY_MIN_TIME && delayTime < DELAY_MAX_TIME) {
            postDelayed(mUpdateRunnable, delayTime);
        } else {
            post(mUpdateRunnable);
        }
    }


    @SuppressLint("SetTextI18n")
    private void setUnreadText() {
        if (mCurrentUnreadMsgCount > 0) {
            setVisibility(View.VISIBLE);
            if (mCurrentUnreadMsgCount <= MAX_COUNT) {
                setText(String.valueOf(mCurrentUnreadMsgCount));
            } else {
                setText(MAX_COUNT + "+");
            }
        } else {
            setVisibility(View.GONE);
            setText("");
        }
    }
}
