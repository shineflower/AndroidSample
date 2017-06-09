package com.jackie.sample.wechat_camera.listener;

import android.graphics.Bitmap;

/**
 * Created by Jackie on 2017/6/8.
 */

public interface JCameraListener {
    void captureSuccess(Bitmap bitmap);
    void recordSuccess(String url);
    void quit();
}
