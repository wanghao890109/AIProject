package com.hao.ui.widget.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hao.ui.R;
import com.hao.ui.widget.BLoadingView2;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * Author wangyu1
 * Data 2019/6/11
 * Description
 **/
public class MyRefreshHead extends LinearLayout implements RefreshHeader {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private SimpleViewSwitcher mProgressBar;
    private TextView mStatusTextView;
    private TextView mHeaderTimeView;
    private BLoadingView2 mBLoadingView2;
    private ScrollListener mScrollListener;

    public void setScrollListener(ScrollListener listener) {
        mScrollListener = listener;
    }

    public interface ScrollListener {
        void onScroll(int offset);
    }

    public MyRefreshHead(Context context) {
        super(context);
        initView(context);
    }

    public MyRefreshHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public MyRefreshHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.refresh_header_layout, this);

        mArrowImageView = mContainer.findViewById(R.id.listview_header_arrow);
        mStatusTextView = mContainer.findViewById(R.id.refresh_status_textview);

        //init the progress view
        mProgressBar = mContainer.findViewById(R.id.listview_header_progressbar);
//        mProgressBar.setView(initIndicatorView(ProgressStyle.BallScale));
        mBLoadingView2 = new BLoadingView2(getContext());
        mProgressBar.setView(mBLoadingView2);
    }

/*    private View initIndicatorView(int style) {
        AVLoadingIndicatorView progressView = (AVLoadingIndicatorView) LayoutInflater.from(getContext()).inflate(R.layout.layout_indicator_view, null);
        progressView.setIndicatorId(style);
        progressView.setIndicatorColor(Color.WHITE);
        return progressView;
    }*/

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.MatchLayout;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPrimaryColors(int... colors) {
        if (colors.length > 0) {
            if (mBLoadingView2 != null) {
                mBLoadingView2.setColor(colors[0]);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

        if (mScrollListener != null) {
            mScrollListener.onScroll(offset);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

        mProgressBar.setVisibility(VISIBLE);
        if (mBLoadingView2 != null) {
            mBLoadingView2.setVisibility(VISIBLE);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        mProgressBar.setVisibility(GONE);
        if (mBLoadingView2 != null) {
            mBLoadingView2.setVisibility(GONE);
        }

        return 0;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {

    }
}
