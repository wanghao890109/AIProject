package com.hao.ui.widget.editor;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.hao.ui.R;

import java.lang.ref.SoftReference;


/**
 * Created by yaocheng on 2017/5/11.
 */

public class BEditText extends androidx.appcompat.widget.AppCompatEditText {


    //    private int bEms = Integer.MAX_VALUE;
    private LengthFilter mLengthFilter = null;
    private ChineseLimitTextWatcher mChineseLimitFilter;
    private boolean supportBlank = false;

    public BEditText(Context context) {
        this(context, null);
    }

    public BEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public BEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs,
                R.styleable.BEditText, defStyleAttr, defStyleAttr);

        if (a.hasValue(R.styleable.BEditText_ChineseLimit)) {
            int chineseLimit = a.getInt(R.styleable.BEditText_ChineseLimit, Integer.MAX_VALUE);
            setChineseLimit(chineseLimit);//xml中文字数设置
        } else {
            int bEms = a.getInt(R.styleable.BEditText_bems, Integer.MAX_VALUE);
            setLengthLimit(bEms);//xml英文字数设置
        }
        a.recycle();
        setSupportBlank(false);//默认不支持回车
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        checkLines();
    }

    public void setSupportBlank(boolean supportBlank) {
        this.supportBlank = supportBlank;
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) {
                    return true;
                }
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && !BEditText.this.supportBlank);
            }
        });
    }

    public void setDelClear(final String regex, boolean delClear) {

        if (delClear) {
            setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (!TextUtils.isEmpty(regex) && getText().toString().matches(regex) && (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_FORWARD_DEL)) {
                        setText("");
                    }
                    return false;
                }
            });
        } else {
            setOnKeyListener(null);
        }
    }

    private void checkLines() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setSupportBlank(getMaxLines() == 1);
        } else {
            boolean multi = (getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE)) ==
                    (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);
            setSupportBlank(!multi);
        }
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
    }

    /**
     * 支持英文字符个数
     *
     * @param ems
     */
    public void setLengthLimit(int ems) {
        if (mChineseLimitFilter != null) {
            mChineseLimitFilter.release();
            mChineseLimitFilter = null;
        }
        mLengthFilter = new LengthFilter(ems);
        setFilters(new InputFilter[]{mLengthFilter});
    }

    /**
     * 支持中文个数
     *
     * @param ems
     */
    public void setChineseLimit(int ems) {
        setChineseLimit(ems, null);
    }

    public void setChineseLimit(int ems, Runnable limit) {
        if (mLengthFilter != null) {
            super.setFilters(new InputFilter[]{});
            mLengthFilter = null;
        }
        setChineseLimitTextWatcher(this, ems, limit);
    }

    private void setChineseLimitTextWatcher(BEditText editText, int len, Runnable limit) {
        int maxBytes = len * 2;
        if (mChineseLimitFilter != null) {
            mChineseLimitFilter.release();
        }
        mChineseLimitFilter = new ChineseLimitTextWatcher(editText, maxBytes, limit);
        editText.removeTextChangedListener(mChineseLimitFilter);
        editText.addTextChangedListener(mChineseLimitFilter);
    }


    public int getLenthLimit() {
        if (mLengthFilter != null) {
            return mLengthFilter.getMax();
        }
        if (mChineseLimitFilter != null) {
            int chinesLimit = mChineseLimitFilter.getMaxBytes() / 2;
            return chinesLimit;
        }
        return Integer.MAX_VALUE;
    }

