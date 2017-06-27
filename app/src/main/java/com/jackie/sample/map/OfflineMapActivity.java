package com.jackie.sample.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.jackie.sample.R;
import com.jackie.sample.bean.OfflineMapCityBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/5.
 */

public class OfflineMapActivity extends AppCompatActivity {
    //离线地图功能
    private MKOfflineMap mOfflineMap;
    private ListView mListView;
    //离线地图数据
    private List<OfflineMapCityBean> mList = new ArrayList<>();
    private OfflineMapAdapter mAdapter;
    private LayoutInflater mInflater;

    //目前加入下载队列的城市
    private List<Integer> mCityCodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_offline_map);

        mInflater = LayoutInflater.from(this);

        //初始化离线地图
        initOfflineMap();

        //初始化ListView数据
        initData();

        initView();
    }

    private void initOfflineMap() {
        mOfflineMap = new MKOfflineMap();
        //设置监听
        mOfflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int type, int state) {
                switch (type) {
                    case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
                        //离线地图下载更新事件类型
                        MKOLUpdateElement updateElement = mOfflineMap.getUpdateInfo(state);
                        for (OfflineMapCityBean offlineMapCityBean : mList) {
                            if (offlineMapCityBean.getCityCode() == state) {
                                offlineMapCityBean.setProgress(updateElement.ratio);
                                offlineMapCityBean.setFlag(OfflineMapCityBean.Flag.DOWNLOADING);
                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        break;
                    case MKOfflineMap.TYPE_NEW_OFFLINE:
                        //有新离线地图安装
                        break;
                    case MKOfflineMap.TYPE_VER_UPDATE:
                        //版本更新提示
                        break;
                }
            }
        });
    }

    private void initData() {
        //获取所有热门城市
        List<MKOLSearchRecord> offlineHotCityList = mOfflineMap.getHotCityList();
        //手动添加西安
//        MKOLSearchRecord xian = new MKOLSearchRecord();
//        xian.cityID = 233;
//        xian.cityName="西安市";
//        offlineHotCityList.add(xian);

        //获取所有已经下载的城市列表
        List<MKOLUpdateElement> allUpdateElementList = mOfflineMap.getAllUpdateInfo();
        //设置所有数据的状态
        for (MKOLSearchRecord mkolSearchRecord : offlineHotCityList) {
            OfflineMapCityBean cityBean = new OfflineMapCityBean();
            cityBean.setCityName(mkolSearchRecord.cityName);
            cityBean.setCityCode(mkolSearchRecord.cityID);

            if (allUpdateElementList != null) {
                for (MKOLUpdateElement updateElement : allUpdateElementList) {
                    if (updateElement.cityID == mkolSearchRecord.cityID) {
                        cityBean.setProgress(updateElement.ratio);
                    }
                }
            }

            mList.add(cityBean);
        }
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new OfflineMapAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int cityId = mList.get(position).getCityCode();
                if (mCityCodes.contains(cityId)) {
                    removeTaskFromQueue(position, cityId);
                } else {
                    addTaskToQueue(position, cityId);
                }
            }
        });
    }

    /**
     * 将任务移除下载队列
     * @param position
     * @param cityId
     */
    private void removeTaskFromQueue(int position, int cityId) {
        mCityCodes.remove(mCityCodes.indexOf(cityId));
        mOfflineMap.pause(cityId);
        mList.get(position).setFlag(OfflineMapCityBean.Flag.NO_STATUS);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 将下载任务添加至下载队列
     * @param position
     * @param cityId
     */
    private void addTaskToQueue(int position, int cityId) {
        mCityCodes.add(cityId);
        mOfflineMap.start(cityId);
        mList.get(position).setFlag(OfflineMapCityBean.Flag.PAUSE);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 热门城市地图列表的Adapter
     */
    private class OfflineMapAdapter extends BaseAdapter {

        @Override
        public boolean isEnabled(int position) {
            if (mList.get(position).getProgress() == 100) {
                return false;
            }

            return super.isEnabled(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OfflineMapCityBean offlineMapCityBean = mList.get(position);
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_offline_map, parent, false);
                holder.cityName = (TextView) convertView.findViewById(R.id.city_name);
                holder.progress = (TextView) convertView.findViewById(R.id.progress);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cityName.setText(offlineMapCityBean.getCityName());
            int progress = offlineMapCityBean.getProgress();
            String progressMsg;
            // 根据进度情况，设置显示
            if (progress == 0) {
                progressMsg = "未下载";
            } else if (progress == 100) {
                offlineMapCityBean.setFlag(OfflineMapCityBean.Flag.NO_STATUS);
                progressMsg = "已下载";
            } else {
                progressMsg = progress + "%";
            }

            // 根据当前状态，设置显示
            switch (offlineMapCityBean.getFlag()) {
                case PAUSE:
                    progressMsg += "【等待下载】";
                    break;
                case DOWNLOADING:
                    progressMsg += "【正在下载】";
                    break;
                default:
                    break;
            }

            holder.progress.setText(progressMsg);
            return convertView;
        }

        private class ViewHolder {
            TextView cityName;
            TextView progress;
        }
    }
}
