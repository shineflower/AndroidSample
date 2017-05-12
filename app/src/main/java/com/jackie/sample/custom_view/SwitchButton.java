package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.listener.OnSwitchStateChangeListener;

/**
 * Created by Jackie on 2015/12/15.
 * 自定义Switch控件
 */
public class SwitchButton extends View {
    private Bitmap mSwitchBackground, mSlideBackground;

    private int mCurrentX;  //当前X轴偏移量
    private boolean mCurrentState = false;  //当前状态，判断滑块是否滑动成功
    private boolean mIsSliding = false;    //滑块是否正在滑动

    private OnSwitchStateChangeListener mOnSwitchStateChangeListener; //状态改变的回调

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        initBitmap();
    }

    //初始化开关图片
    private void initBitmap() {
        mSwitchBackground = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        mSlideBackground = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button_background);
    }

    //设置当前控件的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置开关的宽和高
        setMeasuredDimension(mSwitchBackground.getWidth(), mSwitchBackground.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawBitmap(mSwitchBackground, 0, 0, null);

        if (mIsSliding) {  //正在滑动中
            int left = mCurrentX - mSlideBackground.getWidth();
            if (left < 0) {  //超过左边界
                left = 0;
            } else if (left > mSwitchBackground.getWidth() - mSlideBackground.getWidth()) { //超出右边界
                left = mSwitchBackground.getWidth() - mSlideBackground.getWidth();
            }

            canvas.drawBitmap(mSlideBackground, left, 0, null);
        } else {
            if (mCurrentState) {
                //绘制开关开的状态
                int left = mSwitchBackground.getWidth() - mSlideBackground.getWidth();
                canvas.drawBitmap(mSlideBackground, left, 0, null);
            } else {
                //绘制开关关的状态
                canvas.drawBitmap(mSlideBackground, 0, 0, null);
            }
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCurrentX = (int) event.getX();
                mIsSliding = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mCurrentX = (int) event.getX();
                mIsSliding = false;

                int center = mSwitchBackground.getWidth() / 2;
                boolean state = mCurrentX > center;   //滑块滑动的距离超过背景的一半，表示滑动成功，状态需要改变

                if (mCurrentState != state && mOnSwitchStateChangeListener != null) {  //两个状态不一样，表示滑动已经滑动成功，状态改变
                    mOnSwitchStateChangeListener.onSwitchStateChange(state);
                }

                mCurrentState = state;
                break;
        }

        invalidate();
        return true;
    }

    /**
     * 设置开关的状态
     * @param state 开关的状态，true表示打开，false表示关闭
     */
    public void setSwitchState(boolean state) {
        mCurrentState = state;
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener) {
        this.mOnSwitchStateChangeListener = onSwitchStateChangeListener;
    }
}
