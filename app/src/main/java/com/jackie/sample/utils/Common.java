package com.jackie.sample.utils;

/**
 * Created by Jackie on2017/3/6
 */
public class Common {
    private static Common mInstance;

    public static Common getInstance() {
        if (mInstance == null) {
            synchronized (Common.class) {
                if (mInstance == null) {
                    mInstance = new Common();
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
