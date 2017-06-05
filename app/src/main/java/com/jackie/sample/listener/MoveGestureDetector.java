package com.jackie.sample.listener;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by Jackie on 2017/6/5.
 */

public class MoveGestureDetector extends BaseGestureDetector {
    private PointF mCurrentPoint;
    private PointF mPreviousPoint;
    //仅仅为了减少创建内存
//    private PointF mDeltaPointer = new PointF();

    //用于记录最终结果，并返回
    private PointF mExternalPoint = new PointF();

    private OnMoveGestureListener mOnMoveGestureListener;

    public interface OnMoveGestureListener {
        boolean onMoveBegin(MoveGestureDetector detector);
        boolean onMove(MoveGestureDetector detector);
        void onMoveEnd(MoveGestureDetector detector);
    }

    public MoveGestureDetector(Context context, OnMoveGestureListener onMoveGestureListener) {
        super(context);

        this.mOnMoveGestureListener = onMoveGestureListener;
    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                resetState();  //防止没有接收到CANCEL OR UP，保险起见
                mPreviousMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(mPreviousMotionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureInProgress = mOnMoveGestureListener.onMoveBegin(this);
                break;
        }
    }

    @Override
    protected void handleInProgressEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mOnMoveGestureListener.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);

                if (mOnMoveGestureListener.onMove(this)) {
                    mPreviousMotionEvent.recycle();
                    mPreviousMotionEvent = MotionEvent.obtain(event);
                }
                break;
        }
    }

    @Override
    protected void updateStateByEvent(MotionEvent event) {
        MotionEvent previousMotionEvent = mPreviousMotionEvent;

        mPreviousPoint = calculateFocalPoint(previousMotionEvent);
        mCurrentPoint = calculateFocalPoint(event);

        boolean skipMoveEvent = previousMotionEvent.getPointerCount() != event.getPointerCount();

        mExternalPoint.x = skipMoveEvent ? 0 : mCurrentPoint.x - mPreviousPoint.x;
        mExternalPoint.y = skipMoveEvent ? 0 : mCurrentPoint.y - mPreviousPoint.y;
    }

    /**
     * 根据event计算多指中心点
     * @param event
     * @return
     */
    private PointF calculateFocalPoint(MotionEvent event) {
        int count = event.getPointerCount();

        float x = 0, y = 0;
        for (int i = 0; i < count; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= count;
        y /= count;

        return new PointF(x, y);
    }

    public float getMoveX() {
        return mExternalPoint.x;
    }

    public float getMoveY() {
        return mExternalPoint.y;
    }

    public static class SimpleMoveGestureDetector implements OnMoveGestureListener {

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector detector) {
            return false;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector) {

        }
    }
}
