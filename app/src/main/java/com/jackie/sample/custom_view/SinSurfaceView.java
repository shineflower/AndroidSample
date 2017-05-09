package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SinSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private SurfaceHolder mSurfaceHolder;
    private boolean mIsDrawing = false;  //绘图是否结束
    private Canvas mCanvas;
    private Path mPath;
    private int x, y;
    private Paint mPaint;

    public SinSurfaceView(Context context) {
        this(context, null);
    }

    public SinSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView(){
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        //设置画笔宽度
        mPaint.setStrokeWidth(1);
        //消除锯齿
        mPaint.setAntiAlias(true);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.getKeepScreenOn();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;

        mPath.moveTo(x, (int)(100 * Math.sin(x * 2 * Math.PI / 180) + 400));//开始点移动到起始点。

        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing && x < getWidth()){
            x += 1;
            y = (int)(100 * Math.sin(x * 2 * Math.PI / 180) + 400);
            mPath.lineTo(x, y);
        }

        draw();
    }

    private void draw(){
        try{
            mCanvas = mSurfaceHolder.lockCanvas(); //获取当前的绘图对象。
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
//            mCanvas.drawPoint(x, y, mPaint);
        } catch (Exception  e){

        } finally {
            if(mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);//异常之后也应该每次都将绘图的内容进行提交
            }
        }
    }
}
