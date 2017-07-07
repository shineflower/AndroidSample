package com.jackie.sample.custom_view;

import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.jackie.sample.R;
import com.jackie.sample.bean.Point;
import com.jackie.sample.utils.DensityUtils;
import com.jackie.sample.utils.ScreenUtils;

/**
 * Created by Jackie on 2017/5/9
 */
public class ProgressLinearLayout extends LinearLayout {
    private Context mContext;
    private int mProgress1, mProgress2, mProgress3, mProgress4;
    private ProgressView mProgressView1, mProgressView2, mProgressView3, mProgressView4;

    private int[] mPosition1 = new int[2];
    private int[] mPosition2 = new int[2];
    private int[] mPosition3 = new int[2];
    private int[] mPosition4 = new int[2];

    //圆形点的大小
    private int mDotSize;

    //中间点的坐标
    float mCenterX1, mCenterY1, mCenterX2, mCenterY2, mCenterX3, mCenterY3, mCenterX4, mCenterY4;

    private Paint mDotPaint;
    private Paint mLinePaint;

    private Point mPoint1, mAnimatedPoint1;
    private Point mPoint2, mAnimatedPoint2;
    private Point mPoint3, mAnimatedPoint3;
    private Point mPoint4;

    private static final int MSG_ANIMATOR_START = 0x110;

    public ProgressLinearLayout(Context context) {
       this(context, null);
    }

