package com.jackie.sample.framework.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jackie.sample.R;

import java.util.Map;

/**
 * Created by Jackie on 2017/5/24.
 * Volley网络加载框架
 */

public class VolleyActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetButton;
    private Button mPostButton;
    private TextView mResultTextView;

    private RequestQueue mQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        mGetButton = (Button) findViewById(R.id.get);
        mPostButton = (Button) findViewById(R.id.post);
        mResultTextView = (TextView) findViewById(R.id.result);

        mGetButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);

        // 初始化请求队列
        mQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                mQueue.add(new StringRequest(Request.Method.GET, "http://www.baidu.com", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //请求成功
                        mResultTextView.setText("Status OK!");
                }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //请求失败
                        Toast.makeText(VolleyActivity.this, volleyError.getCause().toString(), Toast.LENGTH_LONG).show();
                    }
                }));
                break;
            case R.id.post:
                // post用法和put一样， get用法和delete一样
                mQueue.add(new StringRequest(Request.Method.POST, "http://www.baidu.com", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        mResultTextView.setText("Status OK!");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(VolleyActivity.this, volleyError.getCause().toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //请求参数
                        return super.getParams();
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        //请求头
                        return super.getHeaders();
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消所有请求
        mQueue.cancelAll(this);
    }
}