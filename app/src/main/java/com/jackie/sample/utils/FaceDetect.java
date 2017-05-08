package com.jackie.sample.utils;

import android.graphics.Bitmap;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Jackie on 2016/4/12.
 * 人脸检测工具类
 */
public class FaceDetect {
    public interface FaceDetectCallback {
        void success(JSONObject result);
        void error(FaceppParseException e);
    }

    public static void detect(final Bitmap bitmap, final FaceDetectCallback faceDetectCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpRequests httpRequests = new HttpRequests(Constant.APP_KEY, Constant.APP_SECRET, true, true);
                    Bitmap dstBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    dstBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    PostParameters parameters = new PostParameters();
                    parameters.setImg(stream.toByteArray());
                    JSONObject jsonObject = httpRequests.detectionDetect(parameters);

                    if (faceDetectCallback != null) {
                        faceDetectCallback.success(jsonObject);
                    }
                } catch (FaceppParseException e) {
                    if (faceDetectCallback != null) {
                        faceDetectCallback.error(e);
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
