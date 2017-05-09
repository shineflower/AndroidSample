package com.jackie.sample.custom_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.utils.DensityUtils;

/**
 * Created by Jackie on 2017/5/9
 */
public class ProgressView extends View {
    private Context mContext;
    private int mWidth;  //控件的宽度
    private int mHeight; //控件的高度
    private int mProgress = 50; //默认的是50%的刻度

    private Paint mBgPaint;
    private Paint mProgressPaint;

    private int mProgressLeft;
    private float mProgressTop;
    private int mProgressRight;
    private int mProgressBottom;
    private int mCurrent;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        initView();
    }

    public void initView(){
        mBgPaint = new Paint();
        mBgPaint.setColor(getResources().getColor(R.color.gray_progress));
//        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setAntiAlias(true); //去锯齿
        mBgPaint.setStrokeCap(Paint.Cap.BUTT);//圆形的笔触

        mProgressPaint = new Paint();
        mProgressPaint.setColor(getResources().getColor(R.color.task_blue));
//        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setAntiAlias(true);//去锯齿
        mProgressPaint.setStrokeCap(Paint.Cap.BUTT);//圆形的笔触
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = DensityUtils.dip2px(mContext, 300);

        //设置自定义view的宽度 LinearGradient(x1, y1, x2, y2) 四个参数代表的是线性渲染的基准线
        mProgressPaint.setShader(new LinearGradient(0, mProgressBottom, 0, mProgressTop, new int[]{ R.color.task_blue, Color.WHITE, R.color.task_blue }, null, Shader.TileMode.REPEAT));
        canvas.drawRect(0, 0, mWidth, mHeight, mBgPaint);

        getProgressLocation();//获取进度相关的坐标

        canvas.drawRect(mProgressLeft, mProgressTop, mProgressRight, mProgressBottom, mProgressPaint);
    }

    public void getProgressLocation(){
        float unitLength = mHeight / (float) 100;

        mProgressLeft = 0;
        mProgressTop = unitLength * (100 - mCurrent);
        mProgressRight = mWidth;
        mProgressBottom = mHeight;
    }

    public void setProgress(int progress){
        mProgress = progress;

        //得到进度相关的一个坐标
        initAnimator();
    }

    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, mProgress);
        animator.setDuration(900);
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
}
