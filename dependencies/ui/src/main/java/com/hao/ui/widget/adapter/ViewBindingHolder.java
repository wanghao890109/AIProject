package com.hao.ui.widget.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


public  class ViewBindingHolder<DB extends ViewBinding> extends RecyclerView.ViewHolder {

    private final DB binding;

    public ViewBindingHolder(DB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @NonNull
    public DB getBinding() {
        return binding;
    }


}
