package com.jackie.sample.custom_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.utils.DensityUtils;

import java.util.Random;

/**
 * created by Jackie on 2017/5/9
 * 斜线进度条
 */
public class ObliqueProgressBar extends View {
    private Context mContext;
    private Paint mPaint;
//    private float mProgress;

    public ObliqueProgressBar(Context context) {
        this(context, null);
    }

    public ObliqueProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObliqueProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCurrent == 0) {
            return;
        }

        //碎片雨
        mPaint.setColor(Color.parseColor("#a96ecb"));
        mPaint.setStrokeWidth(3);
        Random random = new Random();
        int sx, sy;
        for (int i = 0; i < 80; i++) {
            sx = random.nextInt(getWidth() + 10);
            sy = random.nextInt(getHeight() + 10);
//            canvas.drawLine(sx, sy, sx+random.nextInt(5), sy + random.nextInt(5), mPaint);
            canvas.drawCircle(sx, sy, random.nextInt(5) + 1, mPaint);
        }

        //进度
        mPaint.setColor(Color.parseColor("#6Aa96ecb"));
        mPaint.setStrokeWidth(DensityUtils.dp2px(mContext, 3));
        float x = mCurrent * (getWidth() / 100f);
        for (int i = 0; i < x; i += 30) {
            canvas.drawLine(i - 30, -10, i + 30, getHeight() + 10, mPaint);
        }
    }

    private int mTemp = 0;
    private int mCurrent;

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mTemp);
        animator.setDuration(4000);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int count = (int) animation.getAnimatedValue();
                mCurrent = count;
                invalidate();
            }
        });
    }

    public void setProgress(float progress) {
        this.mTemp = (int) progress;
        initAnimator();
    }
}