    public ProgressLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initView();
    }

    public void initView(){
        setWillNotDraw(false);

        mDotSize = DensityUtils.dp2px(mContext, 5);

        this.setOrientation(HORIZONTAL);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.progress_linear_layout, null);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, params);

        mProgressView1 = (ProgressView) findViewById(R.id.progress_1);
        mProgressView2 = (ProgressView) findViewById(R.id.progress_2);
        mProgressView3 = (ProgressView) findViewById(R.id.progress_3);
        mProgressView4 = (ProgressView) findViewById(R.id.progress_4);

        //在之前进行绘制操作
        mDotPaint = new Paint();
        mDotPaint.setColor(Color.WHITE);
        mDotPaint.setStyle(Paint.Style.STROKE);
        mDotPaint.setAntiAlias(true); //去锯齿
        mDotPaint.setStrokeCap(Paint.Cap.BUTT); //圆形的笔触
        mDotPaint.setStrokeWidth(mDotSize);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true); //去锯齿
        mLinePaint.setStrokeCap(Paint.Cap.BUTT); //圆形的笔触
        mLinePaint.setStrokeWidth(DensityUtils.dp2px(mContext, 2));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mAnimatedPoint1 != null) {
            canvas.drawCircle(mCenterX1, mCenterY1, mDotSize / 2, mDotPaint);
        }

        if (mAnimatedPoint1 != null && mPoint2 != null && mAnimatedPoint1.getX() <= mPoint2.getX()) { //还在途中
            canvas.drawLine(mCenterX1, mCenterY1, mAnimatedPoint1.getX(), mAnimatedPoint1.getY(), mLinePaint);
        }

        if (mAnimatedPoint2 != null && mPoint3 != null && mAnimatedPoint2.getX() <= mPoint3.getX()) {
            canvas.drawCircle(mCenterX2, mCenterY2, mDotSize / 2, mDotPaint);
            canvas.drawLine(mCenterX2, mCenterY2, mAnimatedPoint2.getX(), mAnimatedPoint2.getY(), mLinePaint);
        }

        if (mAnimatedPoint3 != null && mPoint4 != null && mAnimatedPoint3.getX() < mPoint4.getX()) {
            canvas.drawCircle(mCenterX3, mCenterY3, mDotSize / 2, mDotPaint);
            canvas.drawLine(mCenterX3, mCenterY3, mAnimatedPoint3.getX(), mAnimatedPoint3.getY(), mLinePaint);
        } else if (mAnimatedPoint3 != null && mPoint4 != null && mAnimatedPoint3.getX() == mPoint4.getX()) { //把第四个点画上
            canvas.drawCircle(mCenterX4, mCenterY4, mDotSize / 2, mDotPaint);
            canvas.drawCircle(mCenterX3, mCenterY3, mDotSize / 2, mDotPaint);
            canvas.drawLine(mCenterX3, mCenterY3, mAnimatedPoint3.getX(), mAnimatedPoint3.getY(), mLinePaint);
        }
    }

    public void setProgress(int progress1, int progress2, int progress3, int progress4) {
        this.mProgress1 = progress1;
        this.mProgress2 = progress2;
        this.mProgress3 = progress3;
        this.mProgress4 = progress4;

        mProgressView1.setProgress(progress1);
        mProgressView2.setProgress(progress2);
        mProgressView3.setProgress(progress3);
        mProgressView4.setProgress(progress4);

        calculateLocation();
    }

    //计算所有的点的坐标
    public void calculateLocation() {
        ViewTreeObserver viewTreeObserver = mProgressView4.getViewTreeObserver();

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                mProgressView1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mProgressView2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mProgressView3.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mProgressView4.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mProgressView1.getLocationOnScreen(mPosition1);
                mProgressView2.getLocationOnScreen(mPosition2);
                mProgressView3.getLocationOnScreen(mPosition3);
                mProgressView4.getLocationOnScreen(mPosition4);

                int statusBarHeight = ScreenUtils.getStatusBarHeight(mContext);
                int width = mProgressView1.getWidth();
                int height = mProgressView1.getHeight();

                //算所有的划线的点的坐标
                float unitLength = height / (float) 100;

                float progressTop1 = (unitLength * (100 - mProgress1));

                mCenterX1 = mPosition1[0] + width / 2;
                mCenterY1 = mPosition1[1]+ progressTop1 - statusBarHeight;

                float progressTop2 = (unitLength * (100 - mProgress2));
                mCenterX2 = mPosition2[0] + width / 2;
                mCenterY2 = mPosition2[1]+ progressTop2 - statusBarHeight ;

                float progressTop3 = (unitLength * (100 - mProgress3));
                mCenterX3 = mPosition3[0] + width / 2;
                mCenterY3 = mPosition3[1]+ progressTop3 - statusBarHeight ;

                float progressTop4 = (unitLength * (100 - mProgress4));
                mCenterX4 = mPosition4[0] + width / 2;
                mCenterY4 = mPosition4[1]+ progressTop4 - statusBarHeight; // - statusBarHeight

                //画点画线
                mPoint1 = new Point((int) mCenterX1, (int) mCenterY1);
                mPoint2 = new Point((int) mCenterX2, (int) mCenterY2);
                mPoint3 = new Point((int) mCenterX3, (int) mCenterY3);
                mPoint4 = new Point((int) mCenterX4, (int) mCenterY4);

                mHandler.sendEmptyMessageDelayed(MSG_ANIMATOR_START, 900);
            }
          });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_ANIMATOR_START:
                    ValueAnimator valueAnimator1 = ValueAnimator.ofObject(new PointEvaluator(), mPoint1, mPoint2);
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mAnimatedPoint1 = (Point) animation.getAnimatedValue();
                            invalidate();
                        }

                    });
                    valueAnimator1.setDuration(300);

                    ValueAnimator valueAnimator2 = ValueAnimator.ofObject(new PointEvaluator(), mPoint2, mPoint3);
                    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mAnimatedPoint2 = (Point) animation.getAnimatedValue();
                            invalidate();
                        }

                    });
                    valueAnimator2.setDuration(300);

                    ValueAnimator valueAnimator3 = ValueAnimator.ofObject(new PointEvaluator(), mPoint3, mPoint4);
                    valueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mAnimatedPoint3 = (Point) animation.getAnimatedValue();
                            invalidate();
                        }

                    });
                    valueAnimator3.setDuration(300);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(valueAnimator1).before(valueAnimator2);
                    animatorSet.play(valueAnimator2).before(valueAnimator3);
                    animatorSet.setDuration(1000);
                    animatorSet.start();
                    break;
            }
        }
    };

    public class PointEvaluator implements TypeEvaluator {
        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            Point startPoint = (Point) startValue;
            Point endPoint = (Point) endValue;
            float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
            float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
            Point point = new Point(x, y);
            return point;
        }
    }
}
