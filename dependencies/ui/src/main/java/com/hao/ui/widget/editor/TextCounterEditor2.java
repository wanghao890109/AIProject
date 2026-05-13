package com.hao.ui.widget.editor;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.ui.R;

public class TextCounterEditor2 extends TextCounterEditor{

    public TextCounterEditor2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.text_counter_editor2;
    }
}
