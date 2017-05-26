package com.jackie.sample.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jackie.sample.float_window.FloatWindowManager;
import com.jackie.sample.utils.PackageUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jackie on 2017/5/25.
 */

public class FloatWindowService extends Service {
    //用于在线程中创建或移除悬浮窗
    private Handler mHandler = new Handler();

   //定时器，定时进行检测当前应该创建还是移除悬浮窗
    private Timer  mTimer;

    private Context mContext;
    private FloatWindowManager mFloatWindowManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开启定时器，每隔0.5秒刷新一次
        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }

        mContext = this;

        mFloatWindowManager = FloatWindowManager.getInstance(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //service被终止时的同时也停止定时器
        mTimer.cancel();
        mTimer = null;
    }

    private class RefreshTask extends TimerTask {

        @Override
        public void run() {
            //当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗
            if (PackageUtils.isHome(mContext) && !mFloatWindowManager.isWindowShowing()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatWindowManager.createSmallWindow(mContext);
                    }
                });
            } else if (!PackageUtils.isHome(mContext) && mFloatWindowManager.isWindowShowing()) {
                //当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatWindowManager.removeSmallWindow();
                        mFloatWindowManager.removeBigWindow();
                    }
                });
            } else if (PackageUtils.isHome(mContext) && mFloatWindowManager.isWindowShowing()) {
                //当前界面是桌面，且有悬浮窗显示，则更新内存数据
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFloatWindowManager.updateUsedMemoryPercentValue(mContext);
                    }
                });
            }
        }
    }


}
