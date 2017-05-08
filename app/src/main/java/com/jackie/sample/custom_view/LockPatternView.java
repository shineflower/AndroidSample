package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2015/12/24.
 * 图案解锁
 */
public class LockPatternView extends View {
    /**
     * 圆的画笔
     */
    private Paint mCirclePaint;
    /**
     * 线的画笔
     */
    private Paint mLinePaint;
    /**
     * 圆心数组
     */
    private PointView[][] mPointViewArray = new PointView[3][3];
    /**
     * 保存选中点的集合
     */
    private List<PointView> mSelectedPointViewList;


    /**
     * 解锁图案的边长
     */
    private int mPatternWidth;

    /**
     * 图案监听器
     */
    private OnPatternChangeListener mOnPatternChangeListener;

    /**
     * 半径
     */
    private float mRadius;

    /**
     * 每个圆圈的下标
     */
    private int mIndex = 1;

    /**
     * 第一个点是否选中
     */
    private boolean mIsSelected;
    /**
     * 是否绘制结束
     */
    private boolean mIsFinished;

    /**
     * 正在滑动并且没有任何点选中
     */
    private boolean mIsMovingWithoutCircle = false;

    private float mCurrentX, mCurrentY;

    /**
     * 正常状态的颜色
     */
    private static final int NORMAL_COLOR = 0xFF70DBDB;
    /**
     * 选中状态的颜色
     */
    private static final int SELECTED_COLOR = 0xFF979797;

    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(NORMAL_COLOR);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeWidth(20);
        mLinePaint.setColor(SELECTED_COLOR);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

        mSelectedPointViewList = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //取屏幕长和宽中的较小值作为图案的边长
        mPatternWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mPatternWidth, mPatternWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        drawCircle(canvas);

        //将选中的圆重新绘制一遍，将选中的点和未选中的点区别开来
        for (PointView pointView : mSelectedPointViewList) {
            mCirclePaint.setColor(SELECTED_COLOR);
            canvas.drawCircle(pointView.x, pointView.y, mRadius, mCirclePaint);
            mCirclePaint.setColor(NORMAL_COLOR);  //每重新绘制一个,将画笔的颜色重置,保证不会影响其他圆的绘制
        }

        //点与点画线
        if (mSelectedPointViewList.size() > 0) {
            Point pointViewA = mSelectedPointViewList.get(0);  //第一个选中的点为A点
            for (int i = 0; i < mSelectedPointViewList.size(); i++) {
                Point pointViewB = mSelectedPointViewList.get(i);  //其他依次遍历出来的点为B点
                drawLine(canvas, pointViewA, pointViewB);
                pointViewA = pointViewB;
            }

            //点与鼠标当前位置绘制轨迹
            if (mIsMovingWithoutCircle & !mIsFinished) {
                drawLine(canvas, pointViewA, new PointView((int)mCurrentX, (int)mCurrentY));
            }
        }

        super.onDraw(canvas);
    }

    /**
     * 画圆
     * @param canvas  画布
     */
    private void drawCircle(Canvas canvas) {
        //初始化点的位置
        for (int i = 0; i < mPointViewArray.length; i++) {
            for (int j = 0; j < mPointViewArray.length; j++) {
                //圆心的坐标
                int cx = mPatternWidth / 4 * (j + 1);
                int cy = mPatternWidth / 4 * (i + 1);

                //将圆心放在一个点数组中
                PointView pointView = new PointView(cx, cy);
                pointView.setIndex(mIndex);
                mPointViewArray[i][j] = pointView;
                canvas.drawCircle(cx, cy, mRadius, mCirclePaint);
                mIndex++;
            }
        }

        mIndex = 1;
    }

    /**
     * 画线
     * @param canvas  画布
     * @param pointA  第一个点
     * @param pointB  第二个点
     */
    private void drawLine(Canvas canvas, Point pointA, Point pointB) {
        canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, mLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrentX = event.getX();
        mCurrentY = event.getY();
        PointView selectedPointView = null;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //重新绘制
                if (mOnPatternChangeListener != null) {
                    mOnPatternChangeListener.onPatternStarted(true);
                }

                mSelectedPointViewList.clear();
                mIsFinished = false;

                selectedPointView = checkSelectPoint();

                if (selectedPointView != null) {
                    //第一次按下的位置在圆内，被选中
                    mIsSelected = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsSelected) {
                    selectedPointView = checkSelectPoint();
                }

                if (selectedPointView == null) {
                    mIsMovingWithoutCircle = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsFinished = true;
                mIsSelected = false;
                break;
        }

        //将选中的点收集起来
        if (!mIsFinished && mIsSelected && selectedPointView != null) {
            if (!mSelectedPointViewList.contains(selectedPointView)) {
                mSelectedPointViewList.add(selectedPointView);
            }
        }

        if (mIsFinished) {
            if (mSelectedPointViewList.size() == 1) {
                mSelectedPointViewList.clear();
            } else if (mSelectedPointViewList.size() < 5 && mSelectedPointViewList.size() > 0) {
                //绘制错误
                if (mOnPatternChangeListener != null) {
                    mOnPatternChangeListener.onPatternChange(null);
                }
            } else {
                //绘制成功
                String patternPassword = "";
                if (mOnPatternChangeListener != null) {
                    for (PointView pointView : mSelectedPointViewList) {
                        patternPassword += pointView.getIndex();
                    }

                    if (!TextUtils.isEmpty(patternPassword)) {
                        mOnPatternChangeListener.onPatternChange(patternPassword);
                    }
                }
            }
        }

        invalidate();
        return true;
    }

    /**
     * 判断当前按下的位置是否在圆心数组中
     * @return 返回选中的点
     */
    private PointView checkSelectPoint() {
        for (int i = 0; i < mPointViewArray.length; i++) {
            for (int j = 0; j < mPointViewArray.length; j++) {
                PointView pointView = mPointViewArray[i][j];
                if (isWithinCircle(mCurrentX, mCurrentY, pointView.x, pointView.y, mRadius)) {
                    return pointView;
                }
            }
        }

        return null;
    }

    /**
     * 判断点是否在圆内
     * @param x      点X轴坐标
     * @param y      点Y轴坐标
     * @param cx     圆心X坐标
     * @param cy     圆心Y坐标
     * @param radius 半径
     * @return       true表示在圆内，false表示在圆外
     */
    private boolean isWithinCircle(float x, float y, float cx, float cy, float radius) {
        //如果点和圆心的距离小于半径，则证明点在圆内
        if (Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y- cy, 2)) <= radius) {
           return true;
        }

        return false;
    }

    /**
     * 设置图案监听器
     */
    public void setOnPatternChangeListener(OnPatternChangeListener onPatternChangeListener) {
        if (onPatternChangeListener != null) {
            this.mOnPatternChangeListener = onPatternChangeListener;
        }
    }

    /**
     * 图案监听器
     */
    public interface OnPatternChangeListener {
        /**
         * 图案改变
         * @param patternPassword 图案密码
         */
        void onPatternChange(String patternPassword);

        /**
         * 图案是否重新绘制
         * @param isStarted 重新绘制
         */
        void onPatternStarted(boolean isStarted);
    }
}