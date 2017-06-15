package com.jackie.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import static com.baidu.location.h.j.w;

/**
 * Created by Jackie on 2017/5/11.
 * 屏幕相关工具类
 */

public class ScreenUtils {
    public static void hideNavigationBar(Activity activity) {
        int uiFlags = //View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hide nav bar

        if (android.os.Build.VERSION.SDK_INT >= 19){
            uiFlags |= 0x00001000;    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    // 获取状态栏高度
    public static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusHeight;
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
//
//        return statusHeight;
//    }

    public static int getMinScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);

        //方法一
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        //方法二
//        windowManager.getDefaultDisplay().getWidth();
//        windowManager.getDefaultDisplay().getHeight();

        //方法三
//        context.getResources().getDisplayMetrics().widthPixels;
//        context.getResources().getDisplayMetrics().heightPixels;

        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    //获取屏幕的宽度
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 测量View的宽高
     * @param view view
     */
    public static void measureWidthAndHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
    }
}
