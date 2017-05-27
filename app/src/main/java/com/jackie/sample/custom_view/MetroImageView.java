package com.jackie.sample.custom_view;


import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Jackie on 2017/5/26.
 * 仿Win8的metro的UI界面
 */

public class MetroImageView extends ImageView {
    //控件的宽
    private int mWidth;
    //控件的高
    private int mHeight;
    //控件的宽1/2
    private int mCenterWidth;
    //控件的高1/2
    private int mCenterHeight;
    //设置一个缩放的常量
    private float mMinScale = 0.85f;
    //缩放是否结束
    private boolean mIsFinish = true;


    private static final int SCALE_REDUCE_INIT = 0;
    private static final int SCALING = 1;
    private static final int SCALE_ADD_INIT = 6;

    public MetroImageView(Context context) {
        this(context, null);
    }

    public MetroImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MetroImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

            mCenterWidth = mWidth / 2;
            mCenterHeight = mHeight / 2;

            BitmapDrawable drawable = (BitmapDrawable) getDrawable();
            drawable.setAntiAlias(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                float x = event.getX();
//                float y = event.getY();

                mHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessage(SCALE_ADD_INIT);
                break;
        }

        /**
         * 执行顺序
         * dispatchTouchEvent > onTouch > onTouchEvent > onClick
         * 现在为ImageView设置OnClickListener是没有作用的，因为自定义的ImageView的onTouchEvent直接返回了true，
         * 不会往下执行onClick事件，如果你希望通过OnClickListener进行注册，
         * 你可以把onTouchEvent里面返回值改成super.onTouchEvent(event)，并且需要将ImageView的clickable设置为true
         */
//        return true;
        return super.onTouchEvent(event);
    }

    private Handler mHandler = new Handler() {
        private Matrix matrix = new Matrix();
        private int count = 0;
        private float scale;

        //是否已经调用了点击事件
        private boolean isClicked;

        @Override
        public void handleMessage(Message msg) {
            matrix.set(getImageMatrix());

            switch (msg.what) {
                case SCALE_REDUCE_INIT:
                    if (!mIsFinish) {
                        mHandler.sendEmptyMessage(SCALE_REDUCE_INIT);
                    } else {
                        mIsFinish = false;
                        count = 0;

                        scale = (float) Math.sqrt(mMinScale);
                        generateScale(matrix, scale);
                        mHandler.sendEmptyMessage(SCALING);
                    }

                    break;
                case SCALING:
                    generateScale(matrix, scale);

                    if (count < 4) {
                        mHandler.sendEmptyMessage(SCALING);
                    } else {
                        mIsFinish = true;

                        if (!isClicked) {
                            isClicked = true;
                        } else {
                            isClicked =  false;
                        }
                    }

                    count++;
                    break;
                case SCALE_ADD_INIT:
                    if (!mIsFinish) {
                        mHandler.sendEmptyMessage(SCALE_ADD_INIT);
                    } else {
                        mIsFinish = false;
                        count = 0;
                        scale = (float) Math.sqrt(1.0f / mMinScale);
                        generateScale(matrix, scale);
                        mHandler.sendEmptyMessage(SCALING);
                    }
                    break;
            }
        }
    };

    private synchronized  void generateScale(Matrix matrix, float scale) {
        matrix.postScale(scale, scale, mCenterWidth, mCenterHeight);
        setImageMatrix(matrix);
    }
}
