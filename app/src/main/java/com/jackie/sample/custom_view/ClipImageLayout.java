package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/15.
 */

public class ClipImageLayout extends RelativeLayout {
    private ClipImageView mClipImageView;
    private ClipImageBorder mClipImageBorder;

    private int mHorizontalPadding;
    private Drawable mImageDrawable;

    public ClipImageLayout(Context context) {
        this(context, null);
    }

    public ClipImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mClipImageView = new ClipImageView(context);
        mClipImageBorder = new ClipImageBorder(context);

        android.view.ViewGroup.LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        addView(mClipImageView, params);
        addView(mClipImageBorder, params);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClipImageLayout);

        mHorizontalPadding = ta.getDimensionPixelOffset(R.styleable.ClipImageLayout_horizontal_padding, mHorizontalPadding);
        mImageDrawable = ta.getDrawable(R.styleable.ClipImageLayout_image_drawable);

        ta.recycle();

		// 设置默认值
		if (mHorizontalPadding == 0) {
			//既没有setHorizontalPadding也没有在xml中定义，设置一个默认值
			mHorizontalPadding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
		}

		if (mImageDrawable == null) {
			//既没有setImageDrawable也没有在xml中定义，设置一个默认值
			mImageDrawable = getResources().getDrawable(R.drawable.yifei1);
		}

        mClipImageView.setImageDrawable(mImageDrawable);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageBorder.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     * @param horizontalPadding
     */
    public void setHorizontalPadding(int horizontalPadding) {
        this.mHorizontalPadding = horizontalPadding;

        mClipImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageBorder.setHorizontalPadding(mHorizontalPadding);
    }

    public void setImageDrawable(Drawable imageDrawable) {
        this.mImageDrawable = imageDrawable;

        mClipImageView.setImageDrawable(mImageDrawable);
    }

    /**
     * 裁切图片
     * @return
     */
    public Bitmap clip() {
        return mClipImageView.clip();
    }
}
