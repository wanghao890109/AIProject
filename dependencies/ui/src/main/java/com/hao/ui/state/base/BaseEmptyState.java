package com.hao.ui.state.base;


import com.hao.statemanager.state.BaseState;
import com.hao.statemanager.state.StateProperty;

/**
 * description：统一空态页基类
 */
public abstract class BaseEmptyState<T extends BaseEmptyState.BaseEmptyMo> extends BaseState<T> {
    public static final String STATE = "EmptyState";

    @Override
    public String getState() {
        return STATE;
    }


    public static class BaseEmptyMo implements StateProperty {
        @Override
        public String getState() {
            return STATE;
        }

    }
}
