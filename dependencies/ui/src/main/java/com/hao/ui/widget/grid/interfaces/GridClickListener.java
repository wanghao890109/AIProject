package com.hao.ui.widget.grid.interfaces;

public interface GridClickListener<T> {

    void onClick(T t, int actionType, boolean isDelBtn);
}
