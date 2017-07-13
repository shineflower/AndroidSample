package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/7/12.
 */

public class TextInputLayout extends LinearLayout {
    private TextView mTextView;
    private EditText mEditText;
    private CharSequence mHint;
    // 是否处于错误状态
    private boolean mIsStateWrong;
    // 错误时的提示信息
    private CharSequence mStateWrongMsg;

    private boolean mInDrawableStateChanged;

    //	默认hint的topMargin为10dp
    private final int DEF_HINT_TOP_MARGIN = 10;
    //	默认hint的字体颜色
    private final int DEF_HINT_COLOR = getResources().getColor(R.color.color_0971ce);
    //  默认的错误提示的颜色
    private final int DEF_WRONG_COLOR = getResources().getColor(R.color.color_ff1f1f);
    //	默认hint的字体大小为12sp
    private final int DEF_HINT_TEXT_SIZE = 12;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        /**
         * 设置整个ViewGroup的Drawable状态是否也包含子空间的drawable状态。该属性用于当子控件EdiText或者Button获得焦点时作为一个组出现使用。
         * 这样一来，将android:addStatesFromChildren设为true，
         * 当组中的EditText或是Button获取焦点时，将Layout的Background设置成相应EditText或的Button的Drawable，
         * 这样看上去该Layout中的View是一个整体
         */
        setAddStatesFromChildren(true);

        setOrientation(VERTICAL);
        LayoutParams hintParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hintParams.topMargin = DensityUtils.dp2px(context, DEF_HINT_TOP_MARGIN);
        mTextView = new TextView(context);
        mTextView.setTextColor(DEF_HINT_COLOR);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEF_HINT_TEXT_SIZE);

        addView(mTextView, hintParams);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            mEditText = (EditText) child;
            mEditText.setBackgroundResource(R.drawable.selector_edit_text_normal);
            mEditText.setPadding(0, 0, 0, 0);
            mHint = mEditText.getHint();
            mTextView.setText(mHint);
        }

        super.addView(child, index, params);
    }

    @Override
    protected void drawableStateChanged() {
        if (mInDrawableStateChanged) {
            return;
        }

        mInDrawableStateChanged = true;
        super.drawableStateChanged();

        updateLabelState();
        mInDrawableStateChanged = false;
    }

    private void updateLabelState() {
        boolean isFocused = arrayContains(getDrawableState(), android.R.attr.state_focused);

        if (isFocused) {
            if (mTextView != null && mEditText != null) {
                if (mIsStateWrong) {
                    mTextView.setText(mStateWrongMsg);
                    mTextView.setTextColor(DEF_WRONG_COLOR);
                    mEditText.setBackgroundResource(R.drawable.layer_edit_text_wrong);
                } else {
                    mTextView.setText(mHint);
                    mTextView.setTextColor(DEF_HINT_COLOR);
                    mEditText.setBackgroundResource(R.drawable.selector_edit_text_normal);
                }

                mEditText.setHint(null);
            }
        } else {
            if (mTextView != null && mEditText !=null) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    mTextView.setText(null);
                    mEditText.setHint(mHint);
                    mEditText.setBackgroundResource(R.drawable.selector_edit_text_normal);
                } else {
                    if (mIsStateWrong) {
                        mTextView.setText(mStateWrongMsg);
                        mTextView.setTextColor(DEF_WRONG_COLOR);
                        mEditText.setBackgroundResource(R.drawable.layer_edit_text_wrong);
                    } else {
                        mTextView.setText(mHint);
                        mTextView.setTextColor(DEF_HINT_COLOR);
                        mEditText.setBackgroundResource(R.drawable.selector_edit_text_normal);
                    }

                    mEditText.setHint(null);
                }

            }
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int v : array) {
            if (v == value) {
                return true;
            }
        }

        return false;
    }

    public void setStateWrong(CharSequence msg) {
        mIsStateWrong = true;
        mStateWrongMsg = msg;
        mTextView.setTextColor(DEF_WRONG_COLOR);
        mTextView.setText(mStateWrongMsg);
        mEditText.setBackgroundResource(R.drawable.layer_edit_text_wrong);
    }

    public void setStateNormal() {
        mIsStateWrong = false;
        mTextView.setTextColor(DEF_HINT_COLOR);
        mTextView.setText(mHint);
        mEditText.setBackgroundResource(R.drawable.selector_edit_text_normal);
    }
}
