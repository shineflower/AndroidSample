package com.jackie.sample.wechat_image_upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.wechat_image_upload.bean.ImageBean;
import com.jackie.sample.wechat_image_upload.bean.ImageShowPickerBean;
import com.jackie.sample.wechat_image_upload.listener.Loader;
import com.jackie.sample.wechat_image_upload.listener.OnImageClickListener;
import com.jackie.sample.wechat_image_upload.view.ImageShowPickerView;

import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<List<ImageBean>> mList;

    public ImageAdapter(Context context, List<List<ImageBean>> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public List<ImageBean> getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_upload, null);
            holder.imageShowPickerView = (ImageShowPickerView) convertView.findViewById(R.id.image_show_picker_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final List<ImageBean> list = getItem(position);
        holder.imageShowPickerView.setOnImageLoaderListener(new Loader());
        holder.imageShowPickerView.setMaxNum(mList.size() * list.size());  //所有图片的集合
        holder.imageShowPickerView.setData(list);

        //展示有动画和无动画
        if (position % 2 == 1) {
            holder.imageShowPickerView.setShowAnimation(true);
        } else {
            holder.imageShowPickerView.setShowAnimation(false);
        }

        holder.imageShowPickerView.setOnImageClickListener(new OnImageClickListener() {
            @Override
            public void onClickAddListener(int count) {
                list.add(new ImageBean("http://pic78.huitu.com/res/20160604/1029007_20160604114552332126_1.jpg"));
                holder.imageShowPickerView.addData(new ImageBean("http://pic78.huitu.com/res/20160604/1029007_20160604114552332126_1.jpg"));
            }

            @Override
            public void onClickDeleteListener(int position, int count) {
                Toast.makeText(mContext, "remain count: " + count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickPictureListener(List<ImageShowPickerBean> list, int position, int count) {
                Toast.makeText(mContext, "position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        holder.imageShowPickerView.show();

        return convertView;
    }

    private class ViewHolder {
        private ImageShowPickerView imageShowPickerView;
    }
}
