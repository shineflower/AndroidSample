package com.jackie.sample.scratch_card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ScratchView;

public class ScratchCardActivity extends AppCompatActivity implements ScratchView.OnSwipeCompletedListener {
    private ScratchView mScratchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        mScratchView = (ScratchView) findViewById(R.id.scratch);
        mScratchView.setOnSwipeCompletedListener(this);

        mScratchView.setText("Android新技能GET");
    }

    @Override
    public void onSwipeCompleted() {
        Toast.makeText(this, "刮得的差不多了", Toast.LENGTH_LONG).show();
    }
}
