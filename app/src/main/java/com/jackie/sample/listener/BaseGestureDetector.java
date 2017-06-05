package com.jackie.sample.listener;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Jackie on 2017/6/5.
 * 手势检测
 */

public abstract class  BaseGestureDetector {
    protected Context mContext;

    protected boolean mGestureInProgress;

    protected MotionEvent mPreviousMotionEvent;
    protected MotionEvent mCurrentMotionEvent;

    protected abstract void handleStartProgressEvent(MotionEvent event);
    protected abstract void handleInProgressEvent(MotionEvent event);
    protected abstract void updateStateByEvent(MotionEvent event);

    public BaseGestureDetector(Context context) {
        this.mContext = context;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!mGestureInProgress) {
            handleStartProgressEvent(event);
        } else {
            handleInProgressEvent(event);
        }

        return true;
    }

    protected void resetState() {
        if (mPreviousMotionEvent != null) {
            mPreviousMotionEvent.recycle();
            mPreviousMotionEvent = null;
        }

        if (mCurrentMotionEvent != null) {
            mCurrentMotionEvent.recycle();
            mCurrentMotionEvent = null;
        }

        mGestureInProgress = false;
    }
}
