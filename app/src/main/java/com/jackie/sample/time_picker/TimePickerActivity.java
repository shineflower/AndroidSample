package com.jackie.sample.time_picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.time_picker.loop_view.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/10/9.
 * 自定义滚轮选择器
 */

public class TimePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_time_picker);

        findViewById(R.id.btn_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setSelectYear  setSelectMonth  setSelectDay 传的是position, 从0开始
                new DatePickerDialog.Builder(TimePickerActivity.this)
                        .setTitle("请选择日期")
                        .setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(String s) {
                        Toast.makeText(TimePickerActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create().show();
            }
        });

        findViewById(R.id.btn_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> dataList = DateUtil.getTimeForString("17:30");

                new TimePickerDialog.Builder(TimePickerActivity.this)
                        .setSelectHour(dataList.get(0)).setSelectMin(dataList.get(1))
                        .setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                    @Override
                    public void onTimeSelected(String times) {
                        Toast.makeText(TimePickerActivity.this, times, Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
            }
        });

        findViewById(R.id.btn_date_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateAndTimePickerDialog.Builder(TimePickerActivity.this)
                        .setOnDateSelectedListener(new DateAndTimePickerDialog.OnDateTimeSelectedListener() {
                    @Override
                    public void onDateTimeSelected(String date) {
                        Toast.makeText(TimePickerActivity.this, date, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create().show();
            }
        });

        findViewById(R.id.btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> dataList = new ArrayList<>();

                String[] loopList = getResources().getStringArray(R.array.loop_list);

                for (String loopString : loopList) {
                    dataList.add(loopString);
                }

                new ChoosePickerDialog.Builder(TimePickerActivity.this)
                        .setData(dataList)
                        .setTitle("请选择业务单位")
                        .setOnDataSelectedListener(new ChoosePickerDialog.OnSelectedListener() {
                    @Override
                    public void onSelected(String itemValue, int position) {
                        Toast.makeText(TimePickerActivity.this, itemValue, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).create().show();
            }
        });
    }
}
