package com.jackie.sample.wechat_image_picker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.jackie.sample.R;
import com.jackie.sample.wechat_image_picker.utils.ViewHolder;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends CommonAdapter<String> {
    private String mDirectoryPath;

    private DisplayMetrics mDisplayMetrics;

    //保存选中的图片的绝对路径
    final static Set<String> selectedImageSet = new HashSet<>();

    public ImageAdapter(Context context, List<String> imagePathList, String directoryPath){
        super(context, R.layout.item_image, imagePathList);
        this.mDirectoryPath = directoryPath;

        mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    public void setDirectoryPath(String directoryPath) {
        this.mDirectoryPath = directoryPath;
    }

    @Override
    public void convert(final ViewHolder holder, String path) {
        final ImageView imageView = holder.getView(R.id.item_image);
        final String filePath = mDirectoryPath + File.separator + path;

        //重置状态
        holder.setImageResource(R.id.item_image, R.drawable.picture_no).setImageResource(R.id.item_selected, R.drawable.picture_unselected);
        imageView.setColorFilter(null);

        //最大宽度设置为屏幕的1/3(GridView有3列)
        imageView.setMaxWidth(mDisplayMetrics.widthPixels / 3);

        holder.loadImage(R.id.item_image, filePath);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已经选中
                if (selectedImageSet.contains(filePath)) {
                    //清除选中状态
                    selectedImageSet.remove(filePath);
                    imageView.setColorFilter(null);
                    holder.setImageResource(R.id.item_selected, R.drawable.picture_unselected);
                } else {
                    selectedImageSet.add(filePath);
                    imageView.setColorFilter(Color.parseColor("#77000000"));
                    holder.setImageResource(R.id.item_selected, R.drawable.picture_selected);
                }
            }
        });

        if (selectedImageSet.contains(filePath)) {
            imageView.setColorFilter(Color.parseColor("#77000000"));
            holder.setImageResource(R.id.item_selected, R.drawable.picture_selected);
        }
    }
}