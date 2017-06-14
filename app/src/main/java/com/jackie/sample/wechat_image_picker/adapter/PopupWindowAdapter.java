package com.jackie.sample.wechat_image_picker.adapter;

import android.content.Context;

import com.jackie.sample.R;
import com.jackie.sample.wechat_image_picker.bean.ImageFolder;
import com.jackie.sample.wechat_image_picker.utils.ViewHolder;

import java.util.List;

public class PopupWindowAdapter extends CommonAdapter<ImageFolder> {

    public PopupWindowAdapter(Context context, List<ImageFolder> list) {
        super(context, R.layout.item_directory, list);
    }

    @Override
    public void convert(ViewHolder holder, ImageFolder imageFolder) {
        //重置状态
        holder.setImageResource(R.id.directory_item_image, R.drawable.picture_no);

        holder.loadImage(R.id.directory_item_image, imageFolder.getFirstImagePath()).setText(R.id.directory_item_name, imageFolder.getName()).setText(R.id.directory_item_count, imageFolder.getCount() + "");
    }
}