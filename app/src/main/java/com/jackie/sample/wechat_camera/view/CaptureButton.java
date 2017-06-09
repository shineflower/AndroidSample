package com.jackie.sample.wechat_camera.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jackie.sample.wechat_camera.listener.CaptureListener;

import static com.jackie.sample.wechat_camera.view.JCameraView.BUTTON_STATE_BOTH;
import static com.jackie.sample.wechat_camera.view.JCameraView.BUTTON_STATE_ONLY_CAPTURE;
import static com.jackie.sample.wechat_camera.view.JCameraView.BUTTON_STATE_ONLY_RECORDER;

/**
 * Created by Jackie on 2017/6/8.
 * 拍照按钮
 */

public class CaptureButton extends View {
    //按钮可执行的功能状态
    private int mButtonState;

    //空状态
    public static final int STATE_NULL = 0x000;
    //点击按下时候的状态
    public static final int STATE_PRESS_CLICK = 0x001;
    //点击后松开时候的状态
    public static final int STATE_RELEASE_CLICK = 0x002;
    //长按按下时候的状态
    public static final int STATE_PRESS_LONG_CLICK = 0x003;
    //长按后松开时候的状态
    public static final int STATE_RELEASE_LONG_CLICK = 0x004;

    //长按后处理的逻辑Runnable
    private LongPressRunnable mLongPressRunnable;
    //录制视频的Runnable
    private RecordRunnable mRecordRunnable;
    //录视频进度条动画
    private ValueAnimator mRecordAnimator = ValueAnimator.ofFloat(0, 362);

    //当前按钮状态
    private int mState;

    private Paint mPaint;
    //进度条宽度
    private float mStrokeWidth;
    //长按外圆半径变大的size
    private int mOutsideAddSize;
    //长按内圆缩小的size
    private int mInsideReduceSize;

    //中心坐标
    private float mCenterX;
    private float mCenterY;

    //按钮半径
    private float mButtonRadius;

    //外圆半径
    private float mButtonOutsideRadius;
    //内圆半径
    private float mButtonInsideRadius;

    //按钮大小
    private int mButtonSize;
    //录制视频的进度
    private float mProgress;
    private RectF mRectF;
    //录制视频最大时间长度
    private int mDuration;

    //按钮回调接口
    private CaptureListener mCaptureListener;
    private boolean mHasWindowFocus = true;

    public CaptureButton(Context context) {
        super(context);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CaptureButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //customize construction method
    public CaptureButton(Context context, int size) {
        super(context);

        this.mButtonSize = size;
        mButtonRadius = size / 2.0f;

        mButtonOutsideRadius = mButtonRadius;
        mButtonInsideRadius = mButtonRadius * 0.75f;

        mStrokeWidth = size / 15;
        mOutsideAddSize = size / 5;
        mInsideReduceSize = size / 8;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mProgress = 0;

        //init long press runnable
        mLongPressRunnable = new LongPressRunnable();
        mRecordRunnable = new RecordRunnable();

        //set default state
        this.mState = STATE_NULL;

        this.mButtonState = BUTTON_STATE_BOTH;

        //set max record duration, default 10 * 1000
        mDuration = 10 * 1000;

        mCenterX = (mButtonSize + mOutsideAddSize * 2) / 2;
        mCenterY = (mButtonSize + mOutsideAddSize * 2) / 2;

        mRectF = new RectF(
                mCenterX - (mButtonRadius + mOutsideAddSize - mStrokeWidth / 2),
                mCenterY - (mButtonRadius + mOutsideAddSize - mStrokeWidth / 2),
                mCenterX + (mButtonRadius + mOutsideAddSize - mStrokeWidth / 2),
                mCenterY + (mButtonRadius + mOutsideAddSize - mStrokeWidth / 2)
        );
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mButtonSize + mOutsideAddSize * 2, mButtonSize + mOutsideAddSize * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        //外圆(半透明灰色)
        mPaint.setColor(0xeecccccc);
        canvas.drawCircle(mCenterX, mCenterY, mButtonOutsideRadius, mPaint);

        //内圆(白色)
        mPaint.setColor(0xffffffff);
        canvas.drawCircle(mCenterX, mCenterY, mButtonInsideRadius, mPaint);

        //如果状态为按钮长按按下的状态，则绘制录制进度条
        if (mState == STATE_PRESS_LONG_CLICK) {
            mPaint.setAntiAlias(true);
            mPaint.setColor(0x9900cc00);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStrokeWidth);
            canvas.drawArc(mRectF, -90, mProgress, false, mPaint);
        }
    }

