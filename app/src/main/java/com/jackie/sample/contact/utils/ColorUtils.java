package com.jackie.sample.contact.utils;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jackie on 2017/7/6.
 */

public class ColorUtils {
    /**
     * 根据数据位置来给Paint循环设置颜色
     * @param paint     paint
     * @param position  position
     */
    public static void setPaintColor(Paint paint, int position) {
        int pos = position % 6;

        switch (pos) {
            case 0:
                paint.setColor(Color.parseColor("#EC5745"));
                break;
            case 1:
                paint.setColor(Color.parseColor("#377caf"));
                break;
            case 2:
                paint.setColor(Color.parseColor("#4ebcd3"));
                break;
            case 3:
                paint.setColor(Color.parseColor("#6fb30d"));
                break;
            case 4:
                paint.setColor(Color.parseColor("#FFA500"));
                break;
            case 5:
                paint.setColor(Color.parseColor("#bf9e5a"));
                break;
        }
    }
}
