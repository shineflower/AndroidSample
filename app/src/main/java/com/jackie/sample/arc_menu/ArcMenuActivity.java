package com.jackie.sample.arc_menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.ArcMenu;

public class ArcMenuActivity extends Activity {
    private ArcMenu mArcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_menu);

        mArcMenu = (ArcMenu) findViewById(R.id.arc_menu);
        mArcMenu.setonMenuItemClickListener(new ArcMenu.onMenuItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ArcMenuActivity.this, position + ": " + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
