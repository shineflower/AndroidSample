package com.jackie.sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Jackie on 2015/12/29.
 * CommonUtils ViewHolder
 */
public class ViewHolder {
    private SparseArray<View> mSparseArray;
    private View mConvertView;
    private int mPosition;

    private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mSparseArray = new SparseArray<>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mPosition = position;

        mConvertView.setTag(this);
    }

    public static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            //new ViewHolder
            ViewHolder holder = new ViewHolder(context, parent, layoutId, position);
            return holder;
        } else {
            //通过getTag获取ViewHolder
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**
     * 通过viewId获取控件
     * @param viewId   ID
     * @param <T>      View
     */
    public <T extends View> T getView (int viewId) {
        View view = mSparseArray.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mSparseArray.put(viewId, view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的文本
     * @param viewId   viewId
     * @param text     text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * 设置ImageView和ImageButton的图片
     * @param viewId   viewId
     * @param resId    resId
     * @return         this
     */
    public ViewHolder setImageResource(int viewId, int resId) {
        View view = getView(viewId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(resId);
        } else if (view instanceof ImageButton) {
            ((ImageButton) view).setImageResource(resId);
        }
        return this;
    }

    /**
     * 设置ImageView的图片
     * @param viewId   viewId
     * @param bitmap   bitmap
     * @return         this
     */
    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 这只ImageView设置图片
     * @param viewId   viewId
     * @param path     path
     * @return         this
     */
    public ViewHolder loadImage(int viewId, String path) {
        ImageView imageView = getView(viewId);
        ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(path, imageView);
        return this;
    }
}
