package com.jackie.sample.custom_view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import static in.srain.cube.views.ptr.util.PtrLocalDisplay.dp2px;

/**
 * Created by Jackie on 2017/4/1.
 * 自定义圆形仪表盘View，适合根据数值显示不同等级范围的场景
 */

public class CircleRangeView extends View {
    private Context mContext;

    private CharSequence[] mRangeColorArray;
    private CharSequence[] mRangeTextArray;
    private CharSequence[] mRangeValueArray;

    private int mExtraColor;
    private int mBorderColor;
    private int mCursorColor;

    private int mRangeTextSize; //中间文本大小
    private int mExtraTextSize; //附加信息文本大小
    private int mBorderSize; //进度圆弧宽度

    private int mSection = 0;  //等分份数

    private int mSparkleWidth; // 指示标宽度
    private int mCalibrationWidth; // 刻度圆弧宽度

    private Paint mPaint;
    private RectF mRectFProgressArc;
    private RectF mRectFCalibrationFArc;
    private Rect mRectText;

    private int mBackgroundColor;

    private int mPadding;
    private float mLength;  //刻度顶部相对边缘的长度
    private int mRadius;    //画布边缘半径(去除padding后的半径)
    private int mStartAngle = 150; // 起始角度
    private int mSweepAngle = 240; // 绘制角度

    private float mCenterX, mCenterY;  //圆心坐标

    private boolean mIsAnimFinish = true;
    private String mCurrentValue;
    private float mAngleWhenAnim;

    private List<String> mExtraList = new ArrayList<>();

    public CircleRangeView(Context context) {
        this(context, null);
    }

