package com.jackie.sample.utils;

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
}
