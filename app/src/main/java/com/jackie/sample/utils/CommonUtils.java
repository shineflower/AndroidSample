package com.jackie.sample.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by Jackie on2017/3/6
 */
public class CommonUtils {
    private static CommonUtils mInstance;

    public static CommonUtils getInstance() {
        if (mInstance == null) {
            synchronized (CommonUtils.class) {
                if (mInstance == null) {
                    mInstance = new CommonUtils();
                }
            }
        }

        return mInstance;
    }

    long mLastClickTime = 0;

    public boolean isNotFastClick() {
        long time = System.currentTimeMillis();
        if (time - mLastClickTime >= 300) {
            mLastClickTime = time;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 随机颜色
     * Color.argb(random.nextInt(120), random.nextInt(255), random.nextInt(255), random.nextInt(255));
     */
    public int getRandomColor() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        String hexString;
        for (int i = 0; i < 3; i++) {
            hexString = Integer.toHexString(random.nextInt(0xFF));
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }

            stringBuilder.append(hexString);
        }

        return Color.parseColor("#" + stringBuilder.toString());
    }
}
