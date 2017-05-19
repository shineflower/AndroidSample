package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/5/19.
 * 1、循环滑动
 * 2、每次滑动结束，保持每个Item的完整
 */

public class LoopCompletenessScrollView extends ScrollView implements View.OnClickListener {
    private int mScreenHeight;

    private ViewGroup mViewGroup;
    private ScrollViewAdapter mAdapter;
    //条目的总数
    private int mItemCount;
    //每个条目的高度
    private int mItemHeight;

    private boolean mOnce = false;

    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setAdapter(ScrollViewAdapter adapter) {
        this.mAdapter = adapter;
    }

    public LoopCompletenessScrollView(Context context) {
        this(context, null);
    }

    public LoopCompletenessScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopCompletenessScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScreenHeight = ScreenUtils.getScreenHeight(context);
        mScreenHeight -= ScreenUtils.getStatusBarHeight(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //防止多次调用
        if (!mOnce) {
            mViewGroup = (ViewGroup) getChildAt(0);

            //根据Adapter的方法，为容器添加Item
            if (mAdapter != null) {
                mItemCount = mAdapter.getCount();
                mItemHeight = mScreenHeight / mItemCount;
                mViewGroup.removeAllViews();

                for (int i = 0; i < mItemCount; i++) {
                    addChildView(i);
                }
            }

            addChildView(0);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mOnce = true;
        int scrollY = getScrollY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (scrollY == 0) {
                    addChildToFirst();
                }

                //ScrollView的顶部已经到达屏幕顶部
                if (Math. abs(scrollY - mItemHeight) <= mItemCount) {
                    addChildToLast();
                }
                break;
            case MotionEvent.ACTION_UP:
                checkForReset();
                return true;
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 在容器末尾添加一个Item
     * @param position
     */
    private void addChildView(int position) {
        View view = mAdapter.getView(this, position);
        ViewGroup.LayoutParams params  =  new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);

        view.setLayoutParams(params);
        view.setTag(position);  //设置Tag
        view.setOnClickListener(this);  //添加时间
        mViewGroup.addView(view);
    }

    /**
     * 在容器指定位置添加一个Item
     * @param index
     */
    private void addChildView(int position, int index)
    {
        View view = mAdapter.getView(this, position);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);

        view.setLayoutParams(params);
        view.setTag(position);
        view.setOnClickListener(this);
        mViewGroup.addView(view, index);
    }

    /**
     * 在顶部添加一个View，并移除最后一个View
     */
    private void addChildToFirst() {
        int position = (int) mViewGroup.getChildAt(mItemCount - 1).getTag();
        addChildView(position, 0);
        mViewGroup.removeViewAt(mViewGroup.getChildCount() - 1);
        this.scrollTo(0, mItemHeight);
    }

    /**
     * 在底部添加一个View，并移除第一个View
     */
    private void addChildToLast() {
        int position = (int) mViewGroup.getChildAt(1).getTag();
        addChildView(position);
        mViewGroup.removeViewAt(0);
        this.scrollTo(0, 0);
    }

    /**
     * 检查当前getScrollY，显示完整Item，或者收缩此Item
     */
    private void checkForReset() {
        int offsetY = getScrollY() % mItemHeight;
        if (offsetY >= mItemHeight / 2) {
            smoothScrollTo(0, mItemHeight);
        } else {
            smoothScrollTo(0, 0);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(position, v);
        }
    }

    /**
     * 适配器
     */
    public static abstract class ScrollViewAdapter {
        public abstract View getView(LoopCompletenessScrollView parent, int position);
        public abstract int getCount();
    }
}
