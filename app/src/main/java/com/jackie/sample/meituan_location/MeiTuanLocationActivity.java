package com.jackie.sample.meituan_location;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jackie.sample.R;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2018/7/30.
 * 仿美团城市定位
 */
public class MeiTuanLocationActivity extends AppCompatActivity {
    private Button mConfirmBtn;

    private LocationClient mLocationClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meituan_location);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocationClient.stop();
    }

    private void initView() {
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);

        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(new LocationListener());

        // 开始定位
        mLocationClient.start();
    }

    private void initData() {
        // 初始化定位参数
        initLocationOption();
    }

    /**
     * 设置定位参数
     */
    private void initLocationOption() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(10000); // 10分钟扫描1次
        // 需要地址信息，设置为其他任何值（string类型，且不能为null）时，都表示无地址信息。
        option.setAddrType("all");
        // 设置是否返回POI的电话和地址等详细信息。默认值为false，即不返回POI的电话和地址信息。
//        option.setPoiExtraInfo(true);
        // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setProdName("通过GPS定位我当前的位置");
        // 禁用启用缓存定位数据
        option.disableCache(true);
        // 设置最多可返回的POI个数，默认值为3。由于POI查询比较耗费流量，设置最多返回的POI个数，以便节省流量。
//        option.setPoiNumber(3);
        // 设置定位方式的优先级。
        // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
    }

    private void initEvent() {
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationWindow();
            }
        });
    }

    private void showLocationWindow() {
        List<HotCity> hotCities = new ArrayList<>();

        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));

        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())	                                //此方法必须调用
                .enableAnimation(true)                          	                                //启用动画效果
//                .setAnimationStyle(anim)	                                                        //自定义动画
//                .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101"))    //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)	                                                        //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                    }

                    @Override
                    public void onLocate() {
                        // 当定位失败，点击定位失败按钮重新定位的回调
                        mLocationClient.start();
                    }
                })
                .show();
    }

    /**
     * 实现实位回调监听
     */
    public class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || bdLocation.getCity() == null) {
                return;
            }

            // 杭州市
            String city = bdLocation.getCity().substring(0, bdLocation.getCity().length() - 1);
            String province = bdLocation.getProvince();
            String cityCode = bdLocation.getCityCode();

            // 定位成功 开始定位成功
            CityPicker.getInstance().setLocatedCity(new LocatedCity(city, province, cityCode));

            // 定位失败 再次定位成功
            CityPicker.getInstance().locateComplete(new LocatedCity(city, province, cityCode),
                    LocateState.SUCCESS);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
