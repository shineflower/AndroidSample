package com.jackie.sample.utils;

import android.content.Context;

/**
 * Created by Jackie on 2017/5/11.
 * 屏幕相关工具类
 */

public class ScreenUtils {

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }

        return height;
    }

//    public static int getWindowStatusHeight(Context context) {
//        int statusHeight = 0;
//        Rect localRect = new Rect();
//        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//        statusHeight = localRect.top;
//        if (0 == statusHeight) {
//            Class<?> localClass;
//            try {
//                localClass = Class.forName("com.android.internal.R$dimen");
//                Object localObject = localClass.newInstance();
//                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
//                statusHeight = context.getResources().getDimensionPixelSize(i5);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return statusHeight;
//    }
}
