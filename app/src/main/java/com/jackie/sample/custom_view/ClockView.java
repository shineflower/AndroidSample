package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jackie.sample.utils.DensityUtils;

public class ClockView extends View{
    private Context mContext;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//
        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.colorPrimary)); //设置画笔的颜色
//        paint.setAntiAlias(false);//设置画笔的锯齿效果 true：隐藏锯齿  false：添加锯齿效果
//        paint.setAlpha(220);//设置画笔的alpha值
//        paint.setStyle(Paint.Style.STROKE);//设置画笔的样式，空心实心
//        paint.setStrokeWidth(2f);//设置画笔的空心边框宽度
//
//        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        //canvas 对象：   弧线(arcs)、填充颜色(argb和color)、 Bitmap、圆(circle和oval)、点(point)、线(line)、矩形(Rect)、图片(Picture)、圆角矩形 (RoundRect)、文本(text)、顶点(Vertices)、路径(path)

        //画图片，就是贴图
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable);
//        canvas.drawBitmap(bitmap, 250, 360, p);
        this.getWidth();
        this.getHeight();

        Log.e("message", "" + DensityUtils.px2dip(mContext, this.getWidth()) + "  " + DensityUtils.px2dip(mContext, this.getHeight()));//获取到的就是我们在布局里面设置的dp高度与宽度。

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.translate(this.getWidth() / (2.0f), this.getHeight() / 2.0f); //将位置移动画纸的坐标点:150, 150  (canvas.getWidth() / 2, 200)
        canvas.drawCircle(0, 0, this.getHeight() / 2.0f - 25, paint); //画圆圈 平移之后的坐标，所以可以在0, 0点进行绘图操作。

        //使用path绘制路径文字
        canvas.save();
        canvas.translate(-this.getWidth() / (4.0f), -this.getWidth() / (4.0f));
        Path path = new Path();
        path.addArc(new RectF(0, 0, this.getWidth() / (2.0f), this.getWidth() / (2.0f)), -(this.getWidth() / (2.0f) + 25), this.getWidth() / (2.0f) + 25);
        Paint citePaint = new Paint(paint);
        citePaint.setTextSize(30);
        citePaint.setStrokeWidth(2);
        canvas.drawTextOnPath("http://www.android777.com", path, 28, 0, citePaint);
        canvas.restore();

        Paint smallPaintPaint = new Paint(paint); //小刻度画笔对象
        smallPaintPaint.setStrokeWidth(1);

        float y = this.getHeight() / 2.0f - 25;
        float lineLengthLong  = (12 * 3f), lineLengthShort = (5 * 3f);
        int count = 60; //总刻度数

        for(int i = 0 ; i < count ; i++){
            if(i % 5 == 0) {
                canvas.drawLine(0f, y, 0, y + lineLengthLong, paint);
                canvas.drawText(String.valueOf(i / 5 + 1), -5f, y + lineLengthLong + 5, smallPaintPaint);
            } else {
                canvas.drawLine(0f, y, 0f, y + lineLengthShort, smallPaintPaint);
            }
            canvas.rotate(360 / count, 0f, 0f); //旋转画纸 也就是相当于旋转坐标点
        }

        //绘制指针
        smallPaintPaint.setColor(Color.GRAY);
        smallPaintPaint.setStrokeWidth(4);
        canvas.drawCircle(0, 0, 21, smallPaintPaint);
        smallPaintPaint.setStyle(Paint.Style.FILL);
        smallPaintPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 18, smallPaintPaint);
        canvas.drawLine(0, 10, 0, -this.getWidth() / (4.0f) - 2, paint);
    }
}
