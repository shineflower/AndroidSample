package com.jackie.sample.circle_range;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.GalleryAdapter;
import com.jackie.sample.custom_view.CircleRangeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Jackie on 2017/5/24.
 */

public class CircleRangeActivity extends AppCompatActivity implements View.OnClickListener {
    private CircleRangeView mCircleRangeView;
    private String [] mValueArray;
    private Random mRandom;

    private TextView mTime;
    private Gallery mGallery;
    private GalleryAdapter mAdapter;

    private List<String> mDataList;

    private static final String[] DATA_ARRAY = { "不设置", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_range);

        mCircleRangeView = (CircleRangeView) findViewById(R.id.circle_range_view);
            mCircleRangeView.setOnClickListener(this);

        mValueArray = getResources().getStringArray(R.array.circle_range_view_values);
        mRandom = new Random();



        mTime = (TextView) findViewById(R.id.time);
        mGallery = (Gallery) findViewById(R.id.gallery);

        mDataList = new ArrayList<>();
        Collections.addAll(mDataList, DATA_ARRAY);

        mAdapter = new GalleryAdapter(2, mDataList);

        mGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSelectPosition(position);

                if (position == 0) {
                    mTime.setText(DATA_ARRAY[position]);
                } else {
                    mTime.setText("≤" + DATA_ARRAY[position]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGallery.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.circle_range_view:
                List<String> extras = new ArrayList<>();
                extras.add("收缩压：116");
                extras.add("舒张压：85");

                int i = mRandom.nextInt(mValueArray.length);
                mCircleRangeView.setValueWithAnim(mValueArray[i], extras);
                break;
        }
    }
}
