package com.jackie.sample.timeline;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.TimelineLayout;
import com.jackie.sample.utils.DensityUtils;

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addItemButton;
    private Button subItemButton;
    private Button addMarginButton;
    private Button subMarginButton;
    private TextView mCurrentMargin;

    private TimelineLayout mTimelineLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        initView();
    }

    private void initView() {
        addItemButton = (Button) findViewById(R.id.add_item);
        subItemButton = (Button) findViewById(R.id.sub_item);
        addMarginButton= (Button) findViewById(R.id.add_margin);
        subMarginButton= (Button) findViewById(R.id.sub_margin);
        mCurrentMargin= (TextView) findViewById(R.id.current_margin);
        mTimelineLayout = (TimelineLayout) findViewById(R.id.timeline_layout);

        addItemButton.setOnClickListener(this);
        subItemButton.setOnClickListener(this);
        addMarginButton.setOnClickListener(this);
        subMarginButton.setOnClickListener(this);
    }

    private int index = 0;
    private void addItem() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_timeline, mTimelineLayout, false);
        ((TextView) view.findViewById(R.id.tv_action)).setText("步骤" + index);
        ((TextView) view.findViewById(R.id.tv_action_time)).setText("2017年3月8日16:55:04");
        ((TextView) view.findViewById(R.id.tv_action_status)).setText("完成");
        mTimelineLayout.addView(view);
        index++;
    }

    private void subItem() {
        if (mTimelineLayout.getChildCount() > 0) {
            mTimelineLayout.removeViews(mTimelineLayout.getChildCount() - 1, 1);
            index--;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_item:
                addItem();
                break;
            case R.id.sub_item:
                subItem();
                break;
            case R.id.add_margin:
                int currentMargin = DensityUtils.px2dp(this, mTimelineLayout.getLineMarginLeft());
                mTimelineLayout.setLineMarginLeft(DensityUtils.dp2px(this, ++currentMargin));
                mCurrentMargin.setText("current line margin left is " + currentMargin + "dp");
                break;
            case R.id.sub_margin:
                currentMargin = DensityUtils.px2dp(this, mTimelineLayout.getLineMarginLeft());
                mTimelineLayout.setLineMarginLeft(DensityUtils.dp2px(this, --currentMargin));
                mCurrentMargin.setText("current line margin left is " + currentMargin + "dp");
                break;
            default:
                break;
        }
    }
}