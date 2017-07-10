package com.jackie.sample.wechat_image_upload.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.wechat_image_upload.bean.ImageShowPickerBean;
import com.jackie.sample.wechat_image_upload.listener.OnImageClickListener;
import com.jackie.sample.wechat_image_upload.listener.OnImageLoaderListener;
import com.jackie.sample.wechat_image_upload.listener.OnPictureClickListener;

import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 */

public class ImageShowPickerAdapter extends RecyclerView.Adapter<ImageShowPickerAdapter.ViewHolder> implements OnPictureClickListener {
    private Context mContext;
    private List<ImageShowPickerBean> mList;
    private int mMaxNum;

    public OnImageLoaderListener mOnImageLoaderListener;
    private OnImageClickListener mOnImageClickListener;

    private int mAddPictureResourceId;
    private int mDeletePictureResourceId;
    private boolean mIsShowDeletePicture;
    private boolean mIsShowAnimation;
    private int mShowNumOneLine;

    public int getAddPictureResourceId() {
        return mAddPictureResourceId;
    }

    public void setAddPictureResourceId(int addPictureResourceId) {
        this.mAddPictureResourceId = addPictureResourceId;
    }

    public int getDeletePictureResourceId() {
        return mDeletePictureResourceId;
    }

    public void setDeletePictureResourceId(int deletePictureResourceId) {
        this.mDeletePictureResourceId = deletePictureResourceId;
    }

    public boolean isIsShowDeletePicture() {
        return mIsShowDeletePicture;
    }

    public void setIsShowDeletePicture(boolean isShowDeletePicture) {
        this.mIsShowDeletePicture = isShowDeletePicture;
    }

    public boolean isIsShowAnimation() {
        return mIsShowAnimation;
    }

    public void setIsShowAnimation(boolean isShowAnimation) {
        this.mIsShowAnimation = isShowAnimation;
    }

    public void setShowNumOneLine(int showNumOneLine) {
        this.mShowNumOneLine = showNumOneLine;
    }

    public ImageShowPickerAdapter(Context context, List<ImageShowPickerBean> list, int maxNum, OnImageLoaderListener onImageLoaderListener, OnImageClickListener onImageClickListener) {
        this.mContext = context;
        this.mList = list;
        this.mMaxNum = maxNum;

        this.mOnImageLoaderListener = onImageLoaderListener;
        this.mOnImageClickListener = onImageClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FrameLayout frameLayout = new FrameLayout(mContext);
        parent.addView(frameLayout);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ViewHolder viewHolder = new ViewHolder(frameLayout, mOnImageLoaderListener, this);

        frameLayout.addView(viewHolder.pictureView);
        frameLayout.addView(viewHolder.deleteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList.size() == 0 || mList.size() == position) {
            mOnImageLoaderListener.displayImage(mContext, mAddPictureResourceId, holder.pictureView);
            holder.deleteView.setVisibility(View.GONE);
        } else {
            if (null == mList.get(position).getImageShowPickerUrl() || "".equals(mList.get(position).getImageShowPickerUrl())) {
                mOnImageLoaderListener.displayImage(mContext, mList.get(position).getImageShowPickerDelRes(), holder.pictureView);
            } else {
                mOnImageLoaderListener.displayImage(mContext, mList.get(position).getImageShowPickerUrl(), holder.pictureView);
            }

            if (mIsShowDeletePicture) {
                holder.deleteView.setVisibility(View.VISIBLE);
                holder.deleteView.setImageResource(mDeletePictureResourceId);
            } else {
                holder.deleteView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() < mMaxNum ? mList.size() + 1 : mList.size();
    }

    @Override
    public void onClickDeleteListener(int position) {
        mList.remove(position);

        if (mIsShowAnimation) {
            notifyItemRemoved(position);

            if (mList.size() - 1 >= 0 && mList.get(mList.size() - 1) == null) {
                notifyItemChanged(mList.size() - 1);
            } else if (mList.size() - 1 == 0) {
                notifyItemChanged(0);
            }
        } else {
            notifyDataSetChanged();
        }

        mOnImageClickListener.onClickDeleteListener(position, mList.size());
    }

    @Override
    public void onClickPictureListener(int position) {
        if (position == mList.size()) {
            if (mOnImageClickListener != null) {
                mOnImageClickListener.onClickAddListener(mMaxNum - position - 1);
            }
        } else {
            if (mOnImageClickListener != null) {
                mOnImageClickListener.OnClickPictureListener(mList, position, mMaxNum > mList.size() ? mMaxNum - mList.size() - 1 :
                        (mList.get(mMaxNum - 1) == null ? 1 : 0));
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View pictureView;
        public ImageView deleteView;
        private OnPictureClickListener onPictureClickListener;

        public ViewHolder(View view, OnImageLoaderListener onImageLoaderListener, OnPictureClickListener onPictureClickListener) {
            super(view);

            this.onPictureClickListener = onPictureClickListener;

            int pictureSize = (ScreenUtils.getScreenWidth(mContext) - (mShowNumOneLine + 1) * 10) / mShowNumOneLine;

            pictureView = onImageLoaderListener.createImageView(mContext);
            FrameLayout.LayoutParams pictureParams = new FrameLayout.LayoutParams(pictureSize, pictureSize);
            pictureParams.setMargins(10, 10, 10, 10);
            pictureView.setLayoutParams(pictureParams);

            deleteView = new ImageView(mContext);
            FrameLayout.LayoutParams deleteParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            deleteParams.gravity = Gravity.TOP | Gravity.END;
            deleteView.setPadding(5, 5, 5, 5);
            deleteView.setLayoutParams(deleteParams);

            pictureView.setId(R.id.iv_image_show_picker_picture);
            deleteView.setId(R.id.iv_image_show_picker_delete);

            pictureView.setOnClickListener(this);
            deleteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_image_show_picker_picture:
                    onPictureClickListener.onClickPictureListener(getLayoutPosition());
                    break;
                case  R.id.iv_image_show_picker_delete:
                    onPictureClickListener.onClickDeleteListener(getLayoutPosition());
                    break;
            }
        }
    }
}
