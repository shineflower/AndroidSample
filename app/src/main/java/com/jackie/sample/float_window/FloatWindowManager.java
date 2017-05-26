package com.jackie.sample.float_window;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.FloatWindowBigView;
import com.jackie.sample.custom_view.FloatWindowSmallView;
import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.utils.SystemUtils;

/**
 * Created by Jackie on 2017/5/25.
 */

public class FloatWindowManager {
    private static FloatWindowManager mInstance;

    //小悬浮窗View的实例
    private FloatWindowSmallView mWindowSmallView;
    //大悬浮窗View的实例
    private FloatWindowBigView mWindowBigView;
    //火箭发射台的实例
    private RocketLauncher mRocketLauncher;

    //小悬浮窗View的参数
    private WindowManager.LayoutParams mWindowSmallParams;
    //大悬浮窗View的参数
    private WindowManager.LayoutParams mWindowBigParams;
    //火箭发射台的参数
    private WindowManager.LayoutParams mLauncherParams;

    //用于控制在屏幕上添加或移除悬浮窗
    private WindowManager mWindowManager;

    //用于取手机可用内存
    private ActivityManager mActivityManager;

    private int mScreenWidth, mScreenHeight;

    private FloatWindowManager(Context context){
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }

        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);
    }

    public static FloatWindowManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FloatWindowManager.class) {
                if (mInstance == null) {
                    mInstance = new FloatWindowManager(context);
                }
            }
        }

        return mInstance;
    }

    /**
     * 创建一个小悬浮窗，初始位置为屏幕的右部中间位置
     */
    public void createSmallWindow(Context context) {
        if (mWindowSmallView == null) {
            mWindowSmallView = new FloatWindowSmallView(context);

            if (mWindowSmallParams == null) {
                mWindowSmallParams = new WindowManager.LayoutParams();
                mWindowSmallParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                mWindowSmallParams.format = PixelFormat.RGBA_8888;
                mWindowSmallParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mWindowSmallParams.gravity = Gravity.LEFT | Gravity.TOP;
                mWindowSmallParams.width = mWindowSmallView.getWindowWidth();
                mWindowSmallParams.height = mWindowSmallView.getWindowHeight();
                mWindowSmallParams.x = mScreenWidth;
                mWindowSmallParams.y = mScreenHeight / 2;
            }

            mWindowSmallView.setParams(mWindowSmallParams);
            mWindowManager.addView(mWindowSmallView, mWindowSmallParams);
        }
    }

    //将小悬浮窗从屏幕移除
    public void removeSmallWindow() {
        if (mWindowSmallView != null) {
            mWindowManager.removeView(mWindowSmallView);
            mWindowSmallView = null;
        }
    }

    /**
     * 创建一个大悬浮窗，位置为屏幕正中间
     */
    public void createBigWindow(Context context) {
        if (mWindowBigView == null) {
            mWindowBigView = new FloatWindowBigView(context);

            if (mWindowBigParams == null) {
                mWindowBigParams = new WindowManager.LayoutParams();
                mWindowBigParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mWindowBigParams.format = PixelFormat.RGBA_8888;
                mWindowBigParams.gravity = Gravity.LEFT | Gravity.TOP;
                mWindowBigParams.width = mWindowBigView.getWindowWidth();
                mWindowBigParams.height = mWindowBigView.getWindowHeight();
                mWindowBigParams.x = mScreenWidth / 2
                        - mWindowBigView.getWindowWidth() / 2;
                mWindowBigParams.y = mScreenHeight / 2
                        - mWindowBigView.getWindowHeight() / 2;
            }

            mWindowManager.addView(mWindowBigView, mWindowBigParams);
        }
    }

    /**
     * 将大悬浮窗从屏幕上移除
     */
    public void removeBigWindow() {
        if (mWindowBigView != null) {
            mWindowManager.removeView(mWindowBigView);
            mWindowBigView = null;
        }
    }

    /**
     * 创建一个火箭发射台，位置为屏幕底部
     */
    public void createLauncher(Context context) {
        if (mRocketLauncher == null) {
            mRocketLauncher = new RocketLauncher(context);

            if (mLauncherParams == null) {
                mLauncherParams = new WindowManager.LayoutParams();
                mLauncherParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mLauncherParams.format = PixelFormat.RGBA_8888;
                mLauncherParams.gravity = Gravity.LEFT | Gravity.TOP;
                mLauncherParams.width = mRocketLauncher.getLauncherWidth();
                mLauncherParams.height = mRocketLauncher.getLauncherHeight();
                mLauncherParams.x = mScreenWidth / 2 - mRocketLauncher.getLauncherWidth() / 2;
                mLauncherParams.y = mScreenHeight - mRocketLauncher.getLauncherHeight();
            }

            mWindowManager.addView(mRocketLauncher, mLauncherParams);
        }
    }

    /**
     * 将火箭发射台从屏幕上移除
     */
    public void removeLauncher() {
        if (mRocketLauncher != null) {
            mWindowManager.removeView(mRocketLauncher);
            mRocketLauncher = null;
        }
    }

    /**
     * 更新火箭发射台显示状态
     */
    public void updateLauncher() {
        if (mRocketLauncher != null) {
            mRocketLauncher.updateLauncherStatus(isReadyToLaunch());
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比
     * @param context 上下文
     */
    public void updateUsedMemoryPercentValue(Context context) {
        if (mWindowSmallView != null) {
            TextView percentView = (TextView) mWindowSmallView.findViewById(R.id.percent);
            percentView.setText(SystemUtils.getUsedMemoryPercentValue(context));
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public boolean isWindowShowing() {
        return mWindowSmallView != null || mWindowBigView != null;
    }

    /**
     * 判断小火箭是否准备好发射了。
     * @return 当火箭被发到发射台上返回true，否则返回false。
     */
    public boolean isReadyToLaunch() {
        if (mWindowSmallParams.x > mLauncherParams.x &&
                mWindowSmallParams.x + mWindowSmallParams.width < mLauncherParams.x + mLauncherParams.width &&
                mWindowSmallParams.y + mWindowSmallParams.height > mLauncherParams.y) {
            return true;
        }

        return false;
    }
}
