package com.jackie.sample.recorder_timing;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.RecorderView;
import com.jackie.sample.utils.DialogUtils;
import com.jackie.sample.utils.FasterAnimationsContainer;
import com.jackie.sample.utils.RecorderUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecorderTimingActivity extends AppCompatActivity implements View.OnClickListener, DialogUtils.OnConfirmListener {
    private ImageView mRecorderAnimView;
    private FasterAnimationsContainer mFasterAnimationsContainer;

    private RecorderView mRecorderView;
    private TextView mRecorderTime;
    private Button mRecorderQuit;
    private Button mRecorderFinish;

    private long mStartTime;

    //最大录制时间
    private int mMaxDuration  = 180000;
    private long mRemainTime; //倒计时的时间

    private MediaRecorder mMediaRecorder;

    private List mRecordFileList; //录音的集合

    private RecorderUtils mRecordArmUtils;

    private int     mPosition    = -1;
    private File    mLastFile = null;
    private boolean mIsRecording = true;

    private static final int MSG_RECORDER_TIMING = 200;
    private static final int ANIMATION_INTERVAL = 50;  // 50ms

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECORDER_TIMING:
                    long currentTimeMillis = System.currentTimeMillis();
                    long duration = currentTimeMillis - mStartTime;

                    mRecorderView.setProgress(duration);

                    if (mIsRecording) {
                        mHandler.sendEmptyMessageDelayed(MSG_RECORDER_TIMING, 0);
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_timing);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mRecorderAnimView = (ImageView) findViewById(R.id.recorder_anim);
        //将帧动画资源id以字符串数组形式写到values/arrays.xml中
        mFasterAnimationsContainer = FasterAnimationsContainer.getInstance(this, mRecorderAnimView);
        mFasterAnimationsContainer.addAllFrames(getDrawableResIds(), ANIMATION_INTERVAL);
        startAnim();

        mRecorderView = (RecorderView) findViewById(R.id.recorder_view);
        mRecorderTime = (TextView) findViewById(R.id.recorder_time);
        mRecorderQuit = (Button) findViewById(R.id.recorder_quit);
        mRecorderFinish = (Button) findViewById(R.id.recorder_finish);
    }

    private void initEvent() {
        mRecorderQuit.setOnClickListener(this);
        mRecorderFinish.setOnClickListener(this);
    }

    private void initData() {
        //圆形画圈计时
        startCircleTimer();
        //显示时间计时器
        startTextTimer();
        //开始录音
        startRecorder();
    }

    private void startAnim() {
        mFasterAnimationsContainer.start();
    }

    private void stopAnim() {
        mFasterAnimationsContainer.stop();
    }

    private void startCircleTimer() {
        mStartTime = System.currentTimeMillis();

        //设置录音最大时长
        mRecorderView.setMax(mMaxDuration);

        mRecorderView.setOnGestureListener(new RecorderView.OnGestureListener() {
            @Override
            public void onLongClick() {
//                mRecorderView.setSplit();
//                mHandler.sendEmptyMessageDelayed(MSG_RECORDER_TIMING, 100);
            }

            @Override
            public void onClick() {

            }

            @Override
            public void onLift() {

            }

            @Override
            public void onOver() {

            }
        });

        mHandler.sendEmptyMessage(MSG_RECORDER_TIMING);
    }

    //开始计时功能
    private void startTextTimer() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(0);
//        calendar.add(Calendar.HOUR_OF_DAY, -8);
//        Date time = calendar.getTime();
        mRemainTime = 180000;
        mHandler.post(mTimerRunnable);
    }

    /**
     * 循环执行线程
     */
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mTimerRunnable, 1000);
            mRemainTime -= 1000;

            CharSequence charSequence = DateFormat.format("mm:ss", mRemainTime);
            mRecorderTime.setText(charSequence);

            if (mRemainTime == 0) {
                //设定的录音时间到达
                mHandler.removeCallbacks(mTimerRunnable);
                mRecorderTime.setTextColor(Color.parseColor("#dcdcdc"));

                mRecorderView.setStop(true);

//                mRecorderAnimView.setVisibility(View.INVISIBLE);
            }
        }
    };

    public void startRecorder() {
        mMediaRecorder = new MediaRecorder();
        mRecordFileList = new ArrayList(); //录音的集合

        mRecordArmUtils = new RecorderUtils(this, mMediaRecorder);
        //获得JackieRecorder文件夹下.mp3后缀的文件名集合
        getRecorderFiles();
        //开始录音
        mRecordArmUtils.startRecorder(mRecordFileList);
    }

    public void getRecorderFiles() {
        boolean isMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        //取得SD Card路径作为录音的文件位置
        if (isMounted) {
            File dir = getRecorderDir();
            File files[] = dir.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().indexOf(".") >= 0) {
                        // 只取.mp3文件,  files[i].getName().indexOf(".")读取"."的位置，然后取得"."(包括.)以后的字符串
                        String fileS = files[i].getName().substring(files[i].getName().indexOf("."));

                        if (fileS.toLowerCase().equals(".mp3")) {
                            mRecordFileList.add(files[i].getName());
                        }
                    }
                }
            }
        }
    }

    public File getRecorderDir() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/JackieRecorder");
        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    @Override
    public void onClick(View v) {
        stopRecorder();

        switch (v.getId()) {
            case R.id.recorder_quit:
                showDialog();
                break;
            case R.id.recorder_finish:
                finish();
                break;
        }
    }

    private void stopRecorder() {
        mIsRecording = false;

        mRecorderView.setStop(true);

        mRecordArmUtils.stopRecorder();
        //结束动画
        stopAnim();

        //隐藏动画的图片
//        mRecorderAnimView.setVisibility(View.INVISIBLE);

        //最新录音对应的集合position
        mPosition = mRecordFileList.size() - 1;

        mLastFile = new File(getRecorderDir().getAbsolutePath() + File.separator + mRecordFileList.get(mPosition));

        mHandler.removeCallbacks(mTimerRunnable);
    }

    public void showDialog() {
        DialogUtils.getInstance().showCustomDialogPrompt(this, "温馨提示", "你确定要取消录音吗？", false, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler = null;

        mFasterAnimationsContainer.stop();
    }

    @Override
    public void onConfirm(Dialog dialog, View view, String input) {
        //删除该条录音 结束界面
        mRecordFileList.remove(mPosition);

        if (mLastFile.exists()) {
            mLastFile.delete();
        }

        finish();
    }

    private int[] getDrawableResIds() {
        TypedArray ta = getResources().obtainTypedArray(R.array.recorder_anim_list);
        int length = ta.length();

        int[] resIds = new int[length];

        for (int i = 0; i < length; i++) {
            resIds[i] = ta.getResourceId(i, -1);
        }

        ta.recycle();

        return resIds;
    }
}
