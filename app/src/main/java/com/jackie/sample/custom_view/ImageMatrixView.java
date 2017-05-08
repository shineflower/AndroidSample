package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2016/2/26.
 */
public class ImageMatrixView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;

    public ImageMatrixView(Context context) {
        this(context, null);
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        setImageMatrix(new Matrix());
    }

    public void setImageMatrix(Matrix matrix) {
        this.mMatrix = matrix;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null); //原图
        canvas.drawBitmap(mBitmap, mMatrix, null);  //对比图
    }
}
