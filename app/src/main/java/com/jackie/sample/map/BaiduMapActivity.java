package com.jackie.sample.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.jackie.sample.R;
import com.jackie.sample.bean.MapInfo;
import com.jackie.sample.listener.MyOrientationListener;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Jackie on 2017/6/2.
 * 百度地图
 */

public class BaiduMapActivity extends AppCompatActivity {
    //地图控件
    private MapView mMapView;
    //地图实例
    private BaiduMap mBaiduMap;
    //定位客户端
    private LocationClient mLocationClient;
   //定位的监听器
    private MyLocationListener mLocationListener;
    //方向传感器的监听器
    private MyOrientationListener mOrientationListener;
    //当前定位的模式
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

    //方向传感器X方向的值
    private int mDirectionX;
    //当前精度
    private float mCurrentAccuracy;

    //是否是第一次定位
    private volatile boolean mIsFirstLocation = true;
    //最新一次的经纬度
    private double mCurrentLatitude;
    private double mCurrentLongitude;

    //地图定位的模式
    private String[] mStyles = new String[] { "地图模式【正常】", "地图模式【跟随】",
            "地图模式【罗盘】" };
    private int mCurrentStyle = 0;

    private BitmapDescriptor mIconMaker;
    //详细信息的布局
    private RelativeLayout mMarkerInfoLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要在setContentView方法之前调用
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);

        mMapView = (MapView) findViewById(R.id.map_view);
        mMarkerInfoLayout = (RelativeLayout) findViewById(R.id.marker_info);
        mBaiduMap = mMapView.getMap();
        mIconMaker = BitmapDescriptorFactory.fromResource(R.drawable.maker);

        //构造一个更新地图的mapStatusUpdate对象，然后设置该对象为缩放等级14.0，最后设置地图状态。
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);

        //初始化定位
        initMyLocation();
        //初始化传感器
        initOrientationListener();
        initMarkerClickEvent();
        initMapClickEvent();

        //第一次定位
        mIsFirstLocation = true;
    }

    /**
     * 初始化定位
     */
    private void initMyLocation() {
        //定位初始化
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        //设置定位的相关配置
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setOpenGps(true); //打开GPS
        locationClientOption.setCoorType("bd09ll");  //设置坐标类型
        locationClientOption.setScanSpan(1000);  //设置发起定位请求的间隔时间为1000ms 不设置或设置数值小于1000ms标识只定位一次
        mLocationClient.setLocOption(locationClientOption);
    }

    /**
     * 实现定位回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //map view销毁后不再处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }

            /**
             * 61 ： GPS定位结果
             * 62 ： 扫描整合定位依据失败。此时定位结果无效。
             * 63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
             * 65 ： 定位缓存的结果。
             * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
             * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
             * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
             * 161： 表示网络定位结果
             * 162~167： 服务端定位失败
             * 502：key参数错误
             * 505：key不存在或者非法
             * 601：key服务被开发者自己禁用
             * 602：key code不匹配
             * 501～700：key验证失败
             */
            int code = bdLocation.getLocType();
            if (code == 161) {
                //构造定位数据
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        //此处设置开发者获取的方向信息，顺时针0-360
                        .direction(mDirectionX)
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();

                mCurrentAccuracy = bdLocation.getRadius();
                //设置定位数据
                mBaiduMap.setMyLocationData(myLocationData);

                mCurrentLatitude = bdLocation.getLatitude();
                mCurrentLongitude = bdLocation.getLongitude();

                //设置自定义图标
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.navi_map_gps_locked);
                MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                        mCurrentMode, true, bitmapDescriptor);
                mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);

                //第一次定位时，将地图位置移动到当前位置
                if (mIsFirstLocation) {
                    mIsFirstLocation = false;

                    LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    mBaiduMap.animateMapStatus(mapStatusUpdate);
                }
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void initOrientationListener() {
        mOrientationListener = new MyOrientationListener(getApplicationContext());
        mOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mDirectionX = (int) x;

                //构造定位数据
                MyLocationData myLocationData = new MyLocationData.Builder()
                        .accuracy(mCurrentAccuracy)
                        .direction(mDirectionX)
                        .latitude(mCurrentLatitude)
                        .longitude(mCurrentLongitude).build();

                mBaiduMap.setMyLocationData(myLocationData);

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.navi_map_gps_locked);
                MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                        mCurrentMode, true, bitmapDescriptor);
                mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);

            }
        });
    }

    private void initMarkerClickEvent() {
        //对Marker的点击
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //获得marker中的数据
                MapInfo mapInfo = (MapInfo) marker.getExtraInfo().get("MapInfo");

//                //官方提供的InfoWindow，弹出后只显示地点的名称
//                InfoWindow infoWindow;
//                //生成一个TextView用于在地图中显示InfoWindow
//                TextView textView = new TextView(getApplicationContext());
//                textView.setBackgroundResource(R.drawable.location_tips);
//                textView.setPadding(30, 20, 30, 50);
//                textView.setText(mapInfo.getName());
//                //将marker所在的经纬度的信息转化成屏幕上的坐标
//                Point point = mBaiduMap.getProjection().toScreenLocation(marker.getPosition());
//                LatLng latLng = mBaiduMap.getProjection().fromScreenLocation(point);
//                //为弹出的InfoWindow添加点击事件
//                infoWindow = new InfoWindow(textView, latLng, -47);
//
////                infoWindow = new InfoWindow(mIconMaker, latLng, point.y, new OnInfoWindowClickListener() {
////                    @Override
////                    public void onInfoWindowClick() {
////                        //隐藏InfoWindow
////                        mBaiduMap.hideInfoWindow();
////                    }
////                });
//
//                //显示InfoWindow
//                mBaiduMap.showInfoWindow(infoWindow);
                //设置详细信息布局为可见
                mMarkerInfoLayout.setVisibility(View.VISIBLE);
                //根据商家信息为详细信息布局设置信息
                popupInfo(mMarkerInfoLayout, mapInfo);
                return true;
            }
        });
    }

    private void initMapClickEvent() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMarkerInfoLayout.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /**
     * 根据mapInfo为布局上的控件设置信息
     * @param relativeLayout
     * @param mapInfo
     */
    private void popupInfo(RelativeLayout relativeLayout, MapInfo mapInfo) {
        ViewHolder viewHolder;
        if (relativeLayout.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.infoImage = (ImageView) relativeLayout.findViewById(R.id.info_image);
            viewHolder.infoName = (TextView) relativeLayout.findViewById(R.id.info_name);
            viewHolder.infoDistance = (TextView) relativeLayout.findViewById(R.id.info_distance);
            viewHolder.infoZan = (TextView) relativeLayout.findViewById(R.id.info_zan);

            relativeLayout.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) relativeLayout.getTag();
        viewHolder.infoImage.setImageResource(mapInfo.getImageId());
        viewHolder.infoDistance.setText(mapInfo.getDistance());
        viewHolder.infoName.setText(mapInfo.getName());
        viewHolder.infoZan.setText(mapInfo.getZan() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 默认点击menu菜单，菜单项不显示图标，反射强制显示
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (/* featureId == Window.FEATURE_OPTIONS_PANEL && */ menu != null) {
            if ("MenuBuilder".equals(menu.getClass().getSimpleName())) {
                try {
                    Method method = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map_common:
                //普通地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.menu_map_site:
                //卫星地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.menu_map_traffic:
                //开启交通图
                if (mBaiduMap.isTrafficEnabled()) {
                    item.setTitle("开启实时交通");
                    mBaiduMap.setTrafficEnabled(false);
                } else {
                    item.setTitle("关闭实时交通");
                    mBaiduMap.setTrafficEnabled(true);
                }
                break;
            case R.id.menu_map_location:
                center2MyLocation();
                break;
            case R.id.id_menu_map_marker:
                //添加覆盖物
                addMapInfoOverlay(MapInfo.MapInfoList);
                break;
            case R.id.menu_map_style:
                mCurrentStyle = (++mCurrentStyle) % mStyles.length;
                item.setTitle(mStyles[mCurrentStyle]);

                switch (mCurrentStyle) {
                    case 0:
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        break;
                    case 1:
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        break;
                    case 2:
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        break;
                }

                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                        .fromResource(R.drawable.navi_map_gps_locked);
                MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(
                        mCurrentMode, true, bitmapDescriptor);
                mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);
                break;
            case R.id.menu_map_offline:
                Intent intent = new Intent(this, OfflineMapActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 地图移动到我的位置，此处可以重新发定位请求，然后定位
     * 直接拿最近一次经纬度，如果长时间没有定位成功，可能会显示不好
     */
    private void center2MyLocation() {
        LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    private void addMapInfoOverlay(List<MapInfo> mapInfoList) {
        mBaiduMap.clear();

        LatLng latLng = null;
        OverlayOptions overlayOptions;
        Marker marker;

        for (MapInfo mapInfo : mapInfoList) {
            //位置
            latLng = new LatLng(mapInfo.getLatitude(), mapInfo.getLongitude());
            //图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);

            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("MapInfo", mapInfo);
            marker.setExtraInfo(bundle);
        }

        //将地图移动到最后一个经纬度位置
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();  //开始定位
        }

        //开启方向传感器
        mOrientationListener.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //在activity执行onDestroy时执行mMapView.onResume()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //在activity执行onPause时执行mMapView.onPause()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();

        //关闭方向传感器
        mOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mIconMaker.recycle();
        mMapView = null;
    }

    private class ViewHolder {
        ImageView infoImage;
        TextView infoName;
        TextView infoDistance;
        TextView infoZan;
    }
}
