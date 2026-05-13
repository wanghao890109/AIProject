package com.hao.ui.state;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.hao.ui.state.base.BaseNetErrorState;
import com.hao.ui.widget.imageview.CompatImageView;
import com.hao.common.utils.UIUtils;
import com.hao.ui.state.base.BaseNetErrorState;
import com.hao.ui.widget.imageview.CompatImageView;
import com.hao.ui.R;
import com.hao.ui.state.base.BaseNetErrorState;
import com.hao.ui.widget.imageview.CompatImageView;

public class CommonNetErrorState extends BaseNetErrorState<CommonNetErrorState.CommonNetErrorMo> {
    public static final String STATE = BaseNetErrorState.STATE;
    public static final String EVENT_CLICK = "BxNetErrorState_CLICK";
//    private static final @DrawableRes
//    int DEFAULT_RES = R.drawable.state_net_error;

    private CompatImageView image;
    private TextView title;
    private TextView desc;
    private LinearLayout netErrorContainer;

    @Override
    protected int getLayoutId() {
        return R.layout.state_error_layout;
    }

    @Override
    protected void onViewCreated(View stateView) {
        image = stateView.findViewById(R.id.net_error_image);
        title = stateView.findViewById(R.id.net_error_title);
        desc = stateView.findViewById(R.id.net_error_desc);
        netErrorContainer = stateView.findViewById(R.id.container);
//        button.setOnClickListener(view -> {
//            if (stateEventListener != null) {
//                stateEventListener.onEventListener(EVENT_CLICK, view);
//            }
//        });
    }

    @Override
    public void setViewProperty(CommonNetErrorMo stateProperty) {
        super.setViewProperty(stateProperty);
        if (stateProperty.res == 0) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(stateProperty.res);
        }
        title.setText(TextUtils.isEmpty(stateProperty.title) ? "加载失败" : stateProperty.title);
        desc.setText(TextUtils.isEmpty(stateProperty.desc) ? "请检查网络是否正常" : stateProperty.desc);
      //  button.setText(TextUtils.isEmpty(stateProperty.buttonTxt) ? "刷新" : stateProperty.buttonTxt);
        title.setVisibility(stateProperty.hideTitle ? View.GONE : View.VISIBLE);
        desc.setVisibility(stateProperty.hideDesc ? View.GONE : View.VISIBLE);
    //    button.setVisibility(stateProperty.hideButton ? View.GONE : View.VISIBLE);

        netErrorContainer.setPadding(0, 0, 0, stateProperty.offset > 0 ? stateProperty.offset : 0);
    }

    @Override
    public String getState() {
        return CommonNetErrorState.STATE;
    }

    public static class CommonNetErrorMo extends BaseNetErrorMo {
        public @DrawableRes
        int res;
        public String title;
        public String desc;
        public int offset;
        public String buttonTxt;
        public boolean hideTitle;
        public boolean hideDesc;
        public boolean hideButton;

        @Override
        public String getState() {
            return CommonNetErrorState.STATE;
        }
    }
}
