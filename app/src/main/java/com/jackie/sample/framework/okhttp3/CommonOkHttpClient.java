package com.jackie.sample.framework.okhttp3;

/**
 * Created by Administrator on 2016/10/30.
 */


import com.jackie.sample.listener.DisposeDataHandler;
import com.jackie.sample.response.CommonJsonCallback;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 用来发送get，post请求的工具类，包括设置一些请求的公用参数
 */
public class CommonOkHttpClient {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        //对https的认证
        okHttpBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
             return true;
          }
        });

        try {
            okHttpBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
            okHttpBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
            okHttpBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
            okHttpBuilder.followRedirects(true);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            okHttpBuilder.sslSocketFactory(sslContext.getSocketFactory());

            mOkHttpClient = okHttpBuilder.build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static void get(Request request, DisposeDataHandler handler) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handler));
    }

    public static void post(Request request, DisposeDataHandler handler) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handler));
    }
}