//    private int getChineseLimit() {
//        if (mChineseLimitFilter != null) {
//            int chinesLimit = mChineseLimitFilter.getMaxBytes() / 2;
//            return chinesLimit;
//        }
//        return Integer.MAX_VALUE;
//
//    }

    public int getLength() {
        if (mChineseLimitFilter != null) {
            int chinesLimit = (int) Math.ceil(mChineseLimitFilter.getNewLength(getText().toString().trim()) / 2.0f);
            return chinesLimit;
        }
        if (mLengthFilter != null) {
            return getText().length();
        }
        return 0;
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        if (mLengthFilter == null) {
            super.setFilters(filters);
            return;
        }
        if (filters == null) {
            throw new RuntimeException("filters can not be null");
        }
        InputFilter[] cur = new InputFilter[filters.length + 1];
        int size = filters.length;
        for (int i = 0; i < size; i++) {
            cur[i] = filters[i];
        }
        cur[size] = mLengthFilter;
        super.setFilters(filters);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * This filter will constrain edits not to make the length of the text
     * greater than the specified length.
     */
    public class LengthFilter implements InputFilter {
        private final int mMax;

        public LengthFilter(int max) {
            mMax = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            int keep = mMax - (dest.length() - (dend - dstart));
            if (keep <= 0) {
                return "";
            } else if (keep >= end - start) {
                return null; // keep original
            } else {
                keep += start;
                if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                    --keep;
                    if (keep == start) {
                        return "";
                    }
                }
                return source.subSequence(start, keep);
            }
        }

        /**
         * @return the maximum length enforced by this input filter
         */
        public int getMax() {
            return mMax;
        }
    }

    public static class ChineseLimitTextWatcher implements TextWatcher {

        private int mMaxBytes;
        private SoftReference<EditText> mEditTextSoftReference;
        private SoftReference<Runnable> mLimitSoftReference;
        private String before;

        public ChineseLimitTextWatcher(EditText editText, int maxBytes) {
            this(editText, maxBytes, null);
        }

        public ChineseLimitTextWatcher(EditText editText, int maxBytes, Runnable limit) {
            mEditTextSoftReference = new SoftReference<EditText>(editText);
            mLimitSoftReference = new SoftReference<Runnable>(limit);
            mMaxBytes = maxBytes;
        }

        public void release() {
            mEditTextSoftReference = null;
        }

        public int getMaxBytes() {
            return mMaxBytes;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mEditTextSoftReference != null && mEditTextSoftReference.get() != null) {

                before = mEditTextSoftReference.get().getText().toString();
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                if (this.before != null) {
                    if (mEditTextSoftReference != null && mEditTextSoftReference.get() != null) {

                        int newlength = getNewLength(mEditTextSoftReference.get().getText().toString());
                        int oldLength = getNewLength(this.before.toString());
                        if (newlength > oldLength && newlength > mMaxBytes) {
                            mEditTextSoftReference.get().removeTextChangedListener(this);
                            mEditTextSoftReference.get().setText(this.before.toString());
                            try {
                                mEditTextSoftReference.get().setSelection(mEditTextSoftReference.get().getText().length());
                                if (mLimitSoftReference != null && mLimitSoftReference.get() != null) {
                                    mLimitSoftReference.get().run();
                                }
                            } catch (Exception e) {

                            }
                            mEditTextSoftReference.get().addTextChangedListener(this);
                        } else if (oldLength > mMaxBytes) {
                            mEditTextSoftReference.get().removeTextChangedListener(this);
                            mEditTextSoftReference.get().setText(getNewString(mEditTextSoftReference.get().getText().toString(), mMaxBytes));
                            mEditTextSoftReference.get().setSelection(mEditTextSoftReference.get().getText().length());
                            mEditTextSoftReference.get().addTextChangedListener(this);
                        }

                    }

                }
            } catch (Exception e) {
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

        public int getNewLength(@Nullable String text) {
            int sumByte = 0;// text.length();

            try {
                char[] tempChars = text.toCharArray();
                int charIndex = 0;
                for (int i = 0, len = tempChars.length; i < len; i++) {
                    char itemChar = tempChars[i];

                    if ((itemChar >= 0x4e00 && itemChar <= 0x9fa5)
                            || (itemChar >= 0xf900 && itemChar <= 0xfa2d)) {
                        sumByte += 2;
                    } else {
                        String temp = new String(new char[]{itemChar});
                        sumByte += temp.getBytes().length;
                    }
                }

            } catch (Exception e) {
                sumByte = text.length();
            }
            return sumByte;
        }

        public String getNewString(@Nullable String text, int maxLen) {
            String newString = text;
            for (int i = maxLen / 2; i < text.length(); i++) {
                if (getNewLength(text.substring(0, i)) > maxLen) {
                    newString = text.substring(0, i - 1);
                    break;
                } else if (getNewLength(text.substring(0, i)) == maxLen) {
                    newString = text.substring(0, i);
                }
            }
            return newString;
        }

    }
}
