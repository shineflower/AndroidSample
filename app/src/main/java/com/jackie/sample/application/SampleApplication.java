package com.jackie.sample.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yolanda.nohttp.NoHttp;

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
