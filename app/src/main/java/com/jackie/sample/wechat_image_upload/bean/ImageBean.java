package com.jackie.sample.wechat_image_upload.bean;

/**
 * Created by Jackie on 2017/6/12.
 */

public class ImageBean extends ImageShowPickerBean {
    public ImageBean(String url) {
        this.url = url;
    }

    public ImageBean(int resId) {
        this.resId = resId;
    }

    private String url;

    private int resId;

    @Override
    public String setImageShowPickerUrl() {
        return url;
    }

    @Override
    public int setImageShowPickerDelRes() {
        return resId;
    }
}
