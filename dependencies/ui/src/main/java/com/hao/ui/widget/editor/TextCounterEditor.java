package com.hao.ui.widget.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hao.ui.R;

/**
 * Created by yaocheng on 2017/5/11.
 */

public class TextCounterEditor extends AbsContentEditor {
    private TextView mCounter;
    private BEditText mBEditText;
    private int colorWarning;
    private int colorNormal;

    public TextCounterEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mCounter = (TextView) findViewById(R.id.counter);
        mBEditText = findViewById(R.id.editor_edit);

        colorNormal = R.color.black_font_color;
        colorWarning = R.color.purple_font_color;
       // this.setBackgroundResource(R.drawable.rectangle_radius_8dp_bg_1aacacac);
        checkCount();
        showCloseView(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.text_counter_editor;
    }

    @Override
    public void setLengthLimit(int count) {
        super.setLengthLimit(count);
        checkCount();
    }

    @Override
    public void setChineseLimit(int ems) {
        super.setChineseLimit(ems);
        checkCount();
    }

    @Override
    protected void onTextChange() {
        super.onTextChange();
        checkCount();
    }

    private void checkCount() {
        if (mCounter == null) {
            return;
        }
        int length = get().getLength();
        mCounter.setText(length + "/" + getLenthLimit());
        if (length >= getLenthLimit()) {
            mCounter.setTextColor(colorWarning);
        } else {
            mCounter.setTextColor(colorNormal);
        }
    }

    public void setCounterColor(int colorNormal, int colorWarning) {
        this.colorNormal = colorNormal;
        this.colorWarning = colorWarning;
        checkCount();
    }

    public void showCounter(boolean show) {
        if (mCounter == null) {
            return;
        }
        mCounter.setVisibility(show ? VISIBLE : GONE);
    }

    public void setCounterVisity(int visity) {
        if (mCounter != null) {
            mCounter.setVisibility(visity);
        }
    }

    public BEditText getBEditText() {
        return mBEditText;
    }
}
