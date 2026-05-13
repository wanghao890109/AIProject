package com.hao.ui.widget.grid.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.hao.ui.widget.grid.data.PageEntity;
import com.hao.ui.widget.grid.data.PageEntity;
import com.hao.ui.widget.grid.data.PageEntity;

public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
