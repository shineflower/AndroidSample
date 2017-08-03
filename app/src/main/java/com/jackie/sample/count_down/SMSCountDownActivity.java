package com.jackie.sample.count_down;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.CountDownTimerUtils;

/**
 * Created by Jackie on 2017/5/12.
 * 短信验证码倒计时
 */

public class SMSCountDownActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCountTimeView;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_count_down);

        mCountTimeView = (TextView) findViewById(R.id.tv_count_down);
        mCountDownTimer = new CountDownTimerUtils(mCountTimeView, 60000, 1000);
        mCountTimeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_count_down:
                mCountDownTimer.start();
                break;
        }
    }
 }