    public CircleRangeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mRangeTextSize = DensityUtils.sp2px(context, 34);
        mExtraTextSize = DensityUtils.sp2px(context, 14);
        mBorderSize = DensityUtils.dp2px(context, 5);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRangeView);

        mRangeColorArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeColorArray);
        mRangeTextArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeTextArray);
        mRangeValueArray = typedArray.getTextArray(R.styleable.CircleRangeView_rangeValueArray);

        mExtraColor = ContextCompat.getColor(context, R.color.widget_extra_color);
        mBorderColor =  ContextCompat.getColor(context, R.color.widget_border_color);
        mCursorColor = ContextCompat.getColor(context, R.color.widget_cursor_color);

        mRangeTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleRangeView_rangeTextSize, mRangeTextSize);
        mExtraTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleRangeView_extraTextSize, mExtraTextSize);

        typedArray.recycle();

        if (mRangeColorArray == null || mRangeValueArray == null || mRangeTextArray == null) {
            throw new IllegalArgumentException("CircleRangeView : rangeColorArray、 rangeValueArray、rangeTextArray must be not null ");
        }

        if (mRangeColorArray.length != mRangeValueArray.length
                || mRangeColorArray.length != mRangeTextArray.length
                || mRangeValueArray.length != mRangeTextArray.length) {
            throw new IllegalArgumentException("arrays must be equal length");
        }

        this.mSection = mRangeColorArray.length;

        mSparkleWidth =  DensityUtils.dp2px(mContext, 15);
        mCalibrationWidth = DensityUtils.dp2px(mContext, 10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFProgressArc = new RectF();
        mRectFCalibrationFArc  = new RectF();
        mRectText = new Rect();

        mBackgroundColor = android.R.color.transparent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPadding = Math.max(Math.max(getPaddingLeft(), getPaddingTop()), Math.max(getPaddingRight(), getPaddingBottom()));
        setPadding(mPadding, mPadding, mPadding, mPadding);

        mLength = mPadding + mSparkleWidth / 2f + DensityUtils.dp2px(mContext, 12);

        int width = resolveSize(DensityUtils.dp2px(mContext, 220), widthMeasureSpec);
        mRadius = (width - mPadding * 2) / 2;

        setMeasuredDimension(width, width - DensityUtils.dp2px(mContext, 30));

        mCenterX = mCenterY = getMeasuredWidth() / 2f;

        mRectFProgressArc.set(mPadding + mSparkleWidth / 2f,
                mPadding + mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f,
                getMeasuredWidth() - mPadding - mSparkleWidth / 2f);

        mRectFCalibrationFArc.set(mLength + mCalibrationWidth / 2f,
                mLength + mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength - mCalibrationWidth / 2f,
                getMeasuredWidth() - mLength - mCalibrationWidth / 2f);

        mPaint.setTextSize(DensityUtils.sp2px(mContext, 10));
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(ContextCompat.getColor(getContext(), mBackgroundColor));

        /**
         * 画圆弧背景
         */
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderSize);
        mPaint.setColor(mBorderColor);
        canvas.drawArc(mRectFProgressArc, mStartAngle + 1, mSweepAngle - 2, false, mPaint);

        mPaint.setAlpha(255);

        /**
         * 画指示标
         */
        if (mIsAnimFinish) {
            float[] points = getCoordinatePoint(mRadius - mSparkleWidth / 2f, mStartAngle + calculateAngleWithValue(mCurrentValue));
            mPaint.setColor(mCursorColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(points[0], points[1], mSparkleWidth / 2f, mPaint);
        } else {
            float[] points = getCoordinatePoint(mRadius - mSparkleWidth / 2f, mAngleWhenAnim);
            mPaint.setColor(mCursorColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(points[0], points[1], mSparkleWidth / 2f, mPaint);
        }

        /**
         * 画等级圆弧
         */
        mPaint.setShader(null);;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(255);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(mCalibrationWidth);

        if (mRangeColorArray != null) {
            for (int i = 0; i < mRangeColorArray.length; i++) {
                mPaint.setColor(Color.parseColor(mRangeColorArray[i].toString()));
                float space = mSweepAngle / mSection;
                if (i == 0) {
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle + 3, space, false, mPaint);
                } else if (i == mRangeColorArray.length - 1) {
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle + (space * i), space, false, mPaint);
                } else {
                    canvas.drawArc(mRectFCalibrationFArc, mStartAngle + (space * i) + 3, space, false, mPaint);
                }
            }
        }

        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(null);
        mPaint.setAlpha(255);

        /**
         * 画等级对应值的文本(居中显示)
         */
        if (mRangeColorArray != null && mRangeValueArray != null && mRangeTextArray != null) {
            if (!TextUtils.isEmpty(mCurrentValue)) {
                int position = 0;

                for (int i = 0; i < mRangeValueArray.length; i++) {
                    if (mRangeValueArray[i].equals(mCurrentValue)) {
                        position = i;
                        break;
                    }
                }

                mPaint.setColor(Color.parseColor(mRangeColorArray[position].toString()));
                mPaint.setTextAlign(Paint.Align.CENTER);

                String text = mRangeTextArray[position].toString();
                if (text.length() <= 4) {
                    mPaint.setTextSize(mRangeTextSize);
                    canvas.drawText(text, mCenterX, mCenterY + dp2px(10), mPaint);
                } else {
                    mPaint.setTextSize(mRangeTextSize - 10);
                    String top = text.substring(0, 4);
                    String bottom = text.substring(4, text.length());

                    canvas.drawText(top, mCenterX, mCenterY, mPaint);
                    canvas.drawText(bottom, mCenterX, mCenterY +  dp2px(30), mPaint);
                }
            }
        }

        /**
         * 画附加信息
         */
        if (mExtraList != null && mExtraList.size() > 0) {
            mPaint.setAlpha(160);
            mPaint.setColor(mExtraColor);
            mPaint.setTextSize(mExtraTextSize);
            for (int i = 0; i < mExtraList.size(); i++) {
                canvas.drawText(mExtraList.get(i), mCenterX, mCenterY + DensityUtils.dp2px(mContext, 50) + i * DensityUtils.dp2px(mContext, 20), mPaint);
            }
        }
    }

    /**
     * 根据角度和半径进行三角函数计算坐标
     * @param radius
     * @param angle
     * @return
     */
    private float[] getCoordinatePoint(float radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    /**
     * 根据起始角度计算对应值应显示的角度大小
     */
    private float calculateAngleWithValue(String level) {

        int pos = -1;

        for (int j = 0; j < mRangeValueArray.length; j++) {
            if (mRangeValueArray[j].equals(level)) {
                pos = j;
                break;
            }
        }

        float degreePerSection = 1f * mSweepAngle / mSection;

        if (pos == -1) {
            return 0;
        } else if (pos == 0) {
            return degreePerSection / 2;
        } else {
            return pos * degreePerSection + degreePerSection / 2;
        }
    }

    /**
     * 设置值并播放动画
     * @param value 值
     */
    public void setValueWithAnim(String value) {
        setValueWithAnim(value, null);
    }

    /**
     * 设置值并播放动画
     * @param value  值
     * @param extras 底部附加信息
     */
    public void setValueWithAnim(String value, List<String> extras) {
        if (!mIsAnimFinish) {
            return;
        }

        this.mCurrentValue = value;
        this.mExtraList = extras;

        //计算最终值对应的角度，以扫过的角度的线性变化来播放动画
        float degree = calculateAngleWithValue(value);

        ValueAnimator degreeValueAnimator = ValueAnimator.ofFloat(mStartAngle, mStartAngle + degree);
        degreeValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAngleWhenAnim = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        long delay = 1500;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(delay).playTogether(degreeValueAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mIsAnimFinish = false;
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimFinish = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mIsAnimFinish = true;
            }
        });

        animatorSet.start();
    }
}
