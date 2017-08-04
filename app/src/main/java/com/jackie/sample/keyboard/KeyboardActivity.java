package com.jackie.sample.keyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.VirtualKeyboardView;
import com.jackie.sample.utils.KeyboardUtils;

public class KeyboardActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener {
    private Animation mEnterAnimation;
    private Animation mExitAnimation;

    private EditText mEditText1;
    private EditText mEditText2;

    private VirtualKeyboardView mVirtualKeyboardView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        initAnimation();
        initView();
        initEvent();
    }

    private void initAnimation() {
        mEnterAnimation = AnimationUtils.loadAnimation(this, R.anim.keyboard_push_bottom_in);
        mExitAnimation = AnimationUtils.loadAnimation(this, R.anim.keyboard_push_bottom_out);
    }

    private void initView() {
        mEditText1 = (EditText) findViewById(R.id.edit_text1);
        mEditText2 = (EditText) findViewById(R.id.edit_text2);
        mVirtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtual_keyboard_view);

        KeyboardUtils.hideSystemKeyboard(this, mEditText1, mEditText2);
    }

    private void initEvent() {
        mVirtualKeyboardView.setOnConfirmListener(new VirtualKeyboardView.OnConfirmListener() {
            @Override
            public void onConfirm() {
                if (mVirtualKeyboardView.getVisibility() == View.VISIBLE) {
                    mVirtualKeyboardView.startAnimation(mExitAnimation);
                    mVirtualKeyboardView.setVisibility(View.GONE);
                }
            }
        });

        mEditText1.setOnFocusChangeListener(this);
        mEditText2.setOnFocusChangeListener(this);
        mEditText1.setOnClickListener(this);
        mEditText2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof EditText) {
            v.requestFocus();

            mVirtualKeyboardView.setFocusable(true);
            mVirtualKeyboardView.setFocusableInTouchMode(true);
            mVirtualKeyboardView.setEditText((EditText) v);

            if (mVirtualKeyboardView.getVisibility() != View.VISIBLE) {
                mVirtualKeyboardView.startAnimation(mEnterAnimation);
                mVirtualKeyboardView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v instanceof EditText && hasFocus) {

            mVirtualKeyboardView.setEditText((EditText) v);

            if (mVirtualKeyboardView.getVisibility() != View.VISIBLE) {
                mVirtualKeyboardView.startAnimation(mEnterAnimation);
                mVirtualKeyboardView.setVisibility(View.VISIBLE);
            }
        }
    }
}
