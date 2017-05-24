package com.jackie.sample.framework.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jackie.sample.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;

/**
 * Created by Jackie on 2017/5/24.
 */

public class OkhttpActivity extends AppCompatActivity implements View.OnClickListener {
    //https://github.com/hongyangAndroid/okhttputils
    private Button mGetButton;
    private Button mPostButton;
    private Button mDownloadButton;
    private Button mUploadButton;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        mGetButton = (Button) findViewById(R.id.get);
        mPostButton = (Button) findViewById(R.id.post);
        mDownloadButton = (Button) findViewById(R.id.download);
        mUploadButton = (Button) findViewById(R.id.upload);
        mResultTextView = (TextView) findViewById(R.id.result);

        mGetButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mDownloadButton.setOnClickListener(this);
        mUploadButton.setOnClickListener(this);

        OkHttpUtils.getInstance().setConnectTimeout(10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                // Get请求http和https都支持
//                String getUrl = "http://develop.toomao.com:9000/1.1/activities";
                String getUrl = "https://api.toomao.com/1.1/activities";
                OkHttpUtils.get().url(getUrl)
//                        .addHeader("", "")  // header和params可以添加多个，也可以不添加
//                        .addParams("", "")
                        .build().execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e) {
                        mResultTextView.setText(e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        mResultTextView.setText(response);
                    }
                });
                break;
            case R.id.post:
//                String postUrl = "http://develop.toomao.com:9000/1.1/activities/569dfbf434439b1447620d92/addcomments";
                String postUrl = "https://api.toomao.com/1.1/activities/56a598013ef3c5501c0f4131/addcomments";

                // 方法1：上传键值对参数
//                OkHttpUtils.post().url(postUrl)
//                        .addHeader("X-AVOSCloud-Session-Token", "51e5e16b3be745308a3a3cf7356623db")
//                        .addHeader("User-Agent", " toomao/2.4.2 NetType/wifi")
//                        .addParams("content", "ssssssdddddd")
//                        .build().execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        mResultTextView.setText(e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//                        mResultTextView.setText(response);
//                    }
//                });

                HashMap<String, String> map = new HashMap<>();
                map.put("content", "很好");
                Gson gson = new Gson();
                String json = gson.toJson(map);
                // 方法2： 上传字符串参数
                OkHttpUtils.postString().url(postUrl)
//                        .addHeader("X-AVOSCloud-Session-Token", "51e5e16b3be745308a3a3cf7356623db") // http用这个
                        .addHeader("X-AVOSCloud-Session-Token", "55a47a5d0cf2a07ccfa49d0a")  // https用这个
                        .addHeader("User-Agent", " toomao/2.4.2 NetType/wifi")
                        .content(json)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                mResultTextView.setText(e.toString());
                            }

                            @Override
                            public void onResponse(String response) {
                                mResultTextView.setText(response);
                            }
                        });

                // 方式三： 同步请求， 这样要自己起线程
//                new Thread() {
//                    public void run() {
//                        try {
//                            String url = "https://api.toomao.com/1.1/activities/56a598013ef3c5501c0f4131/addcomments";
//                            HashMap<String, String> map = new HashMap<>();
//                            map.put("content", "很不错");
//                            Gson gson = new Gson();
//                            String json = gson.toJson(map);
//                            OkHttpClient client = new OkHttpClient();
//                            Request request = new Request.Builder().url(url)
//                                    .post(RequestBody.create(MediaType.parse("text/x-markdown; charset=utf-8"), json))
//                                    .addHeader("X-AVOSCloud-Session-Token", "55a47a5d0cf2a07ccfa49d0a")
//                                    .addHeader("User-Agent", " toomao/2.4.2 NetType/wifi")
//                                    .build();
//
//                            final Response response = client.newCall(request).execute();
//                            if (response.isSuccessful()) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mResultTextView.setText(response.toString());
//                                    }
//                                });
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mResultTextView.setText(response.toString());
//                                    }
//                                });
//                            }
//                        } catch (Exception e) {
//                            mResultTextView.setText("走到Exception了：" + e.toString());
//                        }
//                    }
//                }.start();
                break;
            case R.id.download:
                // 图片下载
                String imgUrl = "http://pic.toomao.com/bd90075f0e05997ac305d0e358125be344b64a47";
                OkHttpUtils.get().url(imgUrl)
                        .build()
                        .execute(new FileCallBack("/mnt/sdcard/", "a.png") {
                            @Override
                            public void inProgress(float progress) {
                                // progress是从0-1，因此给他乘以100
                                mResultTextView.setText((progress * 100) + "");
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                mResultTextView.setText(e.toString());
                            }

                            @Override
                            public void onResponse(File response) {
                                // 这个方法返回已经保存到本地的文件对象
                                mResultTextView.setText(response.getAbsolutePath());
                            }
                        });
                break;
            case R.id.upload:
                // 上传文件, 不知道为什么用OkHttpUtils.postFile()那种方式没有成功
                File file = new File("/mnt/sdcard/header.png");
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-AVOSCloud-Session-Token", "55a47a5d0cf2a07ccfa49d0a");
                headers.put("User-Agent", " toomao/2.4.2 NetType/wifi");
                OkHttpUtils.post()
                        .addFile("mFile", "header.png", file)
                        .url("https://api.toomao.com/1.1/images")
//                        .params(new HashMap<String, String>()) // 可以添加键值对参数
                        .headers(headers)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        mResultTextView.setText("onError:" + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        mResultTextView.setText("response:" + response);
                    }
                });
                break;
        }
    }
}
