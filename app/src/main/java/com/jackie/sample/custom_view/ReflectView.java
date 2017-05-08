package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/2/29.
 */
public class ReflectView extends View {
    private Bitmap mSrcBitmap;
    private Bitmap mReflectBitmap;
    private Paint mPaint;

    public ReflectView(Context context) {
        this(context, null);
    }

    public ReflectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReflectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test4);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);

        mReflectBitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), matrix, false);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(new LinearGradient(0, mSrcBitmap.getHeight(), 0, mSrcBitmap.getHeight() * 1.4f, 0xdd000000, 0x10000000, Shader.TileMode.CLAMP));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(mSrcBitmap, 0, 0, null);
        canvas.drawBitmap(mReflectBitmap, 0, mReflectBitmap.getHeight(), null);
        canvas.drawRect(0, mReflectBitmap.getHeight(), mReflectBitmap.getWidth(), mReflectBitmap.getHeight() * 2, mPaint);
    }
}
