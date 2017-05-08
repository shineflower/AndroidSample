package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/1/10.
 * 扇形菜单
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {
    private enum POSITION { LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM};
    private POSITION mPosition = POSITION.RIGHT_BOTTOM;
    private int mRadius;

    private enum STATUS { OPEN, CLOSE }
    /**
     * 菜单当前状态
     */
    private STATUS mCurrentStatus = STATUS.CLOSE;

    /**
     * 菜单主按钮
     */
    private View mMainButton;

    public interface onMenuItemClickListener {
        void onItemClick(View view, int position);
    }

    private onMenuItemClickListener mOnMenuItemClickListener;

    public void setonMenuItemClickListener(onMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    private static final int LEFT_TOP = 0;
    private static final int LEFT_BOTTOM = 1;
    private static final int RIGHT_TOP = 2;
    private static final int RIGHT_BOTTOM = 3;

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcMenu);
        int position = ta.getInt(R.styleable.ArcMenu_position, RIGHT_BOTTOM);
        switch (position) {
            case LEFT_TOP:
                mPosition = POSITION.LEFT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition = POSITION.LEFT_BOTTOM;
                break;
            case RIGHT_TOP:
                mPosition = POSITION.RIGHT_TOP;
                break;
            case RIGHT_BOTTOM:
                mPosition = POSITION.RIGHT_BOTTOM;
                break;
        }

        mRadius = (int) ta.getDimension(R.styleable.ArcMenu_radius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            layoutMainButton();
            layoutChild();
        }
    }

    private void layoutMainButton() {
        //布局主Button
        mMainButton = getChildAt(0);
        mMainButton.setOnClickListener(this);

        int left = 0;
        int top = 0;

        int width = mMainButton.getMeasuredWidth();
        int height = mMainButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                left = 0;
                top = 0;
                break;
            case LEFT_BOTTOM:
                left = 0;
                top = getMeasuredHeight() - height;
                break;
            case RIGHT_TOP:
                left = getMeasuredWidth() - width;
                top = 0;
                break;
            case RIGHT_BOTTOM:
                left = getMeasuredWidth() - width;
                top = getMeasuredHeight() - height;
                break;
        }

        mMainButton.layout(left, top, left + width, top + height);
    }

    private void layoutChild() {
        int childCount = getChildCount();  //包括主按钮
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i + 1); //主按钮是第0个，必须跳过布局
            child.setVisibility(View.GONE);

            int l = (int) (mRadius * Math.sin(Math.PI / 2 / (childCount - 2) * i));
            int t = (int) (mRadius * Math.cos(Math.PI / 2 / (childCount - 2) * i));

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            //当菜单的位置在坐下或者右下
            if (mPosition == POSITION.LEFT_BOTTOM || mPosition == POSITION.RIGHT_BOTTOM) {
                t = getMeasuredHeight() - height - t;
            }

            //当菜单的位置在右上或者右下
            if (mPosition == POSITION.RIGHT_TOP || mPosition == POSITION.RIGHT_BOTTOM) {
                l = getMeasuredWidth() - width - l;
            }

            child.layout(l, t, l + width, t + height);
        }
    }

    @Override
    public void onClick(View v) {
        rotateMainButton(v, 0f, 360f, 300);

        //为Item添加平移动画
        toggleMenu(300);
    }

    private void rotateMainButton(View view, float start, float end, int duration) {
        RotateAnimation rotateAnimation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
    }

    private void toggleMenu(int duration) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View childView = getChildAt(i + 1);
            childView.setVisibility(View.VISIBLE);

            int l = (int) (mRadius * Math.sin(Math.PI / 2 / (childCount - 2) * i));
            int t = (int) (mRadius * Math.cos(Math.PI / 2 / (childCount - 2) * i));

            int xFlag = 1;
            int yFlag = 1;

            if (mPosition == POSITION.LEFT_TOP || mPosition == POSITION.LEFT_BOTTOM) {
                xFlag = -1;
            }

            if (mPosition == POSITION.LEFT_TOP || mPosition == POSITION.RIGHT_TOP) {
                yFlag = -1;
            }

            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation;

            if (mCurrentStatus == STATUS.CLOSE) {
                translateAnimation = new TranslateAnimation(xFlag * l, 0, yFlag * t, 0);
                childView.setClickable(true);
                childView.setFocusable(true);
            } else {
                translateAnimation = new TranslateAnimation(0, xFlag * l, 0, yFlag * t);
                childView.setClickable(false);
                childView.setFocusable(false);
            }

            //平移动画
            /**
             * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
             * 这四个参数的函数，都是起始点或者结束点距离当前位置的偏移
             */
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(duration);
            translateAnimation.setStartOffset(i * 50);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == STATUS.CLOSE) {
                        childView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            //旋转动画
            RotateAnimation rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(duration);
            rotateAnimation.setFillAfter(true);

            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);
            childView.startAnimation(animationSet);

            final int position = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.onItemClick(childView, position + 1);
                    }

                    menuItemAnimation(position);
                    changeStatus();
                }
            });
        }

        //切换菜单状态
        changeStatus();
    }

    //添加menuItem的点击动画
    private void menuItemAnimation(int position) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (i == position) {
                childView.startAnimation(scaleBigAnimation(300));
            } else {
                childView.startAnimation(scaleSmallAnimation(300));
            }

            childView.setClickable(false);
            childView.setClickable(false);
        }
    }

    /**
     * 为当前点击的Item设置变大和透明度降低的动画
     * @param duration
     * @return
     */
    private Animation scaleBigAnimation(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    private Animation scaleSmallAnimation(int duration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    private void changeStatus() {
        mCurrentStatus = (mCurrentStatus == STATUS.CLOSE ? STATUS.OPEN : STATUS.CLOSE);
    }

    public boolean isOpen() {
        return mCurrentStatus == STATUS.OPEN;
    }
}
