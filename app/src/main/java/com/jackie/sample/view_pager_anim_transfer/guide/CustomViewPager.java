package com.jackie.sample.view_pager_anim_transfer.guide;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Jackie on 2016/3/23.
 * 自定义ViewPager实现切换动画
 */
public class CustomViewPager extends ViewPager {
    private SparseArray<View> mSparseArray = new SparseArray<>();

    private View mLeftView;
    private View mRightView;

    private float mScale;
    private float mTranslation;

    private static final float MIN_SCALE = 0.6f;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addViewToPosition(View view, Integer position) {
        mSparseArray.put(position, view);
    }

    public void removeViewFromPosition(Integer position) {
        mSparseArray.remove(position);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        mLeftView = mSparseArray.get(position);
        mRightView = mSparseArray.get(position + 1);

        animateStack(mLeftView, mRightView, offset, offsetPixels);

        super.onPageScrolled(position, offset, offsetPixels);
    }

    private void animateStack(View leftView, View rightView, float offset, int offsetPixels) {
        if (rightView != null) {
            mScale = (1 - MIN_SCALE) * offset + MIN_SCALE;
            mTranslation = -getWidth() - getPageMargin() + offsetPixels;


            ViewHelper.setScaleX(rightView, mScale);
            ViewHelper.setScaleY(rightView, mScale);

            ViewHelper.setTranslationX(rightView, mTranslation);
        }

        if (leftView != null) {
            leftView.bringToFront();
        }
    }
}
