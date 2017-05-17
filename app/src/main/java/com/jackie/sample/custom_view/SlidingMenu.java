package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.jackie.sample.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by Jackie on 2016/1/7.
 * 侧滑菜单
 */
public class SlidingMenu extends HorizontalScrollView {
    private ViewGroup mMenuView;
    private ViewGroup mMainView;

    private int mScreenWidth;
    private int mMenuPaddingRight;
    private int mMenuWidth;

    private boolean mOnce = false;
    private boolean mIsMenuOpen = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    //在xml声明SlidingMenu，但是没有使用自定义属性，会调用此方法
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    //在xml声明SlidingMenu，使用了自定义属性，会调用此方法
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;

        //获取自定义属性的值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        mMenuPaddingRight = ta.getDimensionPixelSize(R.styleable.SlidingMenu_padding_right, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 50, context.getResources().getDisplayMetrics()));
        ta.recycle();
    }

    /**
     * 计算子View的宽和高和自己的宽和高
     * @param widthMeasureSpec   边界宽度
     * @param heightMeasureSpec  边界高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //保证只调用一次
        if (!mOnce) {
            LinearLayout mWrapperLayout = (LinearLayout) getChildAt(0);
            mMenuView = (ViewGroup) mWrapperLayout.getChildAt(0);
            mMainView = (ViewGroup) mWrapperLayout.getChildAt(1);

            //设置子View的宽和高
            mMenuView.getLayoutParams().width = mMenuWidth = mScreenWidth - mMenuPaddingRight;
            mMainView.getLayoutParams().width = mScreenWidth;
            mOnce = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            //隐藏menu
            scrollTo(mMenuWidth, 0);  //滚动条向右滑动mMenuWidth，HorizontalScrollView向左滑动mMenuWidth。
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * getScrollX()
                 * The left edge of the displayed part of your view, in pixels
                 * 当前表示隐藏在屏幕左边的宽度
                 *
                 * 简而言之，getScrollX() 就是当前view的左上角相对于父布局的左上角的X轴偏移量
                 */
                int scrollX = getScrollX();
                if (scrollX < mMenuWidth / 2) {
                    //显示菜单
                    smoothScrollTo(0, 0);
                    mIsMenuOpen = true;
                } else {
                    //隐藏菜单
                    smoothScrollTo(mMenuWidth, 0);
                    mIsMenuOpen = false;
                }
                return true;  //消费了事件
        }
        //千万不能掉，因为ACTION_DOWN和ACTION_UP会调用
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        //调用属性动画，设置X轴偏移量
        //l  就是getScrollX()，表示隐藏在屏幕左边的宽度
        /**
         * l = getScrollX(); 开始菜单隐藏，l = mMenuWidth，菜单显示：l = 0;
         * l:      mMenuWidth -> 0
         * scale:  1 -> 0
         */
        float scale = l * 1.0f / mMenuWidth;
        /**
         * 区别1：主界面1.0 ~ 0.7缩放的效果 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化
         * 缩放：0.7 ~ 1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0  0.6 + 0.4 * (1- scale) ;
         *
         */
        float mainScale = 0.7f + 0.3f * scale;
        float menuScale = 1.0f - 0.3f * scale;
        float menuAlpha = 0.6f + 0.4f * (1 - scale);
        ViewHelper.setTranslationX(mMenuView, mMenuWidth * scale * 0.5f);
//        ViewHelper.setTranslationX(mMenuView, l);
//        mMenuView.setTranslationX(l);

        //设置菜单的属性动画
        ViewHelper.setScaleX(mMenuView, menuScale);
        ViewHelper.setScaleY(mMenuView, menuScale);
        ViewHelper.setAlpha(mMenuView, menuAlpha);

        // 设置主界面的属性动画和缩放的中心点
        ViewHelper.setPivotX(mMainView, 0);
        ViewHelper.setPivotY(mMainView, mMainView.getHeight() / 2);
        ViewHelper.setScaleX(mMainView, mainScale);
        ViewHelper.setScaleY(mMainView, mainScale);
    }

    private void openMenu() {
        if (mIsMenuOpen) return;
        smoothScrollTo(0, 0);
        mIsMenuOpen = true;
    }

    private void closeMenu() {
        if (!mIsMenuOpen) return;
        smoothScrollTo(mMenuWidth, 0);
        mIsMenuOpen = false;
    }

    public void toggle() {
        if (mIsMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }
}
