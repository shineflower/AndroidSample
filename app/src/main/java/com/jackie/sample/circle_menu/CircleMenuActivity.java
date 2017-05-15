package com.jackie.sample.circle_menu;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Jackie on 2017/5/15.
 */

public class CircleMenuActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getListView().setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, new String[] {
                        "建行圆形菜单", "圆形菜单" }));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;
        if (position == 0) {
            intent = new Intent(this, CCBActivity.class);
        } else {
            intent = new Intent(this, CircleActivity.class);
        }

        startActivity(intent);
    }
}
