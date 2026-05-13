package com.hao.ui.widget.grid.data;

import android.view.View;
import android.view.ViewGroup;

import com.hao.ui.widget.grid.GirdPageView;
import com.hao.ui.widget.grid.GirdPageView;
import com.hao.ui.widget.grid.GirdPageView;

import java.util.List;

public class GridPageEntity<T> extends PageEntity<GridPageEntity> {

    public enum DelBtnStatus {
        // 0,1,2
        GONE, FOLLOW, LAST;

        public boolean isShow() {
            return !GONE.toString().equals(this.toString());
        }
    }

    /**
     * 表情数据源
     */
    private List<T> mEmoticonList;
    /**
     * 每页行数
     */
    private int mLine;
    /**
     * 每页列数
     */
    private int mRow;
    /**
     * 删除按钮
     */
    private DelBtnStatus mDelBtnStatus;

    public List<T> getEmoticonList() {
        return mEmoticonList;
    }

    public void setEmoticonList(List<T> emoticonList) {
        this.mEmoticonList = emoticonList;
    }

    public int getLine() {
        return mLine;
    }

    public void setLine(int line) {
        this.mLine = line;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int row) {
        this.mRow = row;
    }

    public DelBtnStatus getDelBtnStatus() {
        return mDelBtnStatus;
    }

    public void setDelBtnStatus(DelBtnStatus delBtnStatus) {
        this.mDelBtnStatus = delBtnStatus;
    }

    public GridPageEntity() {
    }

    @Override
    public View instantiateItem(final ViewGroup container, int position, GridPageEntity pageEntity) {
        if (mPageViewInstantiateListener != null) {
            return mPageViewInstantiateListener.instantiateItem(container, position, this);
        }
        if (getRootView() == null) {
            GirdPageView pageView = new GirdPageView(container.getContext());
            pageView.setNumColumns(mRow);
            setRootView(pageView);
        }
        return getRootView();
    }
}
