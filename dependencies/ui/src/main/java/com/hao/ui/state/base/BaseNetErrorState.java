package com.hao.ui.state.base;

import com.hao.statemanager.state.BaseState;
import com.hao.statemanager.state.StateProperty;

/**
 * description：统一网络错误页基类
 */
public abstract class BaseNetErrorState<T extends BaseNetErrorState.BaseNetErrorMo> extends BaseState<T> {
    public static final String STATE = "NetErrorState";

    @Override
    public String getState() {
        return STATE;
    }

    public static class BaseNetErrorMo implements StateProperty {
        @Override
        public String getState() {
            return STATE;
        }

    }
}
