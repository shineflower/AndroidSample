package com.jackie.sample.wechat_pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2019/3/14.
 */
public class WechatPayActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wechat_pay);

        findViewById(R.id.main_wechat).setOnClickListener(this);
        findViewById(R.id.main_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_wechat:
                CodeActivity.inputPassword(this, 6);
                break;
            case R.id.main_sms:
                CodeActivity.inputSmsCode(this, 6);
                break;
        }
    }
}
