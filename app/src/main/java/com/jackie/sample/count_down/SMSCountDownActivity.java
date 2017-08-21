package com.jackie.sample.count_down;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.CountDownTimerUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jackie on 2017/5/12.
 * 短信验证码倒计时
 */

public class SMSCountDownActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mCountTimeView;
    private TextView mSmsTxt;
    private CountDownTimer mCountDownTimer;

    private static final int MSG_SMS_FINISH = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_SMS_FINISH:
                    String code = (String) msg.obj;
                    mSmsTxt.setText("短信验证码是: " + code);
                break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_count_down);

        mCountTimeView = (TextView) findViewById(R.id.tv_count_down);
        mCountDownTimer = new CountDownTimerUtils(mCountTimeView, 60000, 1000);
        mCountTimeView.setOnClickListener(this);

        mSmsTxt = (TextView) findViewById(R.id.tv_sms);
        SmsObserver smsObserver = new SmsObserver(this, mHandler);
        Uri smsUri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(smsUri, true, smsObserver); //注册短信uri的监听
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_count_down:
                mCountDownTimer.start();
                break;
        }
    }

    private class SmsObserver extends ContentObserver {
        private Context mContext;
        private Handler mHandler;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsObserver(Context context, Handler handler) {
            super(handler);

            mContext = context;
            mHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            //收到一条短信，onChange()会执行两次，uri不同
            if (uri.toString().equals("content://sms/raw")) { //未写入数据库，不进行任何操作
                return;
            } else {  //此时已经写入数据库
                Uri inboxUri = Uri.parse("content://sms/inbox");
                //按照日期倒序排序
                Cursor cursor = mContext.getContentResolver().query(inboxUri, null, null, null, "date DESC");

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        //发件人的号码
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        //短信内容
                        String body = cursor.getString(cursor.getColumnIndex("body"));

                        //利用正则提取验证码
                        Pattern pattern = Pattern.compile("(\\d{6})"); //提取六位数字
                        Matcher matcher = pattern.matcher(body); //进行匹配

                        if(matcher.find()){ //匹配成功
                            String code = matcher.group(0);
                            Message msg = mHandler.obtainMessage(1, code);
                            mHandler.sendMessage(msg);
                        }
                    }

                    cursor.close();
                }
            }
        }
    }
 }
