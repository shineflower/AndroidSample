package com.jackie.sample.listener;

import com.jackie.sample.adapter.PhotoGridAdapter;

/**
 * Created by Jackie on 15/11/5.
 */
public interface OnPhotoGridItemClickListener {
    void onPhotoGridItemClick(PhotoGridAdapter.ViewHolder holder, int position);
}
