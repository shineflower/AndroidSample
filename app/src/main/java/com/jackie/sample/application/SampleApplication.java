package com.jackie.sample.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jackie.sample.skin_support.CustomSDCardLoader;
import com.yolanda.nohttp.NoHttp;

import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by Jackie on 2017/5/8.
 * Application
 */

public class SampleApplication extends Application {
    private static SampleApplication mSampleApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mSampleApplication = this;

        Fresco.initialize(this);

        NoHttp.initialize(this);

        // 换肤
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // Material Design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭WindowBackground换肤，默认打开[可选]
                .loadSkin();

        SkinCompatManager.withoutActivity(this)
                .addStrategy(new CustomSDCardLoader());                 // 自定义加载策略，指定SDCard路径
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);

        MultiDex.install(this);
    }

    public static SampleApplication getApplicationInstance() {
        return mSampleApplication;
    }
}
