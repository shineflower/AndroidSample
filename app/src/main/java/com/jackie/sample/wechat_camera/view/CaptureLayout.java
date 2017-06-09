package com.jackie.sample.wechat_camera.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.wechat_camera.listener.CaptureListener;
import com.jackie.sample.wechat_camera.listener.ReturnListener;
import com.jackie.sample.wechat_camera.listener.TypeListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Jackie on 2017/6/8.
 * 集成各个控件的布局
 */

public class CaptureLayout extends RelativeLayout {
    private Context mContext;

    //拍照按钮监听
    private CaptureListener mCaptureListener;
    //拍照或录制后结果按钮监听
    private TypeListener mTypeListener;
    //退出按钮监听
    private ReturnListener mReturnListener;

    private CaptureButton mBtnCapture;
    private TypeButton mBtnCancel;
    private TypeButton mBtnConfirm;
    private ReturnButton mBtnReturn;

    private TextView mTvTip;

    private int mWidth;
    private int mHeight;
    private int mButtonSize;

    public void setCaptureListener(CaptureListener captureListener) {
        this.mCaptureListener = captureListener;
    }

    public void setTypeListener(TypeListener typeListener) {
        this.mTypeListener = typeListener;
    }

    public void setReturnListener(ReturnListener returnListener) {
        this.mReturnListener = returnListener;
    }

    public CaptureLayout(Context context) {
        this(context, null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mWidth = ScreenUtils.getScreenWidth(context);
        } else {
            mWidth = ScreenUtils.getScreenWidth(context) / 2;
        }

        mButtonSize = (int) (mWidth / 5.0f);

        mHeight = mButtonSize + (mButtonSize / 5) * 2 + 100;

        initView();
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(0xffff0000);
        super.onDraw(canvas);
    }

    private void initView () {
        setWillNotDraw(false);

        //capture
        mBtnCapture = new CaptureButton(mContext, mButtonSize);
        LayoutParams captureParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        captureParams.addRule(CENTER_IN_PARENT, TRUE);
        captureParams.setMargins(0, 152, 0, 0);
        mBtnCapture.setLayoutParams(captureParams);
        mBtnCapture.setDuration(5000);

        mBtnCapture.setCaptureListener(new CaptureListener() {
            @Override
            public void takePicture() {
                if (mCaptureListener != null) {
                    mCaptureListener.takePicture();
                }
            }

            @Override
            public void recordShort(long time) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordShort(time);
                }

                startTipAlphaAnimation();
            }

            @Override
            public void recordStart() {
                if (mCaptureListener != null) {
                    mCaptureListener.recordStart();
                }

                startTipAlphaAnimation();
            }

            @Override
            public void recordStop(long time) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordStop(time);
                }

                startTipAlphaAnimation();
                startTypeTranslationAnimation();
            }

            @Override
            public void recordZoom(float zoom) {
                if (mCaptureListener != null) {
                    mCaptureListener.recordZoom(zoom);
                }
            }
        });

        //cancel
        mBtnCancel = new TypeButton(getContext(), TypeButton.TYPE_CANCEL, mButtonSize);
        LayoutParams cancelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        cancelParams.addRule(CENTER_VERTICAL, TRUE);
        cancelParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        cancelParams.setMargins((mWidth / 4) - mButtonSize / 2, 0, 0, 0);
        mBtnCancel.setLayoutParams(cancelParams);

        mBtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeListener != null) {
                    mTypeListener.cancel();
                }

                startTipAlphaAnimation();

                mBtnCancel.setVisibility(INVISIBLE);
                mBtnConfirm.setVisibility(INVISIBLE);
                mBtnCapture.setVisibility(VISIBLE);
                mBtnReturn.setVisibility(VISIBLE);
            }
        });


        //confirm
        mBtnConfirm = new TypeButton(getContext(), TypeButton.TYPE_CONFIRM, mButtonSize);
        LayoutParams confirmParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        confirmParams.addRule(CENTER_VERTICAL, TRUE);
        confirmParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        confirmParams.setMargins(0, 0, (mWidth / 4) - mButtonSize / 2, 0);
        mBtnConfirm.setLayoutParams(confirmParams);

        mBtnConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTypeListener != null) {
                    mTypeListener.confirm();
                }

                startTipAlphaAnimation();

                mBtnCancel.setVisibility(INVISIBLE);
                mBtnConfirm.setVisibility(INVISIBLE);
                mBtnCapture.setVisibility(VISIBLE);
                mBtnReturn.setVisibility(VISIBLE);
            }
        });

        //return
        mBtnReturn = new ReturnButton(getContext(), mButtonSize / 2);
        LayoutParams returnParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        returnParams.addRule(CENTER_VERTICAL, TRUE);
        returnParams.setMargins(mWidth / 6, 0, 0, 0);
        mBtnReturn.setLayoutParams(returnParams);

        mBtnReturn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReturnListener != null) {
                    mReturnListener.onReturn();
                }
            }
        });

        //tip
        mTvTip = new TextView(getContext());
        LayoutParams txtParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        txtParam.setMargins(0, 0, 0, 0);
        mTvTip.setText("轻触拍照，长按摄像");
        mTvTip.setTextColor(0xFFFFFFFF);
        mTvTip.setGravity(Gravity.CENTER);
        mTvTip.setLayoutParams(txtParam);

        this.addView(mBtnCapture);
        this.addView(mBtnConfirm);
        this.addView(mBtnCancel);
        this.addView(mBtnReturn);
        this.addView(mTvTip);
    }

    public void initEvent() {
        //默认TypeButton为隐藏
        mBtnCancel.setVisibility(INVISIBLE);
        mBtnConfirm.setVisibility(INVISIBLE);
    }

    private boolean mIsFirst = true;
    public void startTipAlphaAnimation() {
        if (mIsFirst) {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTvTip, "alpha", 1f, 0f);
            objectAnimator.setDuration(500);
            objectAnimator.start();
            mIsFirst = false;
        }
    }

    public void startTypeTranslationAnimation() {
        //拍照录制结果后的动画
        mBtnCapture.setVisibility(INVISIBLE);
        mBtnReturn.setVisibility(INVISIBLE);

        mBtnCancel.setVisibility(VISIBLE);
        mBtnConfirm.setVisibility(VISIBLE);
        mBtnCancel.setClickable(false);
        mBtnConfirm.setClickable(false);

        ObjectAnimator cancelAnimator = ObjectAnimator.ofFloat(mBtnCancel, "translationX", mWidth / 4, 0);
        cancelAnimator.setDuration(200);
        cancelAnimator.start();

        ObjectAnimator confirmAnimator = ObjectAnimator.ofFloat(mBtnConfirm, "translationX", -mWidth / 4, 0);
        confirmAnimator.setDuration(200);
        confirmAnimator.start();

        confirmAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBtnCancel.setClickable(true);
                mBtnConfirm.setClickable(true);
            }
        });
    }

    public void setTextWithAnimation(String tip) {
        mTvTip.setText(tip);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTvTip, "alpha", 0f, 1f, 1f, 0f);
        objectAnimator.setDuration(2500);
        objectAnimator.start();
    }

    public void setDuration(int duration) {
        mBtnCapture.setDuration(duration);
    }

    public void setIsRecording(boolean isRecording) {
        mBtnCapture.setIsRecording(isRecording);
    }

    public void setButtonState(int state) {
        mBtnCapture.setButtonState(state);
    }
}
