package com.jackie.sample.okhttp3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.listener.DisposeDataHandler;
import com.jackie.sample.listener.DisposeDataListener;
import com.jackie.sample.request.CommonRequest;
import com.jackie.sample.request.CountingRequestBody;
import com.jackie.sample.request.RequestParams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Okhttp3Activity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetButton, mPostButton, mPostJsonButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp3);

        initView();
        initEvent();
    }

    private void initView() {
        mGetButton = (Button) findViewById(R.id.get);
        mPostButton = (Button) findViewById(R.id.post);
        mPostJsonButton = (Button) findViewById(R.id.post_json);
    }

    private void initEvent() {
        mGetButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mPostJsonButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                getRequest();
                break;
            case R.id.post:
                postRequest();
                break;
            case R.id.post_json:
                postJsonRequest();
                break;
        }
    }

    private void getRequest() {
//        封装之前的okHttp get请求
//        1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
//        2. 构造Request对象
        Request.Builder requestBuilder = new Request.Builder();
//        Request request = requestBuilder.url("https://www.baidu.com").build();
        Request request = requestBuilder.get().url("https://www.baidu.com").build();
//        3. 将Request封装为Call
        Call call = okHttpClient.newCall(request);
//        4. 执行call
//        Response response = call.execute();     //同步
        call.enqueue(new Callback() {             //异步
            @Override
            public void onFailure(Call call, IOException e) {

            }

            /**
             * 此时还在非UI线程中
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                response.body().string();
            }
        });

        //封装之后的okHttp get请求
        CommonOkHttpClient.get(CommonRequest.createGetRequest(
                "https://publicobject.com/helloworld.txt", null),
                new DisposeDataHandler(new DisposeDataListener() {
            @Override
            public void onSuccess(Object response) {
                Log.d("chengjie", response.toString());
            }

            @Override
            public void onFailure(Object error) {

            }
        }));
    }

    private void postRequest() {
        //封装之前的okHttp post请求
//        1. 拿到okHttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
//        2. 构造Request对象
        FormBody.Builder fromBodyBuilder = new FormBody.Builder();
//        2.1 构造RequestBody对象
        RequestBody requestBody = fromBodyBuilder.add("mb", "19811230100").add("pwd", "999999q").build();

        Request.Builder requestBuilder = new Request.Builder();
        //没有传url，会报异常
        Request request = requestBuilder.post(requestBody).url("").build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

        //封装之后的okhttp post请求
        RequestParams params = new RequestParams();
        params.put("mb", "19811230100");
        params.put("pwd", "999999q");

        CommonOkHttpClient.post(CommonRequest.createPostRequest("", params),
                new DisposeDataHandler(new DisposeDataListener() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onFailure(Object error) {

            }
        }));
    }

    //post 传递json字符串
    private void postJsonRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), "{mb:19811230100,pwd:123}");

//        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
//        if (!file.exists()) {
//            Log.d("chengjie", file.getAbsolutePath() + " not exist");
//            return;
//        }
//
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(requestBody).url("").build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    //上传文件
    private void postUploadRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();

        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        if (!file.exists()) {
            Log.d("chengjie", file.getAbsolutePath() + " not exist");
            return;
        }

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM)
                .addFormDataPart("username", "jackie")
                .addFormDataPart("password", "123")
                /**
                 * mPhoto 表单域 input标签
                 * 在服务端，通过这个表单域来获取上传的文件
                 *
                 * 这两个命名是固定的
                 * File mPhoto
                 * String mPhotoFileName
                 *
                 * if (mPhoto == null) {
                 *      System.out.println(mPhotoFileName + " is null");
                 * }
                 *
                 * String dir = ServletActionContext.getServletContext().getRealPath("files");
                 * File file = new File(dir, mPhotoFileName);
                 * FileUtils.copyFile(mPhoto, file)  //struts2封装的方法
                 */
                .addFormDataPart("mPhoto", "jackie.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.OnCountingListener() {
            @Override
            public void onRequestProgress(long byteWritten, long contentLength) {
                Log.d("chengjie", byteWritten + "/" + contentLength);
            }
        });

        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.post(requestBody).url("").build();


        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void download() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.get().url("" + "test.jpg").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //文件总长度
                long total = response.body().contentLength();
                long sum = 0L;

                InputStream is = response.body().byteStream();
                FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "12306.jpg"));

                int len = 0;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);

                    sum += len;

                    Log.d("chengjie", sum + "/" + total);
                }

                fos.flush();
                fos.close();
                is.close();
            }
        });
    }

    private void downloadImage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder();
        Request request = requestBuilder.get().url("" + "test.jpg").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                //压缩

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        mImageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }
}
