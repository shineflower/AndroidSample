package com.jackie.sample.wechat_image_upload.listener;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Loader extends ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    @Override
    public void displayImage(Context context, @DrawableRes int resId, ImageView imageView) {
        imageView.setImageResource(resId);
    }
}