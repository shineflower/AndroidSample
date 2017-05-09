package com.jackie.sample.custom_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2016/2/26.
 * 为项目定制一个进度条，带有渐变的效果。
 */
public class HorizontalBoldProgressBar extends View {
    private Context mContext;
    private int mWidth;
    private int mHeight;
    private Paint mProgressPaint;
    private int mTotal  = 100;//默认一百
    private int mCurrent = 50;
    private Paint mTextPaint;

    public HorizontalBoldProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalBoldProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalBoldProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context){
        this.mContext = context;
        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setColor(mContext.getResources().getColor(R.color.start_color));
        mProgressPaint.setAntiAlias(true);//去锯齿

        mTextPaint = new Paint();
        mTextPaint.setStrokeWidth(DensityUtils.dip2px(mContext,1));
        mTextPaint.setTextSize(DensityUtils.dip2px(mContext,14));
        mTextPaint.setColor(mContext.getResources().getColor(R.color.gray));
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mTemp);
        animator.setDuration(700);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int count = (int)animation.getAnimatedValue();
                mCurrent = count;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth() - DensityUtils.dip2px(mContext, 60);
        mHeight = getHeight();

        float marginHeight = mHeight / 2.0f;

        mProgressPaint.setStrokeWidth(mHeight);

        float unitWidth = (float) mWidth / mTotal;

        int nowWidth = (int)(mCurrent * unitWidth);

        mProgressPaint.setShader(new LinearGradient(0, marginHeight, nowWidth, marginHeight, getResources().getColor(R.color.start_color), getResources().getColor(R.color.end_color), Shader.TileMode.REPEAT)); //从(0,0)到(0,height)的色彩渐变
        canvas.drawLine(DensityUtils.dip2px(mContext, marginHeight), marginHeight, nowWidth - marginHeight, marginHeight, mProgressPaint);

        String text = "" + mCurrent;
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), bounds);
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        /**
         * http://mikewang.blog.51cto.com/3826268/871765/
         * http://www.2cto.com/kf/201512/455655.html
         */
        canvas.drawText(text, 0, text.length(), nowWidth + DensityUtils.dip2px(mContext, 10), mHeight - 2, mTextPaint);
    }

    int mTemp = 0;
    public void setProgress(int total,int current){
        this.mTotal = total;
        this.mTemp = current;
        initAnimator();
    }
}
