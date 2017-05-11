package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackie.sample.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2015/12/30.
 * 流式布局
 */
public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        //new FlowLayout的时候会调用
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        //在布局文件中定义一个自定义View的时候(没有用到自定义属性)
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        //在布局文件中定义一个自定义View的时候(用到自定义属性)
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * MeasureSpec.EXACTLY        //match_parent或者精确值
           MeasureSpec.AT_MOST        //warp_content 这种情况的View的宽度和高度是需要我们自己计算的
           MeasureSpec.UNSPECIFIED   //少见，子控件想要多大就多大
         */
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        //记录每一行的高度和宽度
        int lineWidth = getPaddingLeft();
        int lineHeight = getPaddingBottom();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();

            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth < sizeWidth - getPaddingLeft() - getPaddingRight()) {  //未换行
                //叠加行宽
                lineWidth += childWidth;
                //得到当前行的最大高度(同一行中所有子View中的最大高度)
                lineHeight = Math.max(lineHeight, childHeight);
            } else {   //换行
                /**
                 * 换行之后，改行的宽和高就能确定
                 */
                //对比得到最大的宽度(所有行中的最大行宽)
                width = Math.max(width, lineWidth);
                //记录高度
                height += lineHeight;

                //重置lineWidth和lineHeight
                lineWidth = childWidth;
                lineHeight = childHeight;
            }

            //最后一个控件
            if (i == childCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                             modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    /**
     * 存储每一行所有子View的集合
     */
    private List<List<View>> mAllView = new ArrayList<>();

    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllView.clear();
        mLineHeight.clear();

        //当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        //存储每一行的所有子View
        List<View> lineView = new ArrayList<>();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            if (lineWidth + childWidth > width - getPaddingLeft() - getPaddingRight()) {
                //记录LineHeight
                mLineHeight.add(lineHeight);
                //记录当前行的View
                mAllView.add(lineView);

                //重置行宽和行高
                lineWidth = 0;
                lineHeight = childHeight;

                //重置子View集合
                lineView = new ArrayList<>();
            }

            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineView.add(child);

            if (child instanceof TextView) {
                ((TextView) child).setTextColor(CommonUtils.getInstance().getRandomColor());
            }
        }

        //最后一行
        mLineHeight.add(lineHeight);
        mAllView.add(lineView);

        //设置子View的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineCount = mAllView.size();
        for (int i = 0; i < lineCount; i++) {
            //当前行的所有子View的集合
            lineView = mAllView.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineView.size(); j++) {
                View child = lineView.get(j);

                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;

                int childLeft = left + params.leftMargin;
                int childTop = top + params.topMargin;
                int childRight = childLeft + child.getMeasuredWidth();
                int childBottom = childTop + child.getMeasuredHeight();

                //为子View设置布局
                child.layout(childLeft, childTop, childRight, childBottom);

                //同一行所有子View高度相同，左边距叠加
                left += childWidth;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}