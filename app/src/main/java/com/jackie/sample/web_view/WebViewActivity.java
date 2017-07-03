package com.jackie.sample.web_view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CustomWebView;
import com.jackie.sample.listener.OnActionSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/7/3.
 * WebView自定义长按选择，实现收藏 / 分享选中文本
 */

public class WebViewActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private CustomWebView mCustomWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mCustomWebView = (CustomWebView) findViewById(R.id.custom_web_view);

        List<String> actionList = new ArrayList<>();
        actionList.add("Item1");
        actionList.add("Item2");
        actionList.add("APIWeb");

        mCustomWebView.setWebViewClient(new CustomWebViewClient());

        //设置item
        mCustomWebView.setActionList(actionList);

        //链接js注入接口，使能选中返回数据
        mCustomWebView.linkJSInterface();

        mCustomWebView.getSettings().setBuiltInZoomControls(true);
        mCustomWebView.getSettings().setDisplayZoomControls(false);
        //使用JavaScript
        mCustomWebView.getSettings().setJavaScriptEnabled(true);
        mCustomWebView.getSettings().setDomStorageEnabled(true);

        mCustomWebView.setOnActionSelectListener(new OnActionSelectListener() {
            @Override
            public void onClick(String title, String value) {
                if(title.equals("APIWeb")) {
                    Intent intent = new Intent(WebViewActivity.this, WebViewAPIActivity.class);
                    startActivity(intent);
                    return;
                }

                Toast.makeText(WebViewActivity.this, value, Toast.LENGTH_LONG).show();
            }
        });

        //加载url
        mCustomWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCustomWebView.loadUrl("http://www.jianshu.com/p/b32187d6e0ad");
            }
        }, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mCustomWebView != null) {
            mCustomWebView.dismissAction();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class CustomWebViewClient extends WebViewClient {
        private boolean isLastLoadFailed = false;

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);

            if (!isLastLoadFailed) {
                CustomWebView customActionWebView = (CustomWebView) webView;
                customActionWebView.linkJSInterface();

                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            isLastLoadFailed = true;

            mProgressBar.setVisibility(View.GONE);
        }
    }
}
