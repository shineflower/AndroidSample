package com.jackie.sample.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.jackie.sample.R;
import com.jackie.sample.adapter.AddressListAdapter;
import com.jackie.sample.bean.NearAddressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/27.
 * 高德地图
 */

public class AMapActivity extends AppCompatActivity implements View.OnClickListener, AddressListAdapter.OnItemClickListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener, GeocodeSearch.OnGeocodeSearchListener {
    private MapView mMapView;
    private ImageView mLocationView;
    private RecyclerView mRecyclerView;

    private AddressListAdapter mAdapter;

    //定位相关
    private AMap mAMap;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng mLatLng;  //地理经纬度

    private Marker mMarker;

    private LatLng mTarget;
    private PoiSearch.Query mQuery; // Poi查询条件类
    private PoiSearch mPoiSearch;
    private List<PoiItem> mPoiItemList; // poi数据
    private List<NearAddressBean> mNearAddressList;

    private int mCurrentPage = 0;// 当前页面，从0开始计数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_amap);

        initView(savedInstanceState);
        initLocation();
        startLocation();
    }

    private void initView(@Nullable Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mLocationView = (ImageView) findViewById(R.id.iv_location);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        mLocationView.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (mAdapter == null) {
            mAdapter = new AddressListAdapter(this);
        }

        mAMap = mMapView.getMap();

        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setLogoBottomMargin(-50);  //隐藏logo
        uiSettings.setScaleControlsEnabled(false);
        uiSettings.setZoomControlsEnabled(false);

        mAdapter.setOnItemClickListener(this);
    }

    private void initLocation() {
        //初始化client
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationOption = getDefaultOption();
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位监听
        mLocationClient.setLocationListener(mLocationListener);
    }

    private void startLocation() {
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 定位监听
     */
    AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                if (location.getErrorCode() == 0) {
                    mLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mAMap.setOnCameraChangeListener(AMapActivity.this);
                    addMarkerInScreenCenter();

                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 30));
                } else {
                    Toast.makeText(AMapActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AMapActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 在屏幕中心添加Marker
     */
    private void addMarkerInScreenCenter() {
        mMarker = mAMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .draggable(true)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_site_2)));

        //设置Marker在屏幕上不跟随地图移动
        mMarker.setPositionByPixels(mMapView.getWidth() / 2, mMapView.getHeight() / 2);
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);   //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.setGpsFirst(false);  //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.setHttpTimeOut(30000);   //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        option.setInterval(2000);   //可选，设置定位间隔。默认为2秒
        option.setNeedAddress(true);    //可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(true);   //可选，设置是否单次定位。默认是false
        option.setOnceLocationLatest(false);    //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);   //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        option.setSensorEnable(false);  //可选，设置是否使用传感器。默认是false
        option.setWifiScan(true);   //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        option.setLocationCacheEnable(true);    //可选，设置是否使用缓存定位，默认为true
        return option;
    }

    private void geo(LatLng target) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(target.latitude, target.longitude), 1000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }

    /**
     * 开始进行poi搜索
     * @param target       经纬度
     * @param addressName  详细地址
     */
    protected void doSearchQuery(LatLng target, String addressName) {
        mQuery = new PoiSearch.Query(addressName, "", "");  // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域(空字符串代表全国)
        mQuery.setPageSize(20); // 设置每页最多返回多少条poi item
        mQuery.setPageNum(mCurrentPage);    // 设置查第一页

        if (mLatLng != null) {
            mPoiSearch = new PoiSearch(this, mQuery);
            mPoiSearch.setOnPoiSearchListener(this);
            LatLonPoint latLonPoint = new LatLonPoint(target.latitude, target.longitude);
            mPoiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 1000, true));  //设置搜索区域为以当前位置为圆心，其周围1000米范围
//            mPoiSearch.searchPOI(); //同步搜索
            mPoiSearch.searchPOIAsyn(); // 异步搜索
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
                startLocation();
                break;
        }
    }

    @Override
    public void onItemClick(NearAddressBean nearAddressBean, int position) {
        if (mNearAddressList != null && mNearAddressList.size() > 0) {
            for (int i = 0; i < mNearAddressList.size(); i++) {
                if (i == position) {
                    mNearAddressList.get(i).setSelect(true);
                } else {
                    mNearAddressList.get(i).setSelect(false);
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //这个target就是地图移动过后中心点的经纬度
        mTarget = cameraPosition.target;
        //这个方法是逆地理编码解析出详细位置
        geo(mTarget);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int poiCode) {
        if (poiCode == AMapException.CODE_AMAP_SUCCESS) {
            if (poiResult != null && poiResult.getQuery() != null) {    // 搜索poi的结果
                if (poiResult.getQuery().equals(mQuery)) {  // 是否是同一条
                    mPoiItemList = poiResult.getPois(); // 取得第一页的poi item数据，页数从数字0开始

                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poi item数据时，会返回含有搜索关键字的城市信息

                    if (mPoiItemList != null && mPoiItemList.size() > 0) {
                        mNearAddressList = new ArrayList<>();

                        for (int i = 0; i < mPoiItemList.size(); i++) {
                            NearAddressBean nearAddressBean = new NearAddressBean();

                            if (i == 0) {
                                nearAddressBean.setSelect(true);
                            } else {
                                nearAddressBean.setSelect(false);
                            }

                            nearAddressBean.setName(mPoiItemList.get(i).getTitle());
                            nearAddressBean.setAddress(mPoiItemList.get(i).getSnippet());

                            mNearAddressList.add(nearAddressBean);
                        }

                        mAdapter.setNearAddressList(mNearAddressList);
                        mRecyclerView.setAdapter(mAdapter);

                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "对不起，没有搜索到相关数据！", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, poiCode, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String information = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            information += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }

        Toast.makeText(this, information, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        //如果逆地理编码成功，就获取到中心点的详细位置，并且在TextView中进行显示，就如同一开始的那张图片所示。
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                    && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                doSearchQuery(mTarget, addressName);
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
