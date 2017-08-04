package com.jackie.sample.metro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.MetroImageView;

/**
 * Created by Jackie on 2017/5/26.
 */

public class MetroActivity extends AppCompatActivity implements View.OnClickListener {
    private MetroImageView mMetroJoke, mMetroCreative, mMetroConstellation, mMetroRecommend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro);

        mMetroJoke = (MetroImageView) findViewById(R.id.metro_joke);
        mMetroCreative = (MetroImageView) findViewById(R.id.metro_creative);
        mMetroConstellation = (MetroImageView) findViewById(R.id.metro_constellation);
        mMetroRecommend = (MetroImageView) findViewById(R.id.metro_recommend);

        mMetroJoke.setOnClickListener(this);
        mMetroCreative.setOnClickListener(this);
        mMetroConstellation.setOnClickListener(this);
        mMetroRecommend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.metro_joke:
                Toast.makeText(this, "Joke", Toast.LENGTH_SHORT).show();
                break;
            case R.id.metro_creative:
                Toast.makeText(this, "Creative", Toast.LENGTH_SHORT).show();
                break;
            case R.id.metro_constellation:
                Toast.makeText(this, "Constellation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.metro_recommend:
                Toast.makeText(this, "Recommend", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
