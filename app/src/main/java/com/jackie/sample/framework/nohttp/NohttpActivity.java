package com.jackie.sample.framework.nohttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ThreadUtils;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadQueue;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

/**
 * Created by Jackie on 2017/5/24.
 */

public class NohttpActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetButton;
    private Button mPostButton;
    private Button mPutButton;
    private Button mDeleteButton;
    private Button mDownloadButton;
    private Button mUploadButton;

    private TextView mTextView;
    private ImageView mImageView;

    private Handler mHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nohttp);

        mGetButton = (Button) findViewById(R.id.get);
        mPostButton = (Button) findViewById(R.id.post);
        mPutButton = (Button) findViewById(R.id.put);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mDownloadButton = (Button) findViewById(R.id.download);
        mUploadButton = (Button) findViewById(R.id.upload);

        mTextView = (TextView) findViewById(R.id.text_view);
        mImageView = (ImageView) findViewById(R.id.image_view);

        mHandler = new Handler();

        mGetButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mPutButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
        mDownloadButton.setOnClickListener(this);
        mUploadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
//                ThreadUtils.newThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "https://api.toomao.com/1.1/activities";
//                        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
////                        request.add(map); // 添加参数
////                        request.addHeader("", ""); // 添加请求头
//                        Response<String> response = NoHttp.startRequestSync(request);
//                        if (response.isSucceed()) {
//                            final String s = response.get();
//
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mTextView.setText(s);
//                                }
//                            });
//                        }
//                    }
//                });

                // 创建请求队列
                RequestQueue getRequestQueue = NoHttp.newRequestQueue();
                Request<String> getRequest = NoHttp.createStringRequest("https://api.toomao.com/1.1/activities", RequestMethod.GET);
                // 如果需要添加很多个请求用第一个参数来区分，然后在监听里面就知道是哪个请求了
                getRequestQueue.add(0, getRequest, responseListener);
                break;
            case R.id.post:
//                ThreadUtils.newThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "https://api.toomao.com/1.1/activities/56a598013ef3c5501c0f4131/addcomments";
//                        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
////                        request.add(map); // 添加参数
//                        request.addHeader("X-AVOSCloud-Session-Token", "55a47a5d0cf2a07ccfa49d0a");  // https用这个
//                        request.addHeader("User-Agent", " toomao/2.4.2 NetType/wifi");
//                        Response<String> response = NoHttp.startRequestSync(request);
//                        if (response.isSucceed()) {
//                            final String s = response.get();
//
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mTextView.setText(s);
//                                }
//                            });
//                        }
//                    }
//                });

                // 创建请求队列
                RequestQueue postRequestQueue = NoHttp.newRequestQueue();
                Request<String> postRequest = NoHttp.createStringRequest("https://api.toomao.com/1.1/activities/56a598013ef3c5501c0f4131/addcomments", RequestMethod.POST);
//                        request.add(map); // 添加参数
                postRequest.addHeader("X-AVOSCloud-Session-Token", "55a47a5d0cf2a07ccfa49d0a");
                postRequest.addHeader("User-Agent", " toomao/2.4.2 NetType/wifi");
//                request.setDefineRequestBodyForJson(json);  // 向服务器发送一个json字符串
                // 如果需要添加很多个请求用第一个参数来区分，然后在监听里面就知道是哪个请求了
                postRequestQueue.add(1, postRequest, responseListener);
                break;
            case R.id.put:
//                ThreadUtils.newThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://develop.toomao.com:9000/1.1/activities/572ab83434439b17cf542cd0/praise";
//                        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.PUT);
//                        request.addHeader("X-AVOSCloud-Session-Token", "cd13bcc90a2e432ca42630dc5a96e7ad");
//                        request.addHeader("User-Agent", " toomao/2.5.1 NetType/wifi");
//                        request.addHeader("x-client-id", "dv_kandaoqinghulue,sy_qinghulue");
//                        request.addHeader("apikey", "64ee55183b48d3971bce76495de9d20c");
//                        request.addHeader("x-channel", "360");
//                        Response<String> response = NoHttp.startRequestSync(request);
//                        if (response.isSucceed()) {
//                            final String s = response.get();
//
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mTextView.setText(s);
//                                }
//                            });
//                        }
//                    }
//                });

                // 创建请求队列
                RequestQueue putRequestQueue = NoHttp.newRequestQueue();
                Request<String> putRequest = NoHttp.createStringRequest("http://develop.toomao.com:9000/1.1/activities/572ab83434439b17cf542cd0/praise", RequestMethod.PUT);
                putRequest.addHeader("X-AVOSCloud-Session-Token", "cd13bcc90a2e432ca42630dc5a96e7ad");
                putRequest.addHeader("User-Agent", " toomao/2.5.1 NetType/wifi");
                putRequest.addHeader("x-client-id", "dv_kandaoqinghulue,sy_qinghulue");
                putRequest.addHeader("apikey", "64ee55183b48d3971bce76495de9d20c");
                putRequest.addHeader("x-channel", "360");
                // 如果需要添加很多个请求用第一个参数来区分，然后在监听里面就知道是哪个请求了
                putRequestQueue.add(2, putRequest, responseListener);
                break;
            case R.id.delete:
                // 跟以上写法完全一样， 这里就不写了
                break;
            case R.id.download:
                ThreadUtils.newThread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://pic.toomao.com/bd90075f0e05997ac305d0e358125be344b64a47";
                        DownloadQueue downloadQueue = NoHttp.newDownloadQueue();
                        DownloadRequest downloadRequest = NoHttp.createDownloadRequest(url, Environment.getExternalStorageDirectory() + "", "aaa.png", true, true);
                        // 如果需要添加很多个请求用第一个参数来区分，然后在监听里面就知道是哪个请求了
                        downloadQueue.add(3, downloadRequest, downloadListener);
                    }
                });
                break;
            case R.id.upload:
                break;
        }
    }

    OnResponseListener<String> responseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {

        }

        @Override
        public void onSucceed(int what, Response<String> response) {
            mTextView.setText(response.get());
        }

        @Override
        public void onFailed(int what, Response<String> response) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    DownloadListener downloadListener = new DownloadListener() {

        @Override
        public void onDownloadError(int what, Exception exception) {

        }

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

        }

        @Override
        public void onProgress(int what, final int progress, long fileCount) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(progress + "");
                }
            });
        }

        @Override
        public void onFinish(int what, String filePath) {
            mTextView.setText(filePath);

            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onCancel(int what) {

        }
    };
}
