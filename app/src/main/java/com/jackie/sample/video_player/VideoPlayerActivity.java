package com.jackie.sample.video_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.video_player.utils.VideoPlayerManager;
import com.jackie.sample.video_player.view.VideoPlayer;
import com.jackie.sample.video_player.view.VideoPlayerController;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoPlayer mVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initView();
    }

    private void initView() {
        mVideoPlayer = (VideoPlayer) findViewById(R.id.video_player);
        mVideoPlayer.setPlayerType(VideoPlayer.PLAYER_TYPE_IJK);
        mVideoPlayer.setup("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-33-30.mp4", null);

        VideoPlayerController controller = new VideoPlayerController(this);
        controller.setTitle("办公室小野开番外了，居然在办公室开澡堂！老板还点赞？");
        controller.setImage("http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/05/2017-05-17_17-30-43.jpg");

        mVideoPlayer.setController(controller);
    }

    @Override
    public void onBackPressed() {
        if (VideoPlayerManager.getInstance().onBackPressd()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        VideoPlayerManager.getInstance().releaseVideoPlayer();
        super.onStop();
    }

    public void enterTinyWindow(View view) {
        if (mVideoPlayer.isPlaying()
                || mVideoPlayer.isBufferingPlaying()
                || mVideoPlayer.isPaused()
                || mVideoPlayer.isBufferingPaused()) {
            mVideoPlayer.enterTinyWindow();
        } else {
            Toast.makeText(this, "要播放后才能进入小窗口", Toast.LENGTH_SHORT).show();
        }
    }

    public void showVideoList(View view) {
        startActivity(new Intent(this, VideoListActivity.class));
    }
}
