package com.jackie.sample.file_stream_recorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jackie.sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FileStreamRecorderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_stream_recorder);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_file_mode, R.id.btn_stream_mode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_file_mode:
                startActivity(new Intent(this, FileModeActivity.class));
                break;
            case R.id.btn_stream_mode:
                startActivity(new Intent(this, StreamModeActivity.class));
                break;
        }
    }
}
