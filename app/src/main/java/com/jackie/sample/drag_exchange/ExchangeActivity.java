package com.jackie.sample.drag_exchange;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.DragGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jackie on 2017/5/11.
 */

public class ExchangeActivity extends AppCompatActivity {
    private List<HashMap<String, Object>> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        DragGridView dragGridView = (DragGridView) findViewById(R.id.drag_grid_view);
        for (int i = 0; i < 50; i++) {
            HashMap<String, Object> hashMap = new HashMap();
            hashMap.put("item_image", R.mipmap.ic_launcher);
            hashMap.put("item_text", "拖 " + Integer.toString(i));
            mList.add(hashMap);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, mList,
                R.layout.item_drag_grid, new String[] { "item_image", "item_text" },
                new int[] { R.id.item_image, R.id.item_text });

        dragGridView.setAdapter(simpleAdapter);

        dragGridView.setOnExchangeListener(new DragGridView.OnExchangeListener() {
            @Override
            public void onExchange(int from, int to) {
//                HashMap<String, Object> hashMap = mList.get(from);
                //直接交互item
//                mList.set(from, mList.get(to));
//                mList.set(to, hashMap);
//                mList.set(to, hashMap);

                //这里的处理需要注意下
                if(from < to) {
                    for(int i = from; i < to; i++) {
                        Collections.swap(mList, i, i + 1);
                    }
                } else if(from > to) {
                    for(int i = from; i > to; i--){
                        Collections.swap(mList, i, i - 1);
                    }
                }

                simpleAdapter.notifyDataSetChanged();
            }
        });
    }
}