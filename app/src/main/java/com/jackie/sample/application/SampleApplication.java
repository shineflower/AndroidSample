package com.jackie.sample.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by Jackie on 2017/5/8.
 * Application
 */

public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        NoHttp.initialize(this);
    }
}
