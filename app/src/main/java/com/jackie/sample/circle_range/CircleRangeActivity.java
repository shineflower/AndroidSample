package com.jackie.sample.circle_range;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CircleRangeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jackie on 2017/5/24.
 */

public class CircleRangeActivity extends Activity implements View.OnClickListener {
    private CircleRangeView mCircleRangeView;
    private String [] mValueArray;
    private Random mRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_range);

        mCircleRangeView = (CircleRangeView) findViewById(R.id.circle_range_view);
            mCircleRangeView.setOnClickListener(this);

        mValueArray = getResources().getStringArray(R.array.circle_range_view_values);
        mRandom = new Random();
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
