package com.jackie.sample.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;
import com.jackie.sample.utils.NotificationUtils;

/**
 * Created by Jackie on 2017/6/16.
 */

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSendNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notification);

        mSendNotification = (Button) findViewById(R.id.send_notification);
        mSendNotification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_notification:
                NotificationUtils.getInstance(this).sendNotification(this, "通知", RemoteActivity.class);
                break;
        }
    }
}
