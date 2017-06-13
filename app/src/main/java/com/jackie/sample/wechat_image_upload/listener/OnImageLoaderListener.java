package com.jackie.sample.wechat_image_upload.listener;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Jackie on 2017/6/12.
 * 加载图片接口
 */

public interface OnImageLoaderListener<T extends View> extends Serializable {
    void displayImage(Context context, String path, T t);
    void displayImage(Context context, @DrawableRes int resId, T t);
    T createImageView(Context context);
}
