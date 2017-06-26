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
    private Context mContext;

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

    private float mCompleteRadius;
    private float mUnCompleteRadius;

    private int mWidth;
    private int mHeight;
    private int mSectionLineWidth;
    private int mSectionLineHeight;
    private int mPaddingLeft;
    private int mPaddingTop;

    private final int HORIZONTAL = 0;
    private final int VERTICAL = 1;

    private int mOrientation;
    private float mBorder;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        mOrientation = ta.getInt(R.styleable.StepView_orientation, HORIZONTAL);
        ta.recycle();

        initView();
    }

    private void initView() {
        mCompleteRadius = DensityUtils.dip2px(mContext, 13);
        mUnCompleteRadius = DensityUtils.dip2px(mContext, 8);
        mSectionLineHeight = DensityUtils.dip2px(mContext, 2);
        mPaddingLeft = DensityUtils.dip2px(mContext, 30);
        mPaddingTop = DensityUtils.dip2px(mContext, 30);

        mBorder = DensityUtils.dip2px(mContext, 1);


        mCompletePaint = createPaint(mCompleteColor, Paint.Style.FILL, mSectionLineHeight);
        mUnCompletePaint = createPaint(mUnCompleteColor, Paint.Style.FILL, mSectionLineHeight);
        mTextPaint = createTextPaint(mTextColor, DensityUtils.sp2px(mContext, 10));
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
            if (i < mCurrentStep - 1) {
                canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mUnCompleteRadius, mCompletePaint);
            } else if (i == mCurrentStep - 1) {
                mCompletePaint.setColor(mCurrentPositionCircleColor);
                canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mCompleteRadius, mCompletePaint);
                mCompletePaint.setColor(mCompleteColor);
                String text = String.valueOf(mCurrentStep);
                float textWidth = mTextPaint.measureText(text);
                float textHeight = mTextPaint.descent() + mTextPaint.ascent();
                canvas.drawText(text, mPaddingLeft + i * mSectionLineWidth - textWidth / 2, 0 - textHeight / 2, mTextPaint);
            } else {
                canvas.drawCircle(mPaddingLeft + i * mSectionLineWidth, 0, mUnCompleteRadius, mUnCompletePaint);
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
