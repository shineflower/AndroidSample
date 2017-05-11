package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/11/18.
 * 仿芝麻信用雷达分布图
 */  
  
public class SesameCreditView extends View {  
    private Paint mPaint;  
    /** 
     * 圆心坐标 
     */  
    private float mCenterX, mCenterY;  
    /** 
     * 圆心半径 
     */  
    private float mRadius;  
    /** 
     * 绘制图标的大小 
     */  
    private float mIconWidth;  
  
    /** 
     * 多边形的边数 
     */  
    private static final int POLYGON_COUNT = 5;  
    /** 
     * 绘制文字的大小 
     */  
    private static final int TEXT_SIZE = 25;  
  
    /** 
     * 角度 
     */  
    private static final int DEGREE = 360 / POLYGON_COUNT;  
  
    private static final int ICON_TITLE_MARGIN = 20;  
  
    /** 
     * 各维度分值 
     */  
    private static final float[] SESAME_DATA = {170, 180, 160, 170, 180};  
    /** 
     * 数据最大值 
     */  
    private static final float SESAME_MAX_VALUE = 190;  
  
    private static final String[] SESAME_TITLE = { "身份特质", "履约能力", "信用历史", "人脉关系", "行为偏好" };  
    private static final int[] SESAME_IDS = { R.mipmap.ic_identity, R.mipmap.ic_performance, R.mipmap.ic_history, R.mipmap.ic_contacts, R.mipmap.ic_predilection };
  
    public SesameCreditView(Context context) {  
        this(context, null);  
    }  
  
    public SesameCreditView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    public SesameCreditView(Context context, AttributeSet attrs, int defStyleAttr) {  
        super(context, attrs, defStyleAttr);  
  
        initView();  
    }  
  
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
  
        int width = getMeasuredWidth();  
        int height = getMeasuredHeight();  
  
        mCenterX = width / 2;  
        mCenterY = height / 2;  
        mRadius = Math.min(mCenterX, mCenterY) * 2 / 3;  
  
