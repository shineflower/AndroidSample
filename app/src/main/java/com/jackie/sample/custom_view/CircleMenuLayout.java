package com.jackie.sample.custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/5/15.
 * 高仿建设银行的圆形菜单
 */

public class CircleMenuLayout extends ViewGroup{
    /**
     * 半径
     */
    private int mRadius;

    /**
     * 该容器的内边距，无视padding属性，如需边距请用该变量
     */
    private float mPadding;
    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 0;
    /**
     * 检测按下到抬起时旋转的角度
     */
    private float mSweepAngle;
    /**
     * 菜单项的文本
     */
    private String[] mItemTexts;
    /**
     * 菜单项的图标
     */
    private int [] mItemIcons;
    /**
     * 菜单项的个数
     */
    private int mMenuItemCount;
    /**
     * 检测按下到抬起时使用的使用
     */
    private long mDownTime;
    /**
     * 判断是否正在自动滚动
     */
    private boolean mIsFling;
    private int mMenuItemLayoutId;
    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingValue = FLING_VALUE;
    /**
     * 该容器内child item的默认尺寸
     */
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    /**
     * 菜单的中心child的默认尺寸
     */
    private static final float RADIO_DEFAULT_CENTER_ITEM_DIMENSION = 1 / 3f;
    /**
     * 该容器的内边距,无视padding属性，如需边距请用该变量
     */
    private static final float RADIO_PADDING_LAYOUT = 1 / 12f;
    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLING_VALUE = 300;
    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NO_CLICK_VALUE = 3;

    public interface OnMenuItemClickListener {
        void itemClick(View view, int position);
        void itemCenterClick(View view);
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;
    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    public CircleMenuLayout(Context context) {
        this(context, null);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //无视
        setPadding(0, 0, 0, 0);
    }

    /**
     * 设置布局的宽高，并测量menu item的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resWidth = 0;
        int resHeight = 0;

        /**
         * 根据传入的参数，分别获取测量模式和测量值
         */
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果宽或者高的测量模式非精确值
         */
        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            //主要设置为背景图的高度
            resWidth = getSuggestedMinimumWidth();
            //如果未设置背景图片，则设置为屏幕宽高的默认值
            resWidth = resWidth == 0 ? ScreenUtils.getMinScreenSize(getContext()) : resWidth;
            resHeight = getSuggestedMinimumHeight();
            //如果未设置背景图片，则设置为屏幕宽高的默认值
            resHeight = resHeight == 0 ? ScreenUtils.getMinScreenSize(getContext()) : resHeight;
        } else {
            //如果都设置为精确值，则直接取小值
            resWidth = resHeight = Math.min(widthSize, heightSize);
        }

        setMeasuredDimension(resWidth, resHeight);

