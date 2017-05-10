package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Jackie on 2017/5/10.
 * 时钟
 */

public class ClockTimeView extends View {
    private Paint mPaint;
    private int mWidth, mHeight, mCenterX, mCenterY, mRadius;
    private int mCount;
    private Path mPath;

    private static final int H_POINTER_WIDTH = 15;         //时针宽
    private static final int M_POINTER_WIDTH = 10;         //分针宽
    private static final int S_POINTER_WIDTH = 5;          //秒针宽
    private static final int SCALE_LINE_LENGTH = 15;       //刻度线长
    private static final int SCALE_LINE_WIDTH = 6;         //刻度线宽
    private static final int M_S_DEGREES_UNIT = 360 / 60;  //分、秒针每个数字走的角度
    private static final int H_DEGREES_UNIT = 360 / 12;    //时针每个数字走的角度

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            postInvalidate();
        }
    };

    public ClockTimeView(Context context) {
        this(context, null);
    }

    public ClockTimeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockTimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getWidth();
        mHeight = getHeight();

        mRadius = Math.min(mWidth, mHeight) / 2 - 20;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long startTime = System.currentTimeMillis();

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);  //外圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 12, mPaint);  //圆心

        /**
         * 绘制刻度
         */
        for (int i = 0; i < 60; i++) {
            canvas.save();
            canvas.rotate(M_S_DEGREES_UNIT * i, mCenterX, mCenterY);
            mPaint.setStrokeWidth(SCALE_LINE_WIDTH);
            canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY - mRadius + SCALE_LINE_LENGTH, mPaint);

            if (i % 5 == 0) {  //小时刻度和数字
                mPaint.setStrokeWidth(SCALE_LINE_WIDTH + 5);
                canvas.drawLine(mCenterX, mCenterY - mRadius, mCenterX, mCenterY - mRadius + SCALE_LINE_LENGTH + 10, mPaint);

                String num = i == 0 ? 12 + "" : i / 5 + "";
                float x = mCenterX - mPaint.measureText(num) / 2;
                float y = mCenterY - mRadius + SCALE_LINE_LENGTH + 30;
                mPaint.setTextSize(50);
                canvas.drawText(num, x, y + Math.abs(mPaint.ascent()), mPaint);
            }

            canvas.restore();
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        /**
         * 分针在最下面，最先绘制
         */
        canvas.save();
        float degrees = minute * M_S_DEGREES_UNIT;
        canvas.rotate(degrees, mCenterX, mCenterY);
        mPaint.setColor(Color.GREEN);
        mPath.reset();
        /**
         * Direction.CCW 逆时针生成
         * Direction.CW  顺时针生成
         */
        mPath.addRect(new RectF(mCenterX - M_POINTER_WIDTH, mCenterY - mRadius / 4 * 3, mCenterX + M_POINTER_WIDTH, mCenterY + mRadius / 5), Path.Direction.CW);
        mPath.quadTo(mCenterX, mCenterY - mRadius / 4 * 3 - 40, mCenterX + M_POINTER_WIDTH, mCenterY - mRadius / 4 * 3);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        /**
         * 时针在中间
         * 分针60分走360度; 时针1小时(即60分)走30度
         * mDegrees:hDegrees = 360:30 = 12:1
         * hDegrees = mDegrees / 12; 时针相对于分钟数所要走的角度
         */
        canvas.save();
        canvas.rotate(degrees / 12 + hour * H_DEGREES_UNIT, mCenterX, mCenterY);
        mPaint.setColor(Color.RED);
        mPath.reset();
        mPath.addRect(new RectF(mCenterX - H_POINTER_WIDTH, mCenterY - mRadius / 3 * 2, mCenterX + H_POINTER_WIDTH, mCenterY + mRadius / 5), Path.Direction.CW);
        mPath.quadTo(mCenterX, mCenterY - mRadius / 3 * 2 - 30, mCenterX + H_POINTER_WIDTH, mCenterY - mRadius / 3 * 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        /**
         * 秒针在最上面
         */
        canvas.save();
        canvas.rotate(second * M_S_DEGREES_UNIT, mCenterX, mCenterY);
        mPaint.setColor(Color.BLACK);
        mPath.reset();
        mPath.addRect(new RectF(mCenterX - S_POINTER_WIDTH, mCenterY - mRadius / 5 * 4, mCenterX + S_POINTER_WIDTH, mCenterY + mRadius / 5), Path.Direction.CW);
        mPath.quadTo(mCenterX, mCenterY - mRadius / 5 * 4 - 30, mCenterX + S_POINTER_WIDTH, mCenterY - mRadius / 5 * 4);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        long spentTime = SystemClock.uptimeMillis() - startTime;
//        System.out.println("间隔时间" + spentTime);
        if (mCount < 5) { //12的位置开始绘制时位置不对，刷新一次后就正常，这里让其快速刷新几次
            mHandler.sendEmptyMessage(0);
            mCount++;
        } else {
            mHandler.sendEmptyMessageDelayed(0, 1000 - spentTime);
        }
    }
}
