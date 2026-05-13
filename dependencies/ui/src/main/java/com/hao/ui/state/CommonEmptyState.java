package com.hao.ui.state;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.hao.ui.state.base.BaseEmptyState;
import com.hao.ui.widget.imageview.CompatImageView;
import com.hao.common.utils.UIUtils;
import com.hao.ui.state.base.BaseEmptyState;
import com.hao.ui.widget.imageview.CompatImageView;
import com.hao.ui.R;
import com.hao.ui.state.base.BaseEmptyState;
import com.hao.ui.widget.imageview.CompatImageView;

public class CommonEmptyState extends BaseEmptyState<CommonEmptyState.CommonEmptyMo> {
    public static final String STATE = BaseEmptyState.STATE;

//    private static final @DrawableRes
//    int DEFAULT_RES = R.drawable.state_empty;

    private CompatImageView image;
    private TextView desc;
    private LinearLayout bxEmptyContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.state_empty_layout;
    }

    @Override
    protected void onViewCreated(View stateView) {
        image = stateView.findViewById(R.id.loading_image);
        desc = stateView.findViewById(R.id.loading_desc);
        bxEmptyContainer = stateView.findViewById(R.id.container);
    }

    @Override
    public String getState() {
        return STATE;
    }

    @Override
    public void setViewProperty(CommonEmptyMo stateProperty) {
        super.setViewProperty(stateProperty);
        if (stateProperty.res == 0) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(stateProperty.res);
        }

        desc.setText(TextUtils.isEmpty(stateProperty.desc) ? desc.getResources().getString(R.string.empty) : stateProperty.desc);
        bxEmptyContainer.setPadding(0, 0, 0, stateProperty.offset > 0 ? stateProperty.offset : 0);
    }

    public static class CommonEmptyMo extends BaseEmptyState.BaseEmptyMo {
        public String desc;
        public @DrawableRes
        int res;
        public int offset;

        @Override
        public String getState() {
            return STATE;
        }

        public CommonEmptyMo() {
        }

        public CommonEmptyMo(String desc) {
            this.desc = desc;
        }

        public CommonEmptyMo(int offset) {
            this.offset = offset;
        }

        public CommonEmptyMo(String desc, int res) {
            this.desc = desc;
            this.res = res;
        }
    }
}
