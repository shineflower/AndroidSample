package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/10/31.
 */

public class PasswordView extends EditText {
    private Paint mBorderPaint;     //外框画笔
    private Paint mLinePaint;       //线的画笔
    private Paint mPasswordPaint;   //密码画笔
    private int mPasswordTextLength;  //输入密码的长度
    private int mWidth;
    private int mHeight;

    private static final int PASSWORD_LENGTH = 6;//密码的长度
    private static final int PASSWORD_RADIUS = 15;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        setFocusable(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(8);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#838B8B"));
        mLinePaint.setStrokeWidth(4);

        mPasswordPaint = new Paint();
        mPasswordPaint.setColor(Color.BLACK);
        mPasswordPaint.setStrokeWidth(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        drawRoundRect(canvas);
        drawLine(canvas);
        drawPassword(canvas);
    }

    /**
     * 绘制圆角矩形背景
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
//        canvas.drawRoundRect(0, 0, mWidth, mHeight, 12, 12, mBorderPaint);
//        4.4 API 21以下上面的写法会出现NoSuchMethodError异常，需要换成下面的写法
        canvas.drawRoundRect(new RectF(0, 0, mWidth, mHeight), 12, 12, mBorderPaint);
    }

    /**
     * 绘制分割线
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        for (int i = 1; i < PASSWORD_LENGTH; i++) {
            float x = mWidth * i / PASSWORD_LENGTH;
            canvas.drawLine(x, 12, x, mHeight - 12, mLinePaint);
        }
    }

    /**
     * 绘制密码
     * @param canvas
     */
    private void drawPassword(Canvas canvas) {
        float cx, cy = mHeight / 2;
        float half = mWidth / PASSWORD_LENGTH / 2;
        for (int i = 0; i < mPasswordTextLength; i++) {
            cx = mWidth * i / PASSWORD_LENGTH + half;
            canvas.drawCircle(cx, cy, PASSWORD_RADIUS, mPasswordPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        mPasswordTextLength = text.toString().length();

        if (mPasswordTextLength == PASSWORD_LENGTH) {
            Toast.makeText(getContext(), "您设置的密码为: " + text, Toast.LENGTH_SHORT).show();;
        }

        invalidate();
    }


    public void reset(){
        setText("");
        invalidate();
    }
}
