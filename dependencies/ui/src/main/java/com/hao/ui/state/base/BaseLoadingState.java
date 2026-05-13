package com.hao.ui.state.base;

import com.hao.statemanager.state.BaseState;
import com.hao.statemanager.state.StateProperty;

/**
 * description：统一loading页基类
 */
public abstract class BaseLoadingState<T extends BaseLoadingState.BaseLoadingMo> extends BaseState<T> {
    public static final String STATE = "LoadingState";

    @Override
    public String getState() {
        return STATE;
    }


    public static class BaseLoadingMo implements StateProperty {
        @Override
        public String getState() {
            return STATE;
        }

    }
}
