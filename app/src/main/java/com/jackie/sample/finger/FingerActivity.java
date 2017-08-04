package com.jackie.sample.finger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackie.sample.R;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by Jackie on 2017/8/4.
 */

public class FingerActivity extends AppCompatActivity {
    private Button mStart;
    private TextView mResult;
    private FingerprintIdentify mFingerprintIdentify;  //指纹识别类，所有接口都封装在这个类里面
    private static final int MAX_AVAILABLE_TIMES = 10;  //识别10次就自动结束识别

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_finger);

        mStart = (Button) findViewById(R.id.start);
        mResult = (TextView) findViewById(R.id.result);

        mFingerprintIdentify = new FingerprintIdentify(getApplicationContext(), new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                append("\nException：" + exception.getLocalizedMessage());
            }
        });

        append("\nisHardwareEnable()   " + mFingerprintIdentify.isHardwareEnable()); // 判断是否硬件支持指纹识别
        append("\nisRegisteredFingerprint()   " + mFingerprintIdentify.isRegisteredFingerprint()); // 判断手机是否已经录入指纹
        append("\nisFingerprintEnable()   " + mFingerprintIdentify.isFingerprintEnable()); // 判断手机硬件是否支持指纹识别并且已经录入指纹

        if (!mFingerprintIdentify.isFingerprintEnable()) {
            append("\n\n" + "手机不支持指纹识别");
            return;
        }

        append("\n" + "点击开始按钮进行指纹识别");

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                append("\n" + "开始进行指纹识别，请放置你的指纹");

                mFingerprintIdentify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
                    @Override
                    public void onSucceed() {
                        append("\n" + "指纹识别成功");
                    }

                    @Override
                    public void onNotMatch(int availableTimes) {
                        append("\n" + "指纹不匹配，剩余" + availableTimes + "次机会");
                    }

                    @Override
                    public void onFailed(boolean isDeviceLocked) {
                        append("\n" + "指纹识别失败");
                    }

                    @Override
                    public void onStartFailedByDeviceLocked() {
                        append("\n" + "超过识别次数，请重新打开应用");
                    }
                });
            }
        });
    }

    private void append(String msg) {
        mResult.append(msg);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mFingerprintIdentify.cancelIdentify();
    }
}
