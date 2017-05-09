package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jackie on 2016/11/3 11:19
 * 一个水平的闪烁的TextView
 * http://blog.csdn.net/htybay/article/details/51444661
 */
public class FlashTextView extends TextView {
    private int mWidth = 0, mTranslate = 0;
    private Paint mPaint;
    private LinearGradient mLinearGradient = null;
    private Matrix mGradientMatrix = null;

    public FlashTextView(Context context) {
        this(context, null);
    }

    public FlashTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlashTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGradientMatrix != null){
            mTranslate += mWidth / 5;
            if(mTranslate > 2* mWidth){
                mTranslate = -mWidth;
            }

            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            /**
             * 一组是invalidate，另一组是postInvalidate，其中前者是在UI线程自身中使用，而后者在非UI线程中
             */
            postInvalidateDelayed(100); //100毫秒之后在非UI进程中重新绘制
        }
    }

    /**
     * 今天查看了下iew的 回调方法的执行顺序：
     * 10-12 07:41:21.040: V/(668): onAttachedToWindow
     * 10-12 07:41:21.040: V/(688): onFinishInflate   1
     * 10-12 07:41:21.100: V/(688): onMeasure         2
     * 10-12 07:41:21.160: V/(688): onSizeChanged     3
     * 10-12 07:41:21.160: V/(688): onLayout          4
     * 10-12 07:41:21.250: V/(688): onMeasure         5
     * 10-12 07:41:21.260: V/(688): onLayout          6
     * 10-12 07:41:21.541: V/(688): onMeasure         7
     * 10-12 07:41:21.541: V/(688): onLayout          8
     * 10-12 07:41:21.559: V/(688): onDraw            9
     * 10-12 07:41:21.599: V/(688): dispatchDraw      10
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if(mWidth == 0){
            mWidth = getMeasuredWidth();
            if(mWidth > 0){
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(0, 0, mWidth, 0, new int[] { Color.BLUE, 0xfffffff, Color.BLUE }, null, Shader.TileMode.CLAMP);

                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }
}
