package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/5/11.
 * 可拖拽交换位置的GridView
 */

public class DragGridView extends GridView implements AdapterView.OnItemLongClickListener {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private Vibrator mVibrator;
    private int mStatusHeight;

    private int mDownScrollBorder;
    private int mUpScrollBorder;

    private View mStartDragView;
    private View mLastHiddenView;
    private ImageView mImageView;
    private int mDragPosition;

    private int mDownX;
    private int mDownY;
    private int mMoveX;
    private int mMoveY;
    private int mUpX;
    private int mUpY;

    private boolean mIsDrag;

    private int mOffset2Left; //GridView屏幕左边的距离
    private int mOffset2Top;

    private int mSpeed = 30;
    private int mTouchSlop;

    public interface OnExchangeListener {

        /**
         * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
         * @param from 开始的position
         * @param to   拖拽的position
         */
        void onExchange(int from, int to);
    }

    private OnExchangeListener mOnExchangeListener;
    public void setOnExchangeListener(OnExchangeListener onExchangeListener) {
        this.mOnExchangeListener = onExchangeListener;
    }

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mStatusHeight = ScreenUtils.getStatusBarHeight(context);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setOnItemLongClickListener(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
        if (mDownScrollBorder == 0) {
            mDownScrollBorder = getHeight() / 4;
        }

        //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
        if (mUpScrollBorder == 0) {
            mUpScrollBorder = getHeight() * 3 / 4;
        }

        mStartDragView = view;
        mDragPosition = position;

        mVibrator.vibrate(50);  //震动一下

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        createDragImage(bitmap);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        mStartDragView.setVisibility(View.INVISIBLE);

        mIsDrag = true;

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                if (mOffset2Left == 0) {
                    mOffset2Left = (int) (ev.getRawX() - mDownX);
                }

                if (mOffset2Top == 0) {
                    mOffset2Top = (int) (ev.getRawY() - mDownY);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDrag && mStartDragView != null) {
                    mMoveX = (int) ev.getX();
                    mMoveY = (int) ev.getY();
                    dragItem();
                    return true;  //若return false, 会走父View本身的Scroll
                }
                break;
            case MotionEvent.ACTION_UP:
                mUpX = (int) ev.getX();
                mUpY = (int) ev.getY();

                if (mIsDrag) {
                    if (Math.abs(mUpX - mDownX) >= mTouchSlop && Math.abs(mUpY - mDownY) >= mTouchSlop) {
                        //正常拖动，remove callback 并 stopDrag
                    } else {
                        //如果只是长按一下，没有拖动，则显示mStartDragView，否则长按一下，没有拖动，mStartDragView也会消失
                        mStartDragView.setVisibility(View.VISIBLE);
                    }

                    removeCallbacks(mScrollRunnable);
                    stopDrag();
                    return true;
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void createDragImage(Bitmap bitmap) {
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.x = mStartDragView.getLeft() + mOffset2Left;
        mLayoutParams.y = mStartDragView.getTop() + mOffset2Top - mStatusHeight;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.alpha = 0.55f;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mImageView = new ImageView(getContext());
        mImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mImageView, mLayoutParams);
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     */
    private void dragItem() {
        mLayoutParams.x += mMoveX - mDownX;
        mLayoutParams.y += mMoveY - mDownY;
        mWindowManager.updateViewLayout(mImageView, mLayoutParams);

        mDownX = mMoveX;
        mDownY = mMoveY;

        swapItem();

        post(mScrollRunnable);
    }

    private void swapItem() {
        int position = pointToPosition(mMoveX, mMoveY);
        if (position != mDragPosition && position != AbsListView.INVALID_POSITION) {
            if (mOnExchangeListener != null) {
                mOnExchangeListener.onExchange(mDragPosition, position);
            }

            //旧位置上item显示
            if (mLastHiddenView == null) {
                mStartDragView.setVisibility(View.VISIBLE);
            } else {
                mLastHiddenView.setVisibility(View.VISIBLE);
            }

            //新位置上的item隐藏掉
            mLastHiddenView = getChildAt(position - getFirstVisiblePosition()); //在GridView中getChildAt只能拿到屏幕可见的view
            mLastHiddenView.setVisibility(View.INVISIBLE);

            mDragPosition = position;
        }
    }

    private void stopDrag() {
        if (mLastHiddenView != null) {
            mLastHiddenView.setVisibility(View.VISIBLE);
        }

        mWindowManager.removeView(mImageView);
        mImageView = null;
        mIsDrag = false;

        mLastHiddenView = null;
    }

    /**
     * 判定 自动 滚动，并在滚动期间触发item交换
     */
    private Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY = 0;
            if (mMoveY > mUpScrollBorder) {
                scrollY = mSpeed;
                postDelayed(mScrollRunnable, 25);
            } else if (mMoveY < mDownScrollBorder) {
                scrollY = -mSpeed;
                postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
            }

            //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
            //所以我们在这里调用下swapItem()方法来交换item
            swapItem();

//            scrollBy(0, scrollY); //只能用下面的
            smoothScrollBy(scrollY, 10);//api8 android2.2
        }
    };
}
