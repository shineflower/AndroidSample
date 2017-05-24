package com.jackie.sample.framework.retrofit;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.CommentData;
import com.jackie.sample.bean.ShoppingCar;
import com.jackie.sample.utils.HttpUtils;
import com.jackie.sample.utils.ThreadUtils;
import com.squareup.okhttp.ResponseBody;

import java.util.HashMap;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Jackie on 2017/5/24.
 */

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mGetButton;
    private Button mPostButton;
    private Button mPutButton;
    private Button mDeleteButton;
    private TextView mResultTextView;

    private Handler mHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        mGetButton = (Button) findViewById(R.id.get);
        mPostButton = (Button) findViewById(R.id.post);
        mPutButton = (Button) findViewById(R.id.put);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mResultTextView = (TextView) findViewById(R.id.result);

        mHandler = new Handler();

        mGetButton.setOnClickListener(this);
        mPostButton.setOnClickListener(this);
        mPutButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                //同步请求
                ThreadUtils.newThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Get请求http和https都支持
                            //设置超时时间
//                            OkHttpClient client = new OkHttpClient.Builder().
//                                    connectTimeout(60, TimeUnit.SECONDS).
//                                    readTimeout(60, TimeUnit.SECONDS).
//                                    writeTimeout(60, TimeUnit.SECONDS).build();
//
//                            Retrofit retrofit = new Retrofit.Builder()
//                                    .baseUrl("http://10.10.10.10:48080/")
//                                    .client(client)
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(HttpUtils.BASE_URL_2)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
                            Call<ShoppingCar> call = service.get1_1Data();
                            final Response<ShoppingCar> response = call.execute();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mResultTextView.setText("code:" + response.code() + "------" + response.body().getCount());
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                // Get异步请求http和https都支持
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(HttpUtils.BASE_URL_2)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
//                Call<ShoppingCar> call = service.get1_1Data();
//                call.enqueue(new Callback<ShoppingCar>() {
//                    @Override
//                    public void onResponse(Response<ShoppingCar> response, Retrofit retrofit) {
//                        mResultTextView.setText("code:" + response.code() + "------" + response.body().getCount());
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        mResultTextView.setText(t.toString());
//                    }
//                });
                break;
            case R.id.post:
                // 同步请求
                ThreadUtils.newThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("content", "dddddddssssss");
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(HttpUtils.BASE_URL_2)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
                            Call<CommentData> call = service.addComment("56a598013ef3c5501c0f4131", map);
                            final Response<CommentData> response = call.execute();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mResultTextView.setText("code:" + response.code());
                                }
                            });
                        } catch (final Exception e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mResultTextView.setText(e.toString());
                                }
                            });
                        }
                    }
                });

                // 异步请求
//                HashMap<String, String> map = new HashMap<>();
//                map.put("content", "dddddddssssss");
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(HttpUtils.BASE_URL_2)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
//                Call<CommentData> call = service.addComment("56a598013ef3c5501c0f4131", map);
//                call.enqueue(new Callback<CommentData>() {
//                    @Override
//                    public void onResponse(Response<CommentData> response, Retrofit retrofit) {
//                        try {
//                            mResultTextView.setText("code:" + response.code());
//                        } catch (Exception e) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        mResultTextView.setText(t.toString());
//                    }
//                });
                break;
            case R.id.put:
                // 同步请求
                ThreadUtils.newThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Get请求http和https都支持
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(HttpUtils.BASE_URL_1)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
                            Call<ResponseBody> call = service.zanActive("572ab83434439b17cf542cd0");
                            final Response<ResponseBody> response = call.execute();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mResultTextView.setText("code:" + response.code() + "------" + response.body().string());
                                    } catch (Exception e) {

                                    }
                                }
                            });
                        } catch (final Exception e) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mResultTextView.setText(e.toString());
                                }
                            });
                        }
                    }
                });

                // 异步请求
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(HttpUtils.BASE_URL_1)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//                HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
//                Call<ResponseBody> call = service.zanActive("572ab83434439b17cf542cd0");
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
//                        try {
//                            mResultTextView.setText("code:" + response.code() + "------" + response.body().string());
//                        } catch (Exception e) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        mResultTextView.setText(t.toString());
//                    }
//                });
                break;
            case R.id.delete:
                // 同步请求
//                ThreadUtils.newThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            // Get请求http和https都支持
//                            Retrofit retrofit = new Retrofit.Builder()
//                                    .baseUrl(HttpUtils.BASE_URL_1)
//                                    .addConverterFactory(GsonConverterFactory.create())
//                                    .build();
//                            HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
//                            Call<ResponseBody> call = service.delOrder("572ab83434439b17cf542cd0");
//                            final Response<ResponseBody> response = call.execute();
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        mResultTextView.setText("code:" + response.code() + "------" + response.body().string());
//                                    } catch (Exception e) {
//
//                                    }
//                                }
//                            });
//                        } catch (final Exception e) {
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mResultTextView.setText(e.toString());
//                                }
//                            });
//                        }
//                    }
//                });

                // 异步请求
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(HttpUtils.BASE_URL_1)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                HttpUtils.GetDataService service = retrofit.create(HttpUtils.GetDataService.class);
                Call<ResponseBody> call = service.delOrder("572ab83434439b17cf542cd0");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            mResultTextView.setText("code:" + response.code() + "------" + response.body().string());
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        mResultTextView.setText(t.toString());
                    }
                });
                break;
        }
    }
}
