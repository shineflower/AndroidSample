package com.jackie.sample.desk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2019/2/26.
 */
public class FirstTaskAffinityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_activity_task_affinity);

        Button goBtn = (Button) findViewById(R.id.btn_go);

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstTaskAffinityActivity.this, SecondAffinityActivity.class);

                startActivity(intent);
            }
        });
    }
}
