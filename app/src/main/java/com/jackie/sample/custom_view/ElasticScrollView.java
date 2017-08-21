package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.jackie.sample.utils.DensityUtils;

public class ElasticScrollView extends ScrollView {
    private Context mContext;
    private boolean mCanScroll;
    private GestureDetector mGestureDetector;

    public ElasticScrollView(Context context) {
        this(context, null);
    }

    public ElasticScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ElasticScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }


    private void init(Context context) {
        mContext = context;
        mGestureDetector = new GestureDetector(new YScrollDetector());
        mCanScroll = true;
        setVerticalScrollBarEnabled(false);
    }

    private View inner;
    private float y;
    private Rect normal = new Rect();
    private boolean isCount = false;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }

    class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(mCanScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    mCanScroll = true;
                else
                    mCanScroll = false;

            if (mCanScroll) {
                if (e1.getAction() == MotionEvent.ACTION_DOWN) {
                    moveYY = 0;
                    startY = e1.getY();
                }
            }
            return mCanScroll;
        }
    }

    double startY, moveYY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            int action = ev.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (isNeedAnimation()) {
                        animation();
                        isCount = false;
                    }

                    if (startY < moveYY - DensityUtils.dp2px(mContext, 100)) {
                        if (onPullListener != null) {
                            onPullListener.onDownPull();
                        }
                    } else if (startY - moveYY > DensityUtils.dp2px(mContext, 100)) {
                        if (onPullListener != null) {
                            onPullListener.onUpPull();
                        }
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    final float preY = y;
                    float nowY = ev.getY();
                    int deltaY = (int) (preY - nowY);
                    if (!isCount) {
                        deltaY = 0;
                    }

                    y = nowY;
                    if (isNeedMove()) {
                        if (normal.isEmpty()) {
                            normal.set(inner.getLeft(), inner.getTop(),
                                    inner.getRight(), inner.getBottom());
                        }
                        inner.layout(inner.getLeft(), inner.getTop() - deltaY / 2,
                                inner.getRight(), inner.getBottom() - deltaY / 2);
                    }
                    isCount = true;
                    moveYY = ev.getY();
                default:
                    break;
            }
        }

        return true;
    }

    public void animation() {
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        ta.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);

        normal.setEmpty();
    }

    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

    public interface OnPullListener {
        void onDownPull();
        void onUpPull();
    }

    OnPullListener onPullListener = null;
    public void setOnPullListener(OnPullListener listener) {
        onPullListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP)
            mCanScroll = true;
        return mGestureDetector.onTouchEvent(ev);
    }
}

