package com.jackie.sample.wechat_image_upload.listener;

import android.content.Context;
import android.widget.ImageView;

public abstract class ImageLoader implements OnImageLoaderListener<ImageView> {

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