        mIconWidth = mRadius / 3;  
    }  
  
    private void initView() {  
        mPaint = new Paint();  
        mPaint.setAntiAlias(true);  
        mPaint.setDither(true);  
        mPaint.setStrokeWidth(3);  
        mPaint.setColor(Color.WHITE);  
        mPaint.setTextSize(TEXT_SIZE);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
  
        drawPolygon(canvas);  
        drawIcon(canvas);  
        drawText(canvas);  
        drawRegion(canvas);  
        drawScore(canvas);  
    }  
  
    private void drawPolygon(Canvas canvas) {  
        /** 
         * 先绘制多边形的边 
         */  
        for (int i = 0; i < POLYGON_COUNT; i++) {  
            canvas.save();  
  
            //圆心正上方的点作为参考点  
//            float targetX = mRadius;  
            float targetX = Math.min(mCenterX, mCenterY);  
            float targetY = mCenterY - mRadius;  
            float distance = (float) (2 * mRadius * Math.sin(Math.toRadians(DEGREE / 2)));  
  
            float y = (distance * distance + mCenterY * mCenterY - targetY * targetY - mRadius * mRadius) / (2 * (mCenterY - targetY));  
            float x = (float) (Math.sqrt(distance * distance - (y - targetY) * (y - targetY)) + targetX);  
  
            canvas.rotate(DEGREE * i, mCenterX, mCenterY);  
            canvas.drawLine(targetX, targetY, x, y, mPaint);  
            canvas.drawLine(mCenterX, mCenterY, targetX, targetY, mPaint);  
            canvas.restore();  
        }  
    }  
  
    private void drawIcon(Canvas canvas) {  
        for (int i = 0; i < SESAME_IDS.length; i++) {  
            canvas.save();  
  
            //图片的宽度和高度设置为半径的1 / 3  
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), SESAME_IDS[i]);  
  
            //获取图标左上顶点的坐标  
            Point point = getIconPoint(i, 1);  
            int left = point.x;  
            int top = point.y;  
  
            int right = (int) (left + mIconWidth);  
            int bottom = (int) (top + mIconWidth);  
            Rect rect = new Rect(left, top, right, bottom);  
            canvas.drawBitmap(bitmap, null, rect, mPaint);  
            canvas.restore();  
        }  
    }  
  
    private void drawText(Canvas canvas) {  
        for (int i = 0; i < SESAME_TITLE.length; i++) {  
            canvas.save();  
  
            Point point = getIconPoint(i, 1);  
            int left = point.x;  
            int top = point.y;  
  
            int textLeft = (int) (left - (mPaint.measureText(SESAME_TITLE[i]) / 2 - mIconWidth / 2));  
            int textTop = (int) (top + mIconWidth + ICON_TITLE_MARGIN * 2);  
            canvas.drawText(SESAME_TITLE[i], textLeft, textTop, mPaint);  
            canvas.restore();  
        }  
    }  
  
    private void drawRegion(Canvas canvas) {  
        mPaint.setAlpha(120);  
  
        Path path = new Path();  
  
        canvas.save();  
  
        for (int i = 0; i < SESAME_DATA.length; i++) {  
            float percent = SESAME_DATA[i] / SESAME_MAX_VALUE;  
  
            Point point = getVertexPoint(i, percent);  
            int x = point.x;  
            int y = point.y;  
            if (i == 0) {  
                path.moveTo(x, y);  
            } else {  
                path.lineTo(x, y);  
            }  
        }  
  
        path.close();  
        mPaint.setStyle(Paint.Style.STROKE);  
        canvas.drawPath(path, mPaint);  
  
        //绘制填充区域  
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);  
        canvas.drawPath(path, mPaint);  
  
        canvas.restore();  
    }  
  
    private void drawScore(Canvas canvas) {  
        canvas.save();  
  
        mPaint.setTextSize(150);  
        int width = (int) mPaint.measureText("860");  
        canvas.drawText("860", mCenterX - width / 2, mCenterY + ICON_TITLE_MARGIN, mPaint);  
  
        canvas.restore();  
    }  
  
    /**I 
     * 获取多边形的顶点 
     * @return 
     */  
    private Point getVertexPoint(int position, float percent) {  
        /**I 
         * 计算多边形每个顶点的坐标 
         * degree = DEGREE = 360 / POLYGON_COUNT; 
         * 0  mCenterX + mRadius * sin(0 * degree)      mCenterY - mRadius * cos(0 * degree) 
         * 1  mCenterX + mRadius * sin(1 * degree)      mCenterY - mRadius * cos(1* degree) 
         * 2  mCenterX + mRadius * sin(2 * degree)       mCenterY - mRadius * cos(2*degree) 
         *.... 
         */  
        int vertexX = (int) (mCenterX + mRadius * Math.sin(Math.toRadians(position * DEGREE)) * percent);  
        int vertexY = (int) (mCenterY - mRadius * Math.cos(Math.toRadians(position * DEGREE)) * percent);  
  
        return new Point(vertexX, vertexY);  
    }  
  
    /** 
     * 获取图标的位置 
     * @param position 
     * @return 
     */  
    private Point getIconPoint(int position, float percent) {  
        Point point = getVertexPoint(position, percent);  
        int vertexX = point.x;  
        int vertexY = point.y;  
  
        int left = 0;  
        int top = 0;  
        if (position == 0) {  
            left = (int) (vertexX - mIconWidth / 2);  
            /** 
             * 这里为了让文字能够显示下，自己手动设置 ICON_TITLE_MARGIN * 3 
             * 实际开发中需要自己是调试 
             * 同理，下面的ICON_TITLE_MARGIN都是为了保证图标和雷达分布图之间留有空隙 
             */  
            top = (int) (vertexY - mIconWidth - ICON_TITLE_MARGIN * 3);  
        } else if (position == 1) {  
            left = vertexX + ICON_TITLE_MARGIN;  
            top = (int) (vertexY - mIconWidth / 2);  
        } else if (position == 2) {  
            left = vertexX;  
            top = vertexY;  
        } else if (position == 3) {  
            left = (int) (vertexX - mIconWidth);  
            top = vertexY;  
        } else if (position == 4) {  
            left = (int) (vertexX - mIconWidth - ICON_TITLE_MARGIN);  
            top = (int) (vertexY - mIconWidth / 2);  
        }  
  
        return new Point(left, top);  
    }  
}  