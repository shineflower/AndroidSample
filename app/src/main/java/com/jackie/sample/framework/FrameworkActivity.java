package com.jackie.sample.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.framework.fresco.FrescoActivity;
import com.jackie.sample.framework.nohttp.NohttpActivity;
import com.jackie.sample.framework.okhttp.OkhttpActivity;
import com.jackie.sample.framework.okhttp3.Okhttp3Activity;
import com.jackie.sample.framework.retrofit.RetrofitActivity;
import com.jackie.sample.framework.volley.VolleyActivity;

/**
 * Created by Jackie on 2017/5/24.
 */

public class FrameworkActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mVolleyButton;
    private Button mOkhttpButton;
    private Button mOkhttp3Button;
    private Button mFrescoButton;
    private Button mRetrofitButton;
    private Button mNohttpButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_framework);

        mVolleyButton = (Button) findViewById(R.id.volley);
        mOkhttpButton = (Button) findViewById(R.id.okhttp);
        mOkhttp3Button = (Button) findViewById(R.id.okhttp3);
        mFrescoButton = (Button) findViewById(R.id.fresco);
        mRetrofitButton = (Button) findViewById(R.id.retrofit);
        mNohttpButton = (Button) findViewById(R.id.nohttp);

        mVolleyButton.setOnClickListener(this);
        mOkhttpButton.setOnClickListener(this);
        mOkhttp3Button.setOnClickListener(this);
        mFrescoButton.setOnClickListener(this);
        mRetrofitButton.setOnClickListener(this);
        mNohttpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.volley:
                intent = new Intent(this, VolleyActivity.class);
                break;
            case R.id.okhttp:
                intent = new Intent(this, OkhttpActivity.class);
                break;
            case R.id.okhttp3:
                intent = new Intent(this, Okhttp3Activity.class);
                break;
            case R.id.fresco:
                intent = new Intent(this, FrescoActivity.class);
                break;
            case R.id.retrofit:
                intent = new Intent(this, RetrofitActivity.class);
                break;
            case R.id.nohttp:
                intent = new Intent(this, NohttpActivity.class);
                break;
        }

        startActivity(intent);
    }
}
