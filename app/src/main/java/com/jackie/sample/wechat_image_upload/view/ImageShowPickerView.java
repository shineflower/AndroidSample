package com.jackie.sample.wechat_image_upload.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;
import com.jackie.sample.wechat_image_upload.adapter.ImageShowPickerAdapter;
import com.jackie.sample.wechat_image_upload.bean.ImageShowPickerBean;
import com.jackie.sample.wechat_image_upload.listener.OnImageClickListener;
import com.jackie.sample.wechat_image_upload.listener.OnImageLoaderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 * 单纯的图片展示选择控件
 */

public class ImageShowPickerView extends LinearLayout {
    private Context mContext;

    private RecyclerView mRecyclerView;

    //图片加载接口
    private OnImageLoaderListener mOnImageLoaderListener;
    private OnImageClickListener mOnImageClickListener;

    private ImageShowPickerAdapter mAdapter;
    private List<ImageShowPickerBean> mList;

    //默认单个大小
    private static final int PICTURE_SIZE = 80;
    //默认单行显示数量
    private static final int SHOW_NUM_ONE_LINE = 4;
    //默认单个大小
    private static final int MAX_NUM = 9;

    //单个item大小
    private int mPictureSize = PICTURE_SIZE;
    //添加图片
    private int mAddPictureResourceId;
    //删除图片
    private int mDeletePictureResourceId;
    //是否显示删除，默认true
    private boolean mIsShowDeletePicture;
    //是否展示动画，默认false
    private boolean mIsShowAnimation;
    //单行显示数量，默认4
    private int mShowNumOneLine;
    //最大数量
    private int mMaxNum;

    /**
     * 设置单个item大小
     * @param pictureSize
     */
    public void setPictureSize(int pictureSize) {
        this.mPictureSize = pictureSize;
    }

    /**
     * 设置增加图片
     * @param addPictureResourceId
     */
    public void setAddPictureResourceId(int addPictureResourceId) {
        this.mAddPictureResourceId = addPictureResourceId;
    }

    /**
     * 设置删除图片
     * @param deletePictureResourceId
     */
    public void setDeletePictureResourceId(int deletePictureResourceId) {
        this.mDeletePictureResourceId = deletePictureResourceId;
    }

    /**
     * 设置是否显示删除
     * @param isShowDeletePicture
     */
    public void setShowDeletePicture(boolean isShowDeletePicture) {
        this.mIsShowDeletePicture = isShowDeletePicture;
    }

    /**
     * 设置是否显示动画
     * @param isShowAnimation
     */
    public void setShowAnimation(boolean isShowAnimation) {
        mIsShowAnimation = isShowAnimation;
    }

    /**
     * 设置单行显示数量
     * @param showNumOneLine
     */
    public void setShowNumOneLine(int showNumOneLine) {
        this.mShowNumOneLine = showNumOneLine;
    }

    /**
     * 设置最大允许图片数量
     * @param maxNum
     */
    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public ImageShowPickerView(Context context) {
        this(context, null);
    }

    public ImageShowPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageShowPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageShowPickerView);
        mPictureSize = ta.getDimensionPixelSize(R.styleable.ImageShowPickerView_picture_size, DensityUtils.dip2px(context, mPictureSize));
        mAddPictureResourceId = ta.getResourceId(R.styleable.ImageShowPickerView_add_picture_resource_id, R.mipmap.image_show_picker_add);
        mDeletePictureResourceId = ta.getResourceId(R.styleable.ImageShowPickerView_delete_picture_resource_id, R.mipmap.image_show_picker_delete);
        mIsShowDeletePicture = ta.getBoolean(R.styleable.ImageShowPickerView_is_show_delete_picture, true);
        mIsShowAnimation = ta.getBoolean(R.styleable.ImageShowPickerView_is_show_animation, false);
        mShowNumOneLine = ta.getInt(R.styleable.ImageShowPickerView_show_num_one_line, SHOW_NUM_ONE_LINE);
        mMaxNum = ta.getInt(R.styleable.ImageShowPickerView_max_num, MAX_NUM);
        ta.recycle();

        mList = new ArrayList<>();
        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView);
    }

    /**
     * 最后调用方法显示，必须最后调用
     */
    public void show() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, mShowNumOneLine));

        ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
////        计算行数
//        int lineNumber = mList.size() % mShowNumOneLine == 0 ? mList.size() / mShowNumOneLine : (mList.size() / mShowNumOneLine) + 1;
////        计算高度 = 行数 * 每行的高度 + (行数 - 1) * 10dp 的 margin + 10dp(为了居中)
////        高度的计算需要自己好好理解，否则会产生嵌套recyclerView可以滑动的现象
//        layoutParams.height = DensityUtils.dip2px(getContext(), lineNumber * mPictureSize) ;
//        layoutParams.height =lineNumber  * mPictureSize ;

        mRecyclerView.setLayoutParams(layoutParams);

        mAdapter = new ImageShowPickerAdapter(mContext, mList, mMaxNum, mOnImageLoaderListener, mOnImageClickListener);
        mAdapter.setPictureSize(mPictureSize);
        mAdapter.setAddPictureResourceId(mAddPictureResourceId);
        mAdapter.setDeletePictureResourceId(mDeletePictureResourceId);
        mAdapter.setIsShowDeletePicture(mIsShowDeletePicture);
        mAdapter.setIsShowAnimation(mIsShowAnimation);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 图片加载器
     * @param onImageLoaderListener
     */
    public void setOnImageLoaderListener(OnImageLoaderListener onImageLoaderListener) {
        this.mOnImageLoaderListener = onImageLoaderListener;
    }

    /**
     * 设置选择器监听
     * @param onImageClickListener 选择器监听事件
     */
    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.mOnImageClickListener = onImageClickListener;
    }

    /**
     * 添加新数据
     * @param bean
     * @param <T>
     */
    public <T extends ImageShowPickerBean> void addData(T bean) {
        if (bean == null) {
            return;
        }

        mList.add(bean);

        if (mIsShowAnimation) {
            if (mAdapter != null) {
                mAdapter.notifyItemChanged(mList.size() - 1);
                mAdapter.notifyItemChanged(mList.size());
            }
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加新数据
     * @param list
     * @param <T>
     */
    public <T extends ImageShowPickerBean> void addData(List<T> list) {
        if (list == null) {
            return;
        }

        mList.addAll(list);

        if (mIsShowAnimation) {
            if (mAdapter != null) {
                mAdapter.notifyItemRangeChanged(mList.size() - list.size(), list.size());
            }
        } else {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 首次添加数据
     * @param list
     * @param <T>
     */
    public <T extends ImageShowPickerBean> void setData(List<T> list) {
        mList = new ArrayList<>();
        mList.addAll(list);

        if (mIsShowAnimation) {
            if (mAdapter != null) {
                mAdapter.notifyItemRangeChanged(mList.size() - list.size(), list.size());
            }
        } else {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取picker的list数据集合
     * @param <T>
     * @return
     */
    public <T extends ImageShowPickerBean> List<T> getDataList() {
        return (List<T>) mList;
    }
}
