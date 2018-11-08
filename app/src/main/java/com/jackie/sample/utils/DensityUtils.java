package com.jackie.sample.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by Jackie on 2017/5/8.
 */
public class DensityUtils {
    public static int dp2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5);
    }

    public static int px2dp(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5);
    }

    public static int px2sp(Context context, double pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5);
    }

    public static int sp2px(Context context, double spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5);
    }

    /**
     * @param paramText  显示的文本
     * @param paramPaint 画笔
     * @return 文本的宽度
     */
    public static float getTextWidth(@NonNull String paramText, Paint paramPaint) {
        return paramPaint.measureText(paramText);
    }

    /**
     * @param paramText  显示的文本
     * @param paramPaint 画笔
     * @return 文本的高度
     */
    public static float getTextHeight(String paramText, Paint paramPaint) {
        if (TextUtils.isEmpty(paramText)) {
            paramText = "高度";
        }

        Rect rect = new Rect();

        paramPaint.getTextBounds(paramText, 0, paramText.length(), rect);

        return rect.height();
    }
}
