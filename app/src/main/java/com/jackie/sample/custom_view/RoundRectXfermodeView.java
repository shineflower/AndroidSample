package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/2/27.
 * 圆角矩形
 */
public class RoundRectXfermodeView extends View {
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Paint mPaint;

    public RoundRectXfermodeView(Context context) {
        this(context, null);
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectXfermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);

        mDstBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mDstBitmap);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //dst
        canvas.drawCircle(300, 200, 150, mPaint);
//        canvas.drawRoundRect(0, 0, mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), 50, 50, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //src
        canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mDstBitmap, 0, 0, null);
    }
}
