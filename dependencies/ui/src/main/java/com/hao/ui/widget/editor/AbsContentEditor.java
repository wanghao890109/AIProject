package com.hao.ui.widget.editor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.hao.common.utils.LogUtil;
import com.hao.ui.R;

/**
 * Created by yaocheng on 2017/5/11.
 */

public abstract class AbsContentEditor extends FrameLayout {
    public interface CallBack {
        void onContextClear(boolean empty);
    }

    protected abstract int getLayoutId();

    protected void onTextChange() {

    }

    public BEditText mBEditText = null;
    private View mCloseView = null;
    private CallBack mCallBack = null;
    private boolean bEmpty = true;
    private boolean bandInputMethed = false;
    private long inputStartTime = 0L;
    private long inputEndTime = 0L;
    private boolean userInput = true;
    private boolean showCloseView = false;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (inputStartTime == 0) {
                inputStartTime = System.currentTimeMillis();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            onTextChange();
            checkTextEmpty();
            inputEndTime = System.currentTimeMillis();
            userInput = true;
        }
    };
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            try {
                mBEditText.setFocusable(true);
                mBEditText.setFocusableInTouchMode(true);
                mBEditText.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mBEditText, 0);
            } catch (Throwable e) {

            }
            return false;
        }
    });

    public long getInputduration() {
        return inputEndTime - inputStartTime;
    }

    public void setHint(@StringRes int res) {
        mBEditText.setHint(res);
    }

    public void setHint(CharSequence text) {
        mBEditText.setHint(text);
    }

    /**
     * 支持中文个数
     *
     * @param ems
     */
    public void setChineseLimit(int ems) {
        mBEditText.setChineseLimit(ems);
        checkClose();
    }

    /**
     * 支持英文字符个数
     *
     * @param count
     */
    public void setLengthLimit(int count) {
        mBEditText.setLengthLimit(count);
        checkClose();
    }

    public int getLenthLimit() {
        return mBEditText.getLenthLimit();
    }

    public int getLength() {
        return mBEditText.getLength();
    }

    public void setLines(int lines) {
        mBEditText.setLines(lines);
        checkClose();
    }

    public void setMaxLines(int maxLines) {
        mBEditText.setMaxLines(maxLines);
        checkClose();
    }

    public void setMinLines(int lines) {
        mBEditText.setMinLines(lines);
        checkClose();
    }

    public AbsContentEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(getLayoutId(), this);
        mBEditText = (BEditText) findViewById(R.id.editor_edit);
        mCloseView = findViewById(R.id.close);
        if (mCloseView != null) {
            mCloseView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBEditText.setText("");
                }
            });
        }
        checkTextEmpty();
        checkClose();
    }

    public void setInputType(int type) {
        mBEditText.setInputType(type);
        try {
            mBEditText.setSelection(mBEditText.getText().length());
        } catch (Exception e) {

        }
    }

    private void checkClose() {
        if (mCloseView != null && showCloseView) {
            String text = mBEditText.getText().toString();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                if (mBEditText.getMaxLines() > 1 && !TextUtils.isEmpty(text)) {
                    mCloseView.setVisibility(View.VISIBLE);
                } else {
                    mCloseView.setVisibility(View.GONE);
                }
            } else {
                mCloseView.setVisibility(View.GONE);
            }
        }
    }

    public void showCloseView(boolean showCloseView) {
        this.showCloseView = showCloseView;
    }

    public void bindInputMethed(boolean showAfterAttached) {
        bandInputMethed = showAfterAttached;
    }

    public void setEditsetGravity(int gravity) {
        mBEditText.setGravity(gravity);
    }

    public void setText(CharSequence text) {

        mBEditText.setText(text);
        try {
            mBEditText.setSelection(mBEditText.getText().length());
        } catch (Exception e) {
            LogUtil.d("setText e:" + e.getMessage());
        }

//
        onTextChange();

        checkTextEmpty();
        if (TextUtils.isEmpty(text)) {
            userInput = true;
        } else {
            userInput = false;
        }
    }

    public View getCloseView() {
        return mCloseView;
    }

    public boolean isUserInput() {
        return userInput;
    }

    public BEditText get() {
        return mBEditText;
    }


    public boolean isEmpty() {
        return bEmpty;
    }


    public void setCallBack(CallBack l) {
        mCallBack = l;
    }

    public String getText() {
        return mBEditText.getText().toString();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mBEditText != null) {
            mBEditText.addTextChangedListener(mTextWatcher);
        }
        if (bandInputMethed && !mHandler.hasMessages(0)) {
            mHandler.sendEmptyMessageDelayed(0, 300);
        }
    }

    public void showInput(int delay) {
        if (!mHandler.hasMessages(0)) {
            mHandler.sendEmptyMessageDelayed(0, delay);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mBEditText != null) {
            mBEditText.removeTextChangedListener(mTextWatcher);
        }
        mHandler.removeMessages(0);
    }

    private void checkTextEmpty() {

        if (mBEditText != null) {
            String text = mBEditText.getText().toString();

            if (!TextUtils.isEmpty(text)) {
                if (mCloseView != null) {
                    if (showCloseView) {
                        mCloseView.setVisibility(View.VISIBLE);
                    } else {
                        mCloseView.setVisibility(View.GONE);
                    }
                }
                if (bEmpty) {
                    bEmpty = false;
                    if (mCallBack != null) {
                        mCallBack.onContextClear(bEmpty);
                    }
                }
            } else {
                if (mCloseView != null) {
                    if (showCloseView) {
                        mCloseView.setVisibility(View.INVISIBLE);
                    } else {
                        mCloseView.setVisibility(View.GONE);
                    }
                }
                if (!bEmpty) {
                    bEmpty = true;
                    if (mCallBack != null) {
                        mCallBack.onContextClear(bEmpty);
                    }
                }
            }
        }
    }


    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
            if (source.equals(" ")) {
                return "";
            } else {
                return null;
            }
        }
    };

}