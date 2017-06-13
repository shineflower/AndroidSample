package com.jackie.sample.wechat_image_upload.bean;

/**
 * Created by Jackie on 2017/6/12.
 * 显示数据类的父类，必须继承于该类
 */

public abstract class ImageShowPickerBean {

    public String getImageShowPickerUrl() {
        return setImageShowPickerUrl();
    }

    public int getImageShowPickerDelRes() {
        return setImageShowPickerDelRes();
    }

    /**
     * 为URL赋值，必须重写方法
     * @return
     */
    public abstract String setImageShowPickerUrl();

    /**
     * 为删除赋值，必须重写方法
     * @return
     */
    public abstract int setImageShowPickerDelRes();
}
