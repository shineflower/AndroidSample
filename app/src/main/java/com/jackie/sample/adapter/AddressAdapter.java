package com.jackie.sample.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.WrappedAddressDataBean;

import java.util.List;

/**
 * Created by Jackie on 2017/6/23
 * 地址选择适配器
 */

public class AddressAdapter extends BaseAdapter {
	private Context mContext;
	private List<WrappedAddressDataBean> mWrappedAddressDataList;
	private LayoutInflater mLayoutInflater;

	public AddressAdapter(Context context, List<WrappedAddressDataBean> wrappedAddressDataList) {
		mContext = context;
		mWrappedAddressDataList = wrappedAddressDataList;

		mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mWrappedAddressDataList.size();
	}

	@Override
	public WrappedAddressDataBean getItem(int position) {
		return mWrappedAddressDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		WrappedAddressDataBean wrappedAddressDataBean = mWrappedAddressDataList.get(position);

		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_address_select, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.addressName.setText(wrappedAddressDataBean.name);

		if (wrappedAddressDataBean.isCheck) {
			holder.addressName.setTextColor(Color.parseColor("#0971ce"));
			holder.addressName.getPaint().setFakeBoldText(true);
			holder.icon.setVisibility(View.VISIBLE);
		} else {
			holder.addressName.setTextColor(Color.parseColor("#333333"));
			holder.addressName.getPaint().setFakeBoldText(false);
			holder.icon.setVisibility(View.GONE);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView addressName;
		ImageView icon;

		public ViewHolder(View view) {
			this.addressName = (TextView) view.findViewById(R.id.tv_address_name);
			this.icon = (ImageView) view.findViewById(R.id.iv_icon);
		}
	}

}
