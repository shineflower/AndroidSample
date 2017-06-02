package com.jackie.sample.custom_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jackie.sample.utils.CommonUtils;

/**
 * Created by Jackie on 2017/6/2.
 * 基于层不规则图像填充
 */

public class ColorImageBaseLayerView extends View {
    private LayerDrawable mLayerDrawable;

    public ColorImageBaseLayerView(Context context) {
        this(context, null);
    }

    public ColorImageBaseLayerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorImageBaseLayerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mLayerDrawable = (LayerDrawable) getBackground();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mLayerDrawable.getIntrinsicWidth(), mLayerDrawable.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = findDrawable(x, y);

                if (drawable != null) {
                    drawable.setColorFilter(CommonUtils.getInstance().getRandomColor(), PorterDuff.Mode.SRC_IN);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 根据（x,y）去找出当前点击的是哪一层（必须点击在非透明区域），最后通过设置setColorFilter去改变颜色即可
     * @param x  x坐标
     * @param y  y坐标
     * @return   点击的是哪一层
     */
    private Drawable findDrawable(float x, float y) {
        int numberOfLayers = mLayerDrawable.getNumberOfLayers();
        Drawable drawable;
        Bitmap bitmap;

        for (int i = numberOfLayers - 1; i >= 0; i--) {
            drawable = mLayerDrawable.getDrawable(i);
            bitmap = ((BitmapDrawable) drawable).getBitmap();
            try {
                int pixel = bitmap.getPixel((int) x, (int) y);
                if (pixel == Color.TRANSPARENT) {
                    continue;
                }
            } catch (Exception e) {
                continue;
            }

            return drawable;
        }

        return null;
    }
}
