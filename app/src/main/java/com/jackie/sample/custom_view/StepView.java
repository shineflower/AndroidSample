package com.jackie.sample.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/6/26.
 * 进度
 */

public class StepView extends View {
    private int mTotalStep = 4;
    private int mCurrentStep = 2;

    private Paint mCompletePaint;
    private Paint mUnCompletePaint;
    private Paint mTransparentPaint;

    private int mCurrentPositionCircleColor = getResources().getColor(R.color.color_ff1f1f);
    private Paint mTextPaint;

    private int mCompleteColor = getResources().getColor(R.color.color_0971ce);
    private int mUnCompleteColor = getResources().getColor(R.color.color_ececec);
    private int mTextColor = getResources().getColor(android.R.color.white);

    private int mCompleteRadius;
    private int mUnCompleteRadius;

    private int mWidth;
    private int mHeight;
    private int mSectionLineWidth;
    private int mSectionLineHeight;
    private int mPaddingLeft;
    private int mPaddingTop;

    private int mOrientation;
    private float mBorder;

    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;
    private final int STYLE_COMMON = 0;
    private final int STYLE_RED = 1;
    private final int STYLE_AVE = 2;

    private int mStyle = STYLE_COMMON;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mCompleteRadius = DensityUtils.dp2px(context, 13);
        mUnCompleteRadius = DensityUtils.dp2px(context, 8);
        mSectionLineHeight = DensityUtils.dp2px(context, 2);
        mPaddingLeft = DensityUtils.dp2px(context, 30);
        mPaddingTop = DensityUtils.dp2px(context, 30);

        mBorder = DensityUtils.dp2px(context, 1);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOrientation = ta.getInt(R.styleable.StepView_orientation, HORIZONTAL);
        mSectionLineHeight = ta.getDimensionPixelSize(R.styleable.StepView_section_line_height, mSectionLineHeight);
        mStyle = ta.getInt(R.styleable.StepView_step_style, STYLE_COMMON);
        mTotalStep = ta.getInt(R.styleable.StepView_total_step, 3);
        mCurrentStep = ta.getInt(R.styleable.StepView_current_step, 1);
        mUnCompleteRadius = ta.getDimensionPixelSize(R.styleable.StepView_unComplete_radius, mUnCompleteRadius);
        mUnCompleteColor = ta.getColor(R.styleable.StepView_unComplete_color, mUnCompleteColor);
        ta.recycle();

        initView(context);
    }

    private void initView(Context context) {
        mCompletePaint = createPaint(mCompleteColor, Paint.Style.FILL, mSectionLineHeight);
        mUnCompletePaint = createPaint(mUnCompleteColor, Paint.Style.FILL, mSectionLineHeight);
        mTextPaint = createTextPaint(mTextColor, DensityUtils.sp2px(context, 10));
        mTransparentPaint = createPaint(mTextColor, Paint.Style.FILL, mSectionLineHeight);
    }

    private Paint createPaint(int paintColor, Paint.Style style, int width) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(paintColor);
        paint.setStyle(style);
        paint.setStrokeWidth(width);
        return paint;
    }

    private Paint createTextPaint(int paintColor, int textSize) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(paintColor);
        paint.setTextSize(textSize);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        switch (mOrientation) {
            case HORIZONTAL:
                drawHorizontal(canvas);
                break;
            case VERTICAL:
                drawVertical(canvas);
                break;
        }

        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas) {
        canvas.translate(0, mHeight / 2);

        //每条线段的线宽
        mSectionLineWidth = (mWidth - 2 * mPaddingLeft) / (mTotalStep - 1);

        for (int i = 0; i < mTotalStep - 1; i++) {
            if (i <= mCurrentStep - 2) {
                canvas.drawLine(mPaddingLeft + i * mSectionLineWidth, 0, mPaddingLeft + (i + 1) * mSectionLineWidth, 0, mCompletePaint);
            } else {
                canvas.drawLine(mPaddingLeft + i * mSectionLineWidth, 0, mPaddingLeft + (i + 1) * mSectionLineWidth, 0, mUnCompletePaint);
            }
        }

        for (int i = 0; i < mTotalStep; i++) {
            String text = String.valueOf(i + 1);
            float textWidth = mTextPaint.measureText(text);
            float textHeight = mTextPaint.descent() + mTextPaint.ascent();

            if (i < mCurrentStep - 1) {
                canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mUnCompleteRadius, mCompletePaint);

                //没有完成的圆圈中画上数字
                if (mStyle == STYLE_AVE) {
                    canvas.drawText(text, mPaddingLeft + i * mSectionLineWidth - textWidth / 2, 0 - textHeight / 2, mTextPaint);
                }
            } else if (i == mCurrentStep - 1) {
                if (mStyle == STYLE_RED) {
                    mCompletePaint.setColor(mCurrentPositionCircleColor);
                }

                if (mStyle == STYLE_AVE) {
                    canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mUnCompleteRadius, mCompletePaint);
                } else {
                    canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mCompleteRadius, mCompletePaint);
                }

                mCompletePaint.setColor(mCompleteColor);
                canvas.drawText(text, mPaddingLeft + i * mSectionLineWidth - textWidth / 2, 0 - textHeight / 2, mTextPaint);
            } else {
                canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mUnCompleteRadius, mUnCompletePaint);

                if (mStyle == STYLE_AVE) {
                    canvas.drawText(text, mPaddingLeft + i * mSectionLineWidth - textWidth / 2, 0 - textHeight / 2, mTextPaint);
                }
            }
        }
    }

    private void drawVertical(Canvas canvas) {
        canvas.translate(mWidth / 2, 0);

        //每条线段的高度
        mSectionLineHeight = (mHeight - 2 * mPaddingTop) / (mTotalStep - 1);

        for (int i = 0; i < mTotalStep - 1; i++) {
            if (i <= mCurrentStep - 2) {
                canvas.drawLine(0, mPaddingTop + i * mSectionLineHeight, 0, mPaddingTop + (i + 1) * mSectionLineHeight, mCompletePaint);
            } else {
                canvas.drawLine(0, mPaddingTop + i * mSectionLineHeight, 0, mPaddingTop + (i + 1) * mSectionLineHeight, mUnCompletePaint);
            }
        }

        for (int i = 0; i < mTotalStep; i++) {
            if (i < mCurrentStep) {
                canvas.drawCircle(0, mPaddingTop + i * mSectionLineHeight, mUnCompleteRadius, mCompletePaint);
            } else {
                canvas.drawCircle(0, mPaddingTop + i * mSectionLineHeight, mUnCompleteRadius, mUnCompletePaint);
            }
        }


        for (int i = 0; i < mTotalStep; i++) {
            canvas.drawCircle(0, mPaddingTop + i * mSectionLineHeight, mUnCompleteRadius - mBorder, mTransparentPaint);
        }
    }
}