    //Touch Event Down时候记录的Y值
    private float mDownY;
    private boolean mIsRecording = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() > 1) {
                    break;
                }

                //记录Y值
                mDownY = event.getY();
                //修改当前状态为点击按下
                mState = STATE_PRESS_CLICK;
                //当前状态能否录制
                //判断按钮状态是否为可录制状态
                if (!mIsRecording && (mButtonState == BUTTON_STATE_ONLY_RECORDER ||
                        mButtonState == BUTTON_STATE_BOTH)) {
                    //同时延长500ms启动长按后处理的逻辑Runnable
                    postDelayed(mLongPressRunnable, 500);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCaptureListener != null
                        && mState == STATE_PRESS_LONG_CLICK
                        && (mButtonState == BUTTON_STATE_ONLY_RECORDER || mButtonState == BUTTON_STATE_BOTH)) {
                    //记录当前Y值与按下时候Y值得差值
                    mCaptureListener.recordZoom(mDownY - event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                //根据当前按钮的状态进行相应的处理
                handleReleaseByState();
                break;
        }
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.mHasWindowFocus = hasWindowFocus;
    }

    //当手指松开按钮时候处理的逻辑
    private void handleReleaseByState() {
        //移除长按逻辑的Runnable
        removeCallbacks(mLongPressRunnable);
        //根据当前状态处理
        switch (mState) {
            case STATE_PRESS_CLICK:
                if (mCaptureListener != null &&
                        (mButtonState == BUTTON_STATE_ONLY_CAPTURE || mButtonState == BUTTON_STATE_BOTH)) {
                    //回调拍照接口
                    mCaptureListener.takePicture();
                }
                break;
            case STATE_PRESS_LONG_CLICK:
                //结束长按时，还原动画
                resetRecordAnim();

                mState = STATE_RELEASE_LONG_CLICK;
                //移除录制视频的Runnable
                removeCallbacks(mRecordRunnable);
                //录制结束
                recordStop(false);
                break;
        }

        //置空当前状态
        mState = STATE_NULL;
    }

    /**
     * 当前能否录视频
     */
    public void setIsRecording(boolean isRecording) {
        mIsRecording = isRecording;
    }

    private class LongPressRunnable implements Runnable {
        @Override
        public void run() {
            if (!mHasWindowFocus) {
                removeCallbacks(mRecordRunnable);
                resetRecordAnim();
                mState = STATE_NULL;
                return;
            }

            //如果按下后经过500ms则会修改当前状态为长按状态
            mState = STATE_PRESS_LONG_CLICK;

            //启动按钮动画，外圆变大，内圆缩小
            startAnimation(
                    mButtonOutsideRadius,
                    mButtonOutsideRadius + mOutsideAddSize,
                    mButtonInsideRadius,
                    mButtonInsideRadius - mInsideReduceSize
            );
        }
    }

    private class RecordRunnable implements Runnable {
        @Override
        public void run() {
            mRecordAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mState == STATE_PRESS_LONG_CLICK) {
                        //更新录制进度
                        mProgress = (float) animation.getAnimatedValue();
                    }

                    invalidate();
                }
            });

            //如果一直长按到结束，则自动回调录制结束接口
            mRecordAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (mState == STATE_PRESS_LONG_CLICK) {
                        recordStop(true);
                    }
                }
            });

            mRecordAnimator.setInterpolator(new LinearInterpolator());
            mRecordAnimator.setDuration(mDuration);
            mRecordAnimator.start();
        }
    }

    /**
     * 录制结束
     * @param finish 是否录制满时间
     */
    private void recordStop(boolean finish) {
        mState = STATE_RELEASE_LONG_CLICK;

        if (mCaptureListener != null) {
            //录制时间小于1s时候则提示录制时间过短
            if (mRecordAnimator.getCurrentPlayTime() < 1500 && !finish) {
                mCaptureListener.recordShort(mRecordAnimator.getCurrentPlayTime());
            } else {
                if (finish) {
                    mCaptureListener.recordStop(mDuration);
                } else {
                    mCaptureListener.recordStop(mRecordAnimator.getCurrentPlayTime());
                }
            }
        }
    }

    private void resetRecordAnim() {
        //取消动画
        mRecordAnimator.cancel();
        //重制进度
        mProgress = 0;
        invalidate();

        //还原按钮初始状态动画
        startAnimation(
                mButtonOutsideRadius,
                mButtonRadius,
                mButtonInsideRadius,
                mButtonRadius * 0.75f
        );
    }

    //capture button outside and inside resize animation
    //按钮动画，录制视频时，外圆变大，内圆缩小
    private void startAnimation(float outsideStart, float outsideEnd, float insideStart, float insideEnd) {
        ValueAnimator outSideAnim = ValueAnimator.ofFloat(outsideStart, outsideEnd);
        ValueAnimator insideAnim = ValueAnimator.ofFloat(insideStart, insideEnd);
        //外圆
        outSideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mButtonOutsideRadius = (float) animation.getAnimatedValue();
                invalidate();
            }

        });

        //内圆
        insideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mButtonInsideRadius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        //当动画结束后启动录像Runnable并且回调录像开始接口
        outSideAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mState == STATE_PRESS_LONG_CLICK) {
                    if (mCaptureListener != null) {
                        mCaptureListener.recordStart();
                    }

                    post(mRecordRunnable);
                }
            }
        });

        outSideAnim.setDuration(100);
        insideAnim.setDuration(100);
        outSideAnim.start();
        insideAnim.start();
    }

    /**
     * set record duration
     * 设置最长录制时间
     */
    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    //设置回调接口
    public void setCaptureListener(CaptureListener captureListener) {
        this.mCaptureListener = captureListener;
    }

    //设置按钮功能（拍照和录像）
    public void setButtonState(int buttonState) {
        this.mButtonState = buttonState;
    }
}
