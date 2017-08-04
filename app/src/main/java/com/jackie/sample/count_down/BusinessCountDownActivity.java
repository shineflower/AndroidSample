package com.jackie.sample.count_down;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.BusinessAdapter;
import com.jackie.sample.bean.BusinessBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/8/3.
 */

public class BusinessCountDownActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BusinessAdapter mAdapter;

    private String[] urls = {
            "https://m.360buyimg.com/n12/jfs/t3274/110/7226515750/340717/dcc22021/58b3f70aN906d7f4e.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t3052/303/6006524998/42126/5b5c2f39/589aeca0N83a08142.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t2827/323/2794999100/113793/bbe682b9/5774f025Ne1873556.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t4477/266/3533973754/324173/23a8d97b/58feb7c9Ne618d47c.png!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t3667/353/1850595426/194928/89444a97/5833a155Nb692c054.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t4069/259/1853915479/216370/f85b3c6a/589a7d09Nbca551a3.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t5269/93/2324195441/356622/aa32d0f/59196808Ne5fa0b47.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t2659/206/1831332690/222038/7d24a87d/574e7eccN189ae60e.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t2836/164/1889998633/294102/3da3a8fe/574e84c7Nd0cdc768.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t4642/253/2687732613/191661/8e317991/58f21116N97a5954a.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t3052/303/6006524998/42126/5b5c2f39/589aeca0N83a08142.jpg!q70.jpg",
            "https://m.360buyimg.com/n12/jfs/t2827/323/2794999100/113793/bbe682b9/5774f025Ne1873556.jpg!q70.jpg" };

    private String[] series = {
            "10201", "10031", "11201", "10221", "10443", "10121", "10661", "10411", "10441",
            "10431", "10201", "10031"
    };

    private String[] titles = {
            "平安银行 招财进宝猴年生肖金条 AUXXX", "海飞丝 去屑洗发露发质柔滑型 750打架打架速度大幅度",
            "苹果(Apple)iPhone 7 Plus 128G 快来抢", "平安银行 招财进宝猴年生肖金条 AUXXX",
            "海飞丝 去屑洗发露发质柔滑型 750打架打架速度大幅度", "苹果(Apple)iPhone 7 Plus 128G 快来抢",
            "平安银行 平安金福金条 Au9999 2g", "8平安银行 招财进宝猴年生肖金条 AUXXX",
            "苹果(Apple)iPhone 7 Plus 128G 快来抢", "平安银行 平安金福金条 Au9999 2g",
            "平安银行 平安金福金条 Au9999 2g", "海飞丝 去屑洗发露发质柔滑型 750打架打架速度大幅度",};

    private String[] values = {
            "4421", "888", "7721", "2215", "6624", "645", "1211", "2215", "442", "23", "4421",
            "888", "7721"
    };

    private String[] numbers = {
            "10543001", "10543002", "10543003", "10543004", "10543005", "10543006",
            "10543007", "10543008", "10543009", "10543010", "10543011", "10543012", "10543013"
    };

    private String[] names = { "刘德华", "张学友", "周星驰", "杨千嬅", "古天乐", "ABC", "陈道明", "黄晓明", "高进", "陈宝国", "洪金宝",
            "林正英" };

    private long[] times = {
            System.currentTimeMillis() + 15000,
            System.currentTimeMillis() + 26000,
            System.currentTimeMillis() + 58000,
            System.currentTimeMillis() + 75000,
            System.currentTimeMillis() + 32000,
            System.currentTimeMillis() + 65000,
            System.currentTimeMillis() + 88000,
            System.currentTimeMillis() + 123000,
            System.currentTimeMillis() + 115000,
            System.currentTimeMillis() + 126000,
            System.currentTimeMillis() + 158000,
            System.currentTimeMillis() + 175000
    };

    String[] dates = { "2017-07-20 12:45:00", "2017-07-21 12:45:00", "2017-07-22 12:45:00",
            "2017-07-20 12:45:00", "2017-07-21 12:45:00", "2017-07-22 12:45:00",
            "2017-07-20 12:45:00", "2017-07-21 12:45:00", "2017-07-22 12:45:00",
            "2017-07-20 12:45:00", "2017-07-21 12:45:00", "2017-07-22 12:45:00"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business_count_down);

        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<BusinessBean> businessList = new ArrayList<>();

        for (int i = 0; i < urls.length; i++) {
            BusinessBean businessBean = new BusinessBean();
            businessBean.setUrl(urls[i]);
            businessBean.setSeries(series[i]);
            businessBean.setTitle(titles[i]);
            businessBean.setValue(values[i]);
            businessBean.setNumber(numbers[i]);
            businessBean.setName(names[i]);
            businessBean.setTime(times[i]);
            businessBean.setDate(dates[i]);
            businessList.add(businessBean);
        }


        mAdapter = new BusinessAdapter(this, businessList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAdapter != null) {
            mAdapter.cancelAllTimers();
        }
    }
}
