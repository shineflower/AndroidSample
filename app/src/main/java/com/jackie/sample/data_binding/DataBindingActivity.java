package com.jackie.sample.data_binding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;

public class DataBindingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding);

        Button sampleButton = (Button) findViewById(R.id.sample);
        Button listButton = (Button) findViewById(R.id.list);
        Button twoWayButton = (Button) findViewById(R.id.two_way);
        Button animationButton = (Button) findViewById(R.id.animation);

        sampleButton.setOnClickListener(this);
        listButton.setOnClickListener(this);
        twoWayButton.setOnClickListener(this);
        animationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sample:
                intent =  new Intent(this, SampleActivity.class);
                startActivity(intent);
                break;
            case R.id.list:
                intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                break;
            case R.id.two_way:
                intent = new Intent(this, TwoWayActivity.class);
                startActivity(intent);
                break;
            case R.id.animation:
                intent = new Intent(this, AnimationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
