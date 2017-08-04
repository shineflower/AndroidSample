package com.jackie.sample.multiple_download;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.adapter.FileListAdapter;
import com.jackie.sample.bean.FileInfo;
import com.jackie.sample.service.DownloadService;

import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity {
    private ListView mDownloadListView;
    private FileListAdapter mFileListAdapter;

    private List<FileInfo> mFileInfoList;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        initView();
        initData();
    }

    private void initView() {
        mDownloadListView = (ListView) findViewById(R.id.downLoad_list_view);
        mFileInfoList = new ArrayList<>();

        /**
         * Android 6.0需要添加下面的运行时权限检测
         */
        //创建文件
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            FileInfo fileInfo = new FileInfo(i,
                    "http://downmobile.kugou.com/Android/KugouPlayer/7840/KugouPlayer_219_V7.8.4.apk",
                    "KugouPlayer_219_V7.8.4.apk", 0, 0);
            mFileInfoList.add(fileInfo);
        }

        mFileListAdapter = new FileListAdapter(this, mFileInfoList);
        mDownloadListView.setAdapter(mFileListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.DOWNLOAD_ACTION_UPDATE);
        filter.addAction(DownloadService.DOWNLOAD_ACTION_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiver);
    }

    /**
     * 更新UI的广播接受者
     */
    private BroadcastReceiver  mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.DOWNLOAD_ACTION_UPDATE.equals(intent.getAction())) {
                int id = intent.getIntExtra(DownloadService.KEY_DOWNLOAD_ID, 0);
                int progress = intent.getIntExtra(DownloadService.KEY_DOWNLOAD_PROGRESS, 0);
                int length = intent.getIntExtra(DownloadService.KEY_DOWNLOAD_LENGTH, 0);
                mFileListAdapter.updateProgress(id, progress, length);
            } else if (DownloadService.DOWNLOAD_ACTION_FINISHED.equals(intent.getAction())) {
                FileInfo fileInfo = intent.getParcelableExtra(DownloadService.KEY_DOWNLOAD_FILE_INFO);
                Toast.makeText(DownloadActivity.this, mFileInfoList.get(fileInfo.getId()).getFileName() + "下载完成", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
                finish();
            }
        }
    }
}
