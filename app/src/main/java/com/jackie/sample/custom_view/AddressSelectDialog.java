package com.jackie.sample.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.adapter.AddressAdapter;
import com.jackie.sample.bean.WrappedAddressDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/23.
 * 地址选择器
 */

public class AddressSelectDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Context mContext;

    private TextView mTextViewP;  //省   province
    private TextView mTextViewC;  //市   city
    private TextView mTextViewD;  //区   district
    private TextView mTextViewS;  //街道 street

    private View mViewP;
    private View mViewC;
    private View mViewD;
    private View mViewS;

    private ListView mListViewP;
    private ListView mListViewC;
    private ListView mListViewD;
    private ListView mListViewS;

    private List<WrappedAddressDataBean> mProvinceList;
    private AddressAdapter mProvinceAdapter;
    private List<WrappedAddressDataBean> mCityList;
    private AddressAdapter mCityAdapter;
    private List<WrappedAddressDataBean> mDistrictList;
    private AddressAdapter mDistrictAdapter;
    private List<WrappedAddressDataBean> mStreetList;
    private AddressAdapter mStreetAdapter;

    private String mProvince = "";
    private String mCity = "";
    private String mDistrict = "";
    private String mStreet = "";

    private int mLastProvincePosition = -1;
    private int mLastCityPosition = -1;
    private int mLastDistrictPosition = -1;
    private int mLastStreetPosition = -1;

    public interface onAddressListener {
        void onAddress(String province, String city, String district , String street);
    }

    public onAddressListener mOnAddressListener;

    public void setOnAddressListener(onAddressListener onAddressListener) {
        mOnAddressListener = onAddressListener;
    }

    public AddressSelectDialog(@NonNull Context context) {
        this(context, R.style.Theme_Light_NoTitle_Dialog);
    }

    public AddressSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
       super(context, themeResId);

        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.address_select_dialog);
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);

        initView();
        initData();
    }

    private void initView() {
        //4个按钮
        mTextViewP = (TextView) findViewById(R.id.tv_p);
        mTextViewC = (TextView) findViewById(R.id.tv_c);
        mTextViewD = (TextView) findViewById(R.id.tv_d);
        mTextViewS = (TextView) findViewById(R.id.tv_s);

        //4个指示器
        mViewP = findViewById(R.id.view_p);
        mViewC = findViewById(R.id.view_c);
        mViewD = findViewById(R.id.view_d);
        mViewS = findViewById(R.id.view_s);

        //4个列表
        mListViewP = (ListView) findViewById(R.id.lv_p);
        mListViewC = (ListView) findViewById(R.id.lv_c);
        mListViewD = (ListView) findViewById(R.id.lv_d);
        mListViewS = (ListView) findViewById(R.id.lv_s);

        //4个点击事件
        mTextViewP.setOnClickListener(this);
        mTextViewC.setOnClickListener(this);
        mTextViewD.setOnClickListener(this);
        mTextViewS.setOnClickListener(this);
    }

    private void initData() {
        //		省的假数据
        WrappedAddressDataBean provinceBean1 = new WrappedAddressDataBean("浙江省", false);
        WrappedAddressDataBean provinceBean2 = new WrappedAddressDataBean("山东省", false);
        WrappedAddressDataBean provinceBean3 = new WrappedAddressDataBean("安徽省", false);

        mProvinceList = new ArrayList<>();
        mProvinceList.add(provinceBean1);
        mProvinceList.add(provinceBean2);
        mProvinceList.add(provinceBean3);

        mProvinceAdapter = new AddressAdapter(mContext, mProvinceList);
        mListViewP.setAdapter(mProvinceAdapter);
        mListViewP.setOnItemClickListener(this);

        //		市的假数据
        WrappedAddressDataBean cityBean1 = new WrappedAddressDataBean("杭州市", false);
        WrappedAddressDataBean cityBean2 = new WrappedAddressDataBean("聊城市", false);
        WrappedAddressDataBean cityBean3 = new WrappedAddressDataBean("长春市", false);

        mCityList = new ArrayList<>();
        mCityList.add(cityBean1);
        mCityList.add(cityBean2);
        mCityList.add(cityBean3);

        mCityAdapter = new AddressAdapter(mContext, mCityList);
        mListViewC.setAdapter(mCityAdapter);
        mListViewC.setOnItemClickListener(this);

        //		区的假数据
        WrappedAddressDataBean districtBean1 = new WrappedAddressDataBean("江干区", false);
        WrappedAddressDataBean districtBean2 = new WrappedAddressDataBean("白洋淀", false);
        WrappedAddressDataBean districtBean3 = new WrappedAddressDataBean("朝阳区", false);

        mDistrictList = new ArrayList<>();
        mDistrictList.add(districtBean1);
        mDistrictList.add(districtBean2);
        mDistrictList.add(districtBean3);

        mDistrictAdapter = new AddressAdapter(mContext, mDistrictList);
        mListViewD.setAdapter(mDistrictAdapter);
        mListViewD.setOnItemClickListener(this);

        //     街道的假数据
        WrappedAddressDataBean streetBean1 = new WrappedAddressDataBean("白杨街道", false);
        WrappedAddressDataBean streetBean2 = new WrappedAddressDataBean("江南街道", false);
        WrappedAddressDataBean streetBean3 = new WrappedAddressDataBean("长河街道", false);

        mStreetList = new ArrayList<>();
        mStreetList.add(streetBean1);
        mStreetList.add(streetBean2);
        mStreetList.add(streetBean3);

        mStreetAdapter = new AddressAdapter(mContext, mStreetList);
        mListViewS.setAdapter(mStreetAdapter);
        mListViewS.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WrappedAddressDataBean wrappedAddressDataBean;
        List<WrappedAddressDataBean> queriedDistrictList = null;
        List<WrappedAddressDataBean> queriedStreetList = null;

        switch (parent.getId()) {
            case R.id.lv_p:
                wrappedAddressDataBean = mProvinceList.get(position);

                if (wrappedAddressDataBean.isCheck) {   //	如果被点击的条目已经被选中了, 什么都不做
                    return;
                }

                wrappedAddressDataBean.isCheck = true;  //	如果被点击的条目不是上次被选中的

                mTextViewP.setText(wrappedAddressDataBean.name);
                checkButton(mTextViewC);
                mTextViewC.setText("选择市");
                mTextViewD.setText("");
                mTextViewS.setText("");

                if (mLastProvincePosition > -1) {   //	如果这个ListView不是第一次被点击
                    mProvinceList.get(mLastProvincePosition).isCheck = false;   //	重置上次的点击标志
                    mLastCityPosition = -1;
                    mLastDistrictPosition = -1;
                    mTextViewD.setText("");

                    mCityAdapter.notifyDataSetChanged();
                    mDistrictAdapter.notifyDataSetChanged();
                }

                mLastProvincePosition = position;
                mProvinceAdapter.notifyDataSetChanged();
                //	跳到市的集合列表
                mCityAdapter.notifyDataSetChanged();
                showListView(mListViewC);
                break;
            case R.id.lv_c:
                wrappedAddressDataBean = mCityList.get(position);

                if (queriedDistrictList != null && queriedDistrictList.size() == 1 && TextUtils.isEmpty(queriedDistrictList.get(0).name)) {    //	没有第三级
                    if (wrappedAddressDataBean.isCheck) {   //	如果已经被选中, 什么都不做, 直接dismiss
                        this.dismiss();
                        return;
                    }

                    wrappedAddressDataBean.isCheck = true;  //	如果没有被选中, 先让状态变成选中
                    mTextViewC.setText(wrappedAddressDataBean.name);
                    mTextViewD.setText("");
                    mTextViewS.setText("");

                    if (mLastCityPosition > -1) {   //	如果不是第一次被选中, 清除之前的状态
                        mCityList.get(mLastCityPosition).isCheck = false;
                        mLastDistrictPosition = -1;
                        mDistrictAdapter.notifyDataSetChanged();
                    }

                    mLastCityPosition = position;
                    mCityAdapter.notifyDataSetChanged();

                    mProvince = mTextViewP.getText().toString().trim();
                    mCity = mTextViewC.getText().toString().trim();
                    mDistrict = "";
                    mStreet = "";

                    if (mOnAddressListener != null) {
                        mOnAddressListener.onAddress(mProvince, mCity, mDistrict, mStreet);
                    }

                    this.dismiss();
                } else {    //如果有第三级
                    if (wrappedAddressDataBean.isCheck) {
                        return;
                    }

                    wrappedAddressDataBean.isCheck = true;

                    mTextViewC.setText(wrappedAddressDataBean.name);
                    checkButton(mTextViewD);
                    mTextViewD.setText("选择区");
                    mTextViewS.setText("");

                    if (mLastCityPosition > -1) {
                        mCityList.get(mLastCityPosition).isCheck = false;
                        mLastDistrictPosition = -1;
                        mDistrictAdapter.notifyDataSetChanged();
                    }

                    mLastCityPosition = position;
                    mCityAdapter.notifyDataSetChanged();
                    mDistrictAdapter.notifyDataSetChanged();
                    showListView(mListViewD);
                }
                break;
            case R.id.lv_d:
                wrappedAddressDataBean = mDistrictList.get(position);

                if (queriedStreetList != null && queriedStreetList.size() == 1 && TextUtils.isEmpty(queriedStreetList.get(0).name)) {
                    if (wrappedAddressDataBean.isCheck) {
                        this.dismiss();
                        return;
                    }

                    wrappedAddressDataBean.isCheck = true;
                    mTextViewD.setText(wrappedAddressDataBean.name);
                    mTextViewS.setText("");

                    if (mLastDistrictPosition > -1) {
                        mDistrictList.get(mLastDistrictPosition).isCheck = false;
                        mLastStreetPosition = -1;
                        mStreetAdapter.notifyDataSetChanged();
                    }

                    mLastStreetPosition = position;
                    mDistrictAdapter.notifyDataSetChanged();

                    mProvince = mTextViewP.getText().toString().trim();
                    mCity = mTextViewC.getText().toString().trim();
                    mDistrict = mTextViewD.getText().toString().trim();
                    mStreet = "";

                    if (mOnAddressListener != null) {
                        mOnAddressListener.onAddress(mProvince, mCity, mDistrict , mStreet);
                    }

                    this.dismiss();
                } else {
                    if (wrappedAddressDataBean.isCheck) {
                        return;
                    }

                    wrappedAddressDataBean.isCheck = true;

                    mTextViewD.setText(wrappedAddressDataBean.name);
                    checkButton(mTextViewS);
                    mTextViewS.setText("选择街道");

                    if (mLastDistrictPosition > -1) {
                        mDistrictList.get(mLastDistrictPosition).isCheck = false;
                        mLastStreetPosition = -1;
                        mStreetAdapter.notifyDataSetChanged();
                    }

                    mLastDistrictPosition = position;
                    mDistrictAdapter.notifyDataSetChanged();
                    mStreetAdapter.notifyDataSetChanged();
                    showListView(mListViewS);
                }
                break;
            case R.id.lv_s:
                wrappedAddressDataBean = mStreetList.get(position);

                if (wrappedAddressDataBean.isCheck) {
                    this.dismiss();
                    return;
                }

                wrappedAddressDataBean.isCheck = true;

                mTextViewS.setText(wrappedAddressDataBean.name);

                if (mLastStreetPosition > -1) {
                    mStreetList.get(mLastStreetPosition).isCheck = false;
                }

                mLastStreetPosition = position;
                mStreetAdapter.notifyDataSetChanged();

                mProvince = mTextViewP.getText().toString().trim();
                mCity = mTextViewC.getText().toString().trim();
                mDistrict = mTextViewD.getText().toString().trim();
                mStreet = mTextViewS.getText().toString().trim();

                if (mOnAddressListener != null) {
                    mOnAddressListener.onAddress(mProvince, mCity, mDistrict , mStreet);
                }

                this.dismiss();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_p:
                checkButton(mTextViewP);
                showListView(mListViewP);
                break;
            case R.id.tv_c:
                if (mCityList.isEmpty()) {
                    return;
                }

                checkButton(mTextViewC);
                showListView(mListViewC);
                break;
            case R.id.tv_d:
                if (mDistrictList.isEmpty()) {
                    return;
                }

                checkButton(mTextViewD);
                showListView(mListViewD);
                break;
            case R.id.tv_s:
                if (mStreetList.isEmpty()) {
                    return;
                }

                checkButton(mTextViewS);
                showListView(mListViewS);
                break;
        }
    }

    private void checkButton(View view) {
        mTextViewP.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        mTextViewC.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        mTextViewD.setTextColor(mContext.getResources().getColor(R.color.color_333333));
        mTextViewS.setTextColor(mContext.getResources().getColor(R.color.color_333333));

        mViewC.setVisibility(View.INVISIBLE);
        mViewD.setVisibility(View.INVISIBLE);
        mViewP.setVisibility(View.INVISIBLE);
        mViewS.setVisibility(View.INVISIBLE);

        switch (view.getId()) {
            case R.id.tv_p:
                mTextViewP.setTextColor(mContext.getResources().getColor(R.color.color_0971ce));
                mViewP.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_c:
                mTextViewC.setTextColor(mContext.getResources().getColor(R.color.color_0971ce));
                mViewC.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_d:
                mTextViewD.setTextColor(mContext.getResources().getColor(R.color.color_0971ce));
                mViewD.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_s:
                mTextViewS.setTextColor(mContext.getResources().getColor(R.color.color_0971ce));
                mViewS.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showListView(View view) {
        mListViewP.setVisibility(View.GONE);
        mListViewC.setVisibility(View.GONE);
        mListViewD.setVisibility(View.GONE);
        mListViewS.setVisibility(View.GONE);

        switch (view.getId()) {
            case R.id.lv_p:
                mListViewP.setVisibility(View.VISIBLE);
                break;
            case R.id.lv_c:
                mListViewC.setVisibility(View.VISIBLE);
                break;
            case R.id.lv_d:
                mListViewD.setVisibility(View.VISIBLE);
                break;
            case R.id.lv_s:
                mListViewS.setVisibility(View.VISIBLE);
                break;
        }
    }
}
