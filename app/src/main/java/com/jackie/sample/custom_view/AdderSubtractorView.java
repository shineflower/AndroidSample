package com.jackie.sample.custom_view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/6/23.
 * 数字加减器
 */

public class AdderSubtractorView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;

    private RelativeLayout mAdderLayout;
    private RelativeLayout mSubtractorLayout;
    private EditText mInputEditText;

    private int mStyle = 1;

    private int mLeastValue;
    private int mMostValue;

    private int mNumber = 0;

    private OnNumberListener mOnNumberListener;

    private final int BORDER = 1;

    private final int NO_BORDER = 2;

    public interface OnNumberListener {
        void onLeast(View view, int number); //最小
        void onMost(View view, int number);  //最大
    }

    public void setOnNumberListener(OnNumberListener onNumberListener) {
        this.mOnNumberListener = onNumberListener;
    }

    public AdderSubtractorView(Context context) {
        this(context, null);
    }

    public AdderSubtractorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdderSubtractorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
    }

    public void setStyle(int style) {
        this.mStyle = style;

        initView();
        initEvent();
    }

    public void setInitialValue(int number) {
        mInputEditText.setText(String.valueOf(number));
        mInputEditText.setSelection(mInputEditText.getText().length());
    }

    public void setLeastValue(int leastValue) {
        this.mLeastValue = leastValue;
    }

    public void setMostValue(int mostValue) {
        this.mMostValue = mostValue;
    }

    public int getInputNumber() {
        return mNumber;
    }

    private void initView() {
        View view = null;
        switch (mStyle) {
            case BORDER:
                view = View.inflate(mContext, R.layout.adder_subtractor_border_view, null);
                break;
            case NO_BORDER:
                view = View.inflate(mContext, R.layout.adder_subtractor_no_border_view, null);
                break;
        }

        mAdderLayout = (RelativeLayout) view.findViewById(R.id.rl_adder);
        mSubtractorLayout = (RelativeLayout) view.findViewById(R.id.rl_subtract);
        mInputEditText = (EditText) view.findViewById(R.id.et_input_number);

        addView(view);
    }

    private void initEvent() {
        mAdderLayout.setOnClickListener(this);
        mSubtractorLayout.setOnClickListener(this);

        mInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    if (Integer.parseInt(s.toString()) > mMostValue) {
                        mInputEditText.setText(String.valueOf(mMostValue));
                        mInputEditText.setSelection(mInputEditText.getText().length());

                        mNumber = mMostValue;

                        if (mOnNumberListener != null) {
                            mOnNumberListener.onMost(mAdderLayout, mMostValue);
                        }
                    } else if (Integer.parseInt(s.toString()) < mLeastValue){
                        mInputEditText.setText(String.valueOf(mLeastValue));
                        mInputEditText.setSelection(mInputEditText.getText().length());

                        mNumber = mLeastValue;

                        if (mOnNumberListener != null) {
                            mOnNumberListener.onLeast(mAdderLayout, mLeastValue);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String inputStr = mInputEditText.getText().toString().trim();

        int number;
        if (TextUtils.isEmpty(inputStr)) {
            number = 0;
        } else {
            number = Integer.parseInt(inputStr);
        }

        switch (v.getId()) {
            case R.id.rl_adder:
                if (number >= mMostValue) {
                    if (mOnNumberListener != null) {
                        mOnNumberListener.onMost(mAdderLayout, number);
                    }
                } else {
                    number++;

                    mInputEditText.setText(String.valueOf(number));
                    mInputEditText.setSelection(mInputEditText.getText().length());

                    mNumber = number;
                }
                break;
            case R.id.rl_subtract:
                if (number <= mLeastValue) {
                    if (mOnNumberListener != null) {
                        mOnNumberListener.onLeast(mSubtractorLayout, number);
                    }
                } else {
                    number--;

                    mInputEditText.setText(String.valueOf(number));
                    mInputEditText.setSelection(mInputEditText.getText().length());

                    mNumber = number;
                }
                break;
        }
    }
}
