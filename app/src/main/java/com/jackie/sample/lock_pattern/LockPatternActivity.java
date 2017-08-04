package com.jackie.sample.lock_pattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.LockPatternView;

public class LockPatternActivity extends AppCompatActivity implements LockPatternView.OnPatternChangeListener {
    private TextView mLockPatternHint;
    private LockPatternView mLockPatternView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_pattern);

        mLockPatternHint = (TextView) findViewById(R.id.lock_pattern_hint);
        mLockPatternView = (LockPatternView) findViewById(R.id.lock_pattern_view);
        mLockPatternView.setOnPatternChangeListener(this);
    }

    @Override
    public void onPatternChange(String patternPassword) {
        if (patternPassword == null) {
            mLockPatternHint.setText("至少5个点");
        } else {
            mLockPatternHint.setText(patternPassword);
        }
    }

    @Override
    public void onPatternStarted(boolean isStarted) {
        if (isStarted) {
            mLockPatternHint.setText("请绘制图案");
        }
    }
}
