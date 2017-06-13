package com.jackie.sample.wechat_image_upload.listener;

import com.jackie.sample.wechat_image_upload.bean.ImageShowPickerBean;

import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 * 点击事件的接口
 */

public interface OnImageClickListener {
    void onClickAddListener(int count);
    void onClickDeleteListener(int position, int count);
    void OnClickPictureListener(List<ImageShowPickerBean> list, int position, int count);
}
