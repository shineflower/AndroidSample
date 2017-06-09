package com.jackie.sample.wechat_camera.listener;

/**
 * Created by Jackie on 2017/6/8.
 */

public interface CaptureListener {
    void takePicture();
    void recordStart();
    void recordShort(long time);
    void recordStop(long time);
    void recordZoom(float zoom);
}
