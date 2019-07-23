package com.jackie.sample.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Jackie on 2018/1/18.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= 19) {
            // 设置沉浸式
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            /*
             * style 要设置成NoActionBar，style中配置 <item name="android:windowIsTranslucent">true</item>
             * 否则，会闪一下状态栏的颜色
             * 需要在xml中设置下面的代码，background为状态栏的颜色
             * android:background="@color/white_color"
             * android:clipToPadding="true"
             * android:fitsSystemWindows="true"
             *
             */
        }

        // Android 6.0以上，设置状态栏字体变黑
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
