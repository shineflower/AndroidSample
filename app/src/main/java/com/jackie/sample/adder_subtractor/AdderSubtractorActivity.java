package com.jackie.sample.adder_subtractor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.AdderSubtractorView;

public class AdderSubtractorActivity extends AppCompatActivity implements View.OnClickListener, AdderSubtractorView.OnNumberListener {
    private Button mConfirmButton;

    private AdderSubtractorView mAdderSubtractorView1;
    private AdderSubtractorView mAdderSubtractorView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adder_subtractor);

        initView();
    }

    private void initView() {
        mConfirmButton = (Button) findViewById(R.id.btn_confirm);

        mAdderSubtractorView1 = (AdderSubtractorView) findViewById(R.id.adder_subtractor_view_1);
        mAdderSubtractorView2 = (AdderSubtractorView) findViewById(R.id.adder_subtractor_view_2);

        mConfirmButton.setOnClickListener(this);

        mAdderSubtractorView1.setStyle(1);  //设置选择器的样式
        mAdderSubtractorView1.setInitialValue(0);   //设置默认值
        mAdderSubtractorView1.setLeastValue(0); //设置最小值
        mAdderSubtractorView1.setMostValue(99); //设置最大值
        mAdderSubtractorView1.setOnNumberListener(this);    //添加当数字到达两界点时的监听

        mAdderSubtractorView2.setStyle(2);
        mAdderSubtractorView2.setInitialValue(0);
        mAdderSubtractorView2.setLeastValue(0);
        mAdderSubtractorView2.setMostValue(99);
        mAdderSubtractorView2.setOnNumberListener(this);
    }

    @Override
    public void onLeast(View view, int number) {
        Toast.makeText(this, "最小", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMost(View view, int number) {
        Toast.makeText(this, "最大", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                int number = mAdderSubtractorView1.getInputNumber();
                if (number == 0) {
                    Toast.makeText(this, "再加点吧！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, String.valueOf(number), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
