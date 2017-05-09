package com.jackie.sample.animator;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jackie.sample.R;

public class ValueAnimatorActivity extends Activity implements View.OnClickListener {
    private Context mContext;
    private TextView mValueAnimatorIntText, mValueAnimatorFloatText;
    private Button mButton;
    private ValueAnimator mIntValueAnimator, mFloatValueAnimator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animator);

        initView();
    }

    private void initView() {
        mValueAnimatorIntText = (TextView)findViewById(R.id.value_animator_int_text);
        mValueAnimatorFloatText = (TextView)findViewById(R.id.value_animator_float_text);
        mButton = (Button)findViewById(R.id.value_animator_start);

        mButton.setOnClickListener(this);

        mValueAnimatorIntText.setOnClickListener(this);

        int [] data = getIntByFloat(50001.24f);

        mIntValueAnimator = ValueAnimator.ofInt(0, data[0]);
        mFloatValueAnimator2 = ValueAnimator.ofInt(0, data[1]);

        /**
         * 监听属性动画的改变值
         * 类似理财产品的钱数额增加的，还得有其他的处理方式才行。
         */
        mIntValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValueAnimatorIntText.setText("" + (valueAnimator.getAnimatedValue()));
            }
        });

        mIntValueAnimator.setDuration(2000);

        mFloatValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mValueAnimatorFloatText.setText(""+ (valueAnimator.getAnimatedValue()));
            }
        });

        mFloatValueAnimator2.setDuration(2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.value_animator_start:
                mIntValueAnimator.start();
                mFloatValueAnimator2.start();
                break;
        }
    }
    public int[] getIntByFloat(float number){
        if(number == 0f) {
            return new int[]{0, 0};
        }
        String numberStr = number + "";

        String [] dest = numberStr.split("\\.");
        if(dest.length == 2) {
            return new int[]{ parseIntByStr(dest[0]), parseIntByStr(dest[1]) };
        } else if(dest.length == 1) {
            return new int[]{ 0, parseIntByStr(dest[0]) };
        } else {
            return new int[]{0,0};
        }
    }

    public int parseIntByStr(String dest ) {
        if (dest==null  || "".equals(dest)) {
            return 0;
        }

        try {
            return Integer.parseInt(dest);
        } catch(Exception e){
            return 0 ;
        }
    }
}
