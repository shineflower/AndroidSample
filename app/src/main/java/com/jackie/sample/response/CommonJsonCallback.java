package com.jackie.sample.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jackie.sample.exception.OkHttpException;
import com.jackie.sample.listener.DisposeDataHandler;
import com.jackie.sample.listener.DisposeDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/30.
 */

/**
 * 专门处理Json的回调
 */
public class CommonJsonCallback implements Callback {
    /**
     * the logic layer exception, may alter in different app
     */
    protected final String RESULT_CODE = "ecode"; //有返回则对于http请求来说是成功的，但还有可能失败
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1;  //the network relative error
    protected final int JSON_ERROR = -2;  //the json relative error
    protected final int OTHER_ERROR = -3;  //the unknown error

    private DisposeDataListener mListener;
    private Class<?> mClazz;
    private Handler mHandler;

    public CommonJsonCallback(DisposeDataHandler handler) {
        mListener = handler.mListener;
        mClazz = handler.mClazz;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onFailure(e);
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        //还在子线程中
        final String result = response.body().toString();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(String result) {
        if (TextUtils.isEmpty(result)) {
            if (mListener != null) {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                return;
            }
        }

        try {
            JSONObject resultJsonObject = new JSONObject(result);
            if (resultJsonObject.has(RESULT_CODE)) {
                if (resultJsonObject.optInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClazz == null) {
                        if (mListener != null) {
                            mListener.onSuccess(resultJsonObject);
                        }
                    } else {
                        Gson gson = new Gson();
                        Object resultObject = gson.fromJson(result, mClazz);

                        if (resultObject == null) {
                            if (mListener != null) {
                                mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                            }
                        } else {
                            //相应真正正确的处理，并且直接返回了实体对象
                            if (mListener != null) {
                                mListener.onSuccess(resultObject);
                            }
                        }
                    }
                }
            } else {
                if (mListener != null) {
                    mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
                }
            }
        } catch (JSONException e) {
            if (mListener != null) {
                mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            }
        }
    }
}
