package com.jackie.sample.keyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jackie on 2019/3/19.
 */
public class XianYuKeyboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xianyu_keyboard);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_rondom, R.id.btn_xianyu})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rondom:
                startActivity(new Intent(this, RandomActivity.class));
                break;
            case R.id.btn_xianyu:
                startActivity(new Intent(this, XianYuActivity.class));
                break;
        }
    }
}
