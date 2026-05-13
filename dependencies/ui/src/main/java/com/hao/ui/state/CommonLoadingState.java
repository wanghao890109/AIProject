package com.hao.ui.state;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hao.ui.state.base.BaseLoadingState;
import com.hao.common.utils.UIUtils;
import com.hao.ui.state.base.BaseLoadingState;
import com.hao.ui.R;
import com.hao.ui.state.base.BaseLoadingState;


public class CommonLoadingState extends BaseLoadingState<CommonLoadingState.CommonLoadingMo> {
    public static final String STATE = BaseLoadingState.STATE;
    public static final
    String LOADING_RES = "apng/ui_loading_shaking.png";

    //    private CompatImageView image;
    private ProgressBar progressBar;
    private TextView desc;
    private LinearLayout bxLoadingContainer;
//    private APNGDrawable animationDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.state_loading_layout;
    }

    @Override
    protected void onViewCreated(View stateView) {
        //   image = stateView.findViewById(R.id.loading_image);
        progressBar = stateView.findViewById(R.id.loading_image);
        desc = stateView.findViewById(R.id.loading_desc);
        bxLoadingContainer = stateView.findViewById(R.id.container);
    }

    @Override
    public void onStateResume() {
        super.onStateResume();
//        if (animationDrawable == null) {
//            animationDrawable = APNGDrawable.fromAsset(context, LOADING_RES);
//            startAnimation();
//        } else {
//            animationDrawable.start();
//        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
//        if (animationDrawable != null) {
//            animationDrawable.stop();
//        }
    }

    @Override
    public void setViewProperty(CommonLoadingMo stateProperty) {
        super.setViewProperty(stateProperty);
        desc.setText(TextUtils.isEmpty(stateProperty.desc) ? "loading" : stateProperty.desc);
        bxLoadingContainer.setPadding(0, 0, 0, stateProperty.offset > 0 ? stateProperty.offset : 0);
    }

    @Override
    public String getState() {
        return STATE;
    }

//    private void startAnimation() {
//        if (animationDrawable.isRunning()) {
//            animationDrawable.stop();
//        }
//        image.setImageDrawable(animationDrawable);
//        animationDrawable.start();
//    }

    public static class CommonLoadingMo extends BaseLoadingMo {
        public String desc;
        public int offset;

        public CommonLoadingMo(int offset) {
            this.offset = offset;
        }

        public CommonLoadingMo(String desc, int offset) {
            this.desc = desc;
            this.offset = offset;
        }

        @Override
        public String getState() {
            return STATE;
        }
    }
}
