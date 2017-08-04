package com.jackie.sample.alipay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.PasswordView;

public class AliPayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mResetButton;
    private PasswordView mPasswordView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mResetButton = (Button) findViewById(R.id.reset);
        mPasswordView = (PasswordView) findViewById(R.id.password_view);
        mResetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                mPasswordView.reset();
                break;
        }
    }
}
