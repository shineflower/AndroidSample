package com.jackie.sample.wechat_recorder;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.RecorderAdapter;
import com.jackie.sample.bean.Recorder;
import com.jackie.sample.custom_view.RecorderButton;
import com.jackie.sample.utils.MediaManager;

import java.util.ArrayList;
import java.util.List;

public class WechatRecorderActivity extends Activity implements RecorderButton.OnRecordStateListener {

    private ListView mListView;
    private RecorderButton mRecorderButton;
    private RecorderAdapter mAdapter;
    private List<Recorder> mList = new ArrayList<>();

    private View mAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_recorder);

        initView();
        data2View();
        initEvent();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mRecorderButton = (RecorderButton) findViewById(R.id.recorder_button);
        mRecorderButton.setOnRecordStateListener(this);
    }

    private void data2View() {
        mAdapter = new RecorderAdapter(this, mList);
        mListView.setAdapter(mAdapter);
    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放动画
                if (mAnimationView != null) {
                    mAnimationView.setBackgroundResource(R.drawable.recorder_anim);
                    mAnimationView = null;
                }
                mAnimationView = view.findViewById(R.id.recorder_anim);
                mAnimationView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationView.getBackground();
                animationDrawable.start();
                //播放音频
                MediaManager.playSound(mList.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimationView.setBackgroundResource(R.drawable.recorder_anim);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    @Override
    public void onRecordFinish(float time, String filePath) {
        Recorder recorder = new Recorder(time, filePath);
        mList.add(recorder);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mList.size() - 1);
    }
}
