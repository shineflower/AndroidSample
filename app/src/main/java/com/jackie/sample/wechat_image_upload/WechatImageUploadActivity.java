package com.jackie.sample.wechat_image_upload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.jackie.sample.R;
import com.jackie.sample.wechat_image_upload.adapter.ImageAdapter;
import com.jackie.sample.wechat_image_upload.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/12.
 */

public class WechatImageUploadActivity extends AppCompatActivity {
    List<List<ImageBean>> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_image_upload);

        ListView listView = (ListView) findViewById(R.id.list_view);

        ImageAdapter adapter = new ImageAdapter(this, mList);
        listView.setAdapter(adapter);

        for (int i = 0; i < 10; i++) {
            List<ImageBean> list = new ArrayList<>();

            list.add(new ImageBean("http://img02.tooopen.com/images/20140504/sy_60294738471.jpg"));
            list.add(new ImageBean("http://pic.58pic.com/58pic/16/62/63/97m58PICyWM_1024.jpg"));
            list.add(new ImageBean("http://pic78.huitu.com/res/20160604/1029007_20160604114552332126_1.jpg"));
            list.add(new ImageBean("http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg"));

            mList.add(list);
        }

        adapter.notifyDataSetChanged();
    }
}