        //获取半径
        mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());

        //menu item数量
        int childCount = getChildCount();
        //menu item尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        //menu item测量模式
        int childMode = MeasureSpec.EXACTLY;

        //迭代测量
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == GONE) {
                continue;
            }

            //计算menu item的尺寸，以及和设置好的模式，去对item进行测量
            int makeMeasureSpec = -1;
            if (childView.getId() == R.id.circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CENTER_ITEM_DIMENSION), childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }

            childView.measure(makeMeasureSpec, makeMeasureSpec);
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }

    /**
     * 设置menu item的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        // menu item的尺寸
        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        //根据menu item的个数，计算角度
        float unitAngle = 360 / (childCount - 1);

        //遍历去设置menu item的位置
        for (int i = 0; i< childCount; i++) {
            View childView = getChildAt(i);

            if (childView.getId() == R.id.circle_menu_item_center) {
                continue;
            }

            if (childView.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;

            //计算，中心点到menu item中心的距离
            float distance = mRadius / 2f - childSize / 2 - mPadding;

            //distance cos 即menu item中心点的横坐标
            int childLeft = (int) (mRadius / 2 + Math.round(
                                distance * Math.cos(Math.toRadians(mStartAngle)) - 1 / 2f * childSize));

            //distance sin 即menu item中心点的纵坐标
            int childTop = (int) (mRadius / 2 + Math.round(
                    distance * Math.sin(Math.toRadians(mStartAngle)) - 1 / 2f * childSize));


            childView.layout(childLeft, childTop, childLeft + childSize, childTop + childSize);
            mStartAngle += unitAngle;
        }

        //找到中心的view，如果存在设置onClick事件
        View centerView = findViewById(R.id.circle_menu_item_center);
        if (centerView != null) {
            centerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });

            //设置center item位置
            int centerLeft = mRadius / 2 - centerView.getMeasuredHeight() / 2;
            int centerRight = centerLeft + centerView.getMeasuredHeight();
            centerView.layout(centerLeft, centerLeft, centerRight, centerRight);
        }
    }

    /**
     * 记录上一次的x, y坐标
     */
    private float mLastX;
    private float mLastY;

    /**
     * 自动滚动的Runnable
     */
    private AutoFlingRunnable mAutoFlingRunnable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;

                mDownTime = System.currentTimeMillis();

                mSweepAngle = 0;

                //如果当时已经在快速滚动
                if (mIsFling) {
                    //移除快速滚动的回调
                    removeCallbacks(mAutoFlingRunnable);
                    mIsFling = false;
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //获得开始的角度
                float startAngle = getAngle(mLastX, mLastY);
                //获得当前的角度
                float endAngle = getAngle(x, y);

                //如果是一、四象限，则直接endAngle - startAngle，角度值都是正值
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += endAngle - startAngle;
                    mSweepAngle += endAngle - startAngle;
                } else {
                    //二、三象限，角度值是负值
                    mStartAngle += startAngle - endAngle;
                    mSweepAngle += startAngle - endAngle;
                }

                //重新布局
                requestLayout();

                mLastX = x;
                mLastY = y;
               break;
            case MotionEvent.ACTION_UP:
                //计算每秒移动的角度
                float anglePerSecond = mSweepAngle * 1000 / (System.currentTimeMillis() - mDownTime);

                //如果达到该值认为快速移动
                if (Math.abs(anglePerSecond) > mFlingValue && !mIsFling) {
                    post(mAutoFlingRunnable = new AutoFlingRunnable(anglePerSecond));
                    return true;
                }

                //如果当前旋转角度超过NOCLICK_VALUE，屏蔽点击
                if (Math.abs(mSweepAngle) > NO_CLICK_VALUE) {
                    return true;
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 主要为了action_down时，返回true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 根据触摸的位置，计算角度
     * @param xTouch
     * @param yTouch
     * @return
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     * @param x
     * @param y
     * @return
     */
    private int getQuadrant(float x, float y) {
        int quadrantX = (int) (x - mRadius / 2);
        int quadrantY = (int) (y - mRadius / 2);
        if (quadrantX >= 0) {
            return quadrantX >= 0 ? 4 : 1;
        } else {
            return quadrantX >= 0 ? 3 : 2;
        }
    }

    /**
     * 自动滚动的任务
     */
    private class AutoFlingRunnable implements Runnable {
        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        @Override
        public void run() {
            // 如果小于20,则停止
            if ((int) Math.abs(angelPerSecond) < 20) {
                mIsFling = false;
                return;
            }

            mIsFling = true;
            // 不断改变mStartAngle，让其滚动，/30为了避免滚动太快
            mStartAngle += (angelPerSecond / 30);
            // 逐渐减小这个值
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            // 重新布局
            requestLayout();
        }
    }

    /**
     * 设置MenuItem的布局文件，必须在setMenuItemIconsAndTexts之前调用
     */
    public void setMenuItemLayoutId(int menuItemLayoutId) {
        this.mMenuItemLayoutId = menuItemLayoutId;
    }

    /**
     * 设置菜单条目的图标和文本
     */
    public void setMenuItemIconsAndTexts(int[] itemIcons, String[] itemTexts) {
        mItemIcons = itemIcons;
        mItemTexts = itemTexts;

        // 参数检查
        if (itemIcons == null && itemTexts == null) {
            throw new IllegalArgumentException("菜单项文本和图片至少设置其一");
        }

        // 初始化mMenuCount
        mMenuItemCount = itemIcons == null ? itemTexts.length : itemIcons.length;

        if (itemIcons != null && itemTexts != null) {
            mMenuItemCount = Math.min(itemIcons.length, itemTexts.length);
        }

        addMenuItems();
    }

    /**
     * 添加菜单项
     */
    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        //根据用户设置的参数，初始化View
        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;
            View view = inflater.inflate(mMenuItemLayoutId, this, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.circle_menu_item_icon);
            TextView textView = (TextView) view.findViewById(R.id.circle_menu_item_text);

            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(mItemIcons[i]);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }

            if (textView != null) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(mItemTexts[i]);
            }

            //添加view到容器中
            addView(view);
        }
    }

    /**
     * 如果每秒旋转角度到达该值，则认为是自动滚动
     */
    public void setFlingValue(int flingValue) {
        this.mFlingValue = flingValue;
    }

    /**
     * 设置内边距的比例
     */
    public void setPadding(float padding) {
        this.mPadding = padding;
    }
}
