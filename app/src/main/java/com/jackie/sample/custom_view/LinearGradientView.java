package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class LinearGradientView extends View {
    public LinearGradientView(Context context) {
        super(context);
    }

    public LinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearGradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth(); //以控件的宽高进行设置。
        int height = getHeight(); //控件的宽高进行设置。

        Paint paint = new Paint();
        paint.setShader(new LinearGradient(0, 0, 0, height, Color.BLUE, Color.WHITE, Shader.TileMode.REPEAT));//从(0, 0)到(0, height)的色彩渐变
        canvas.drawRect(0, 0, height, height , paint);//(0, 0)到(height, height)的一个矩形
    }

}
