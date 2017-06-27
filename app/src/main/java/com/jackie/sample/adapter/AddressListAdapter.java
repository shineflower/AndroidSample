package com.jackie.sample.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.NearAddressBean;

import java.util.List;


/**
 * Created by Jackie on 2017/6/27
 * 高德地图地址列表适配器
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    private Context mContext;
    private List<NearAddressBean> mNearAddressList;
    private OnItemClickListener mOnItemClickListener;

    public AddressListAdapter(Context context) {
        mContext = context;
    }

    public void setNearAddressList(List<NearAddressBean> nearAddressList) {
        this.mNearAddressList = nearAddressList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_map_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final NearAddressBean nearAddressBean = mNearAddressList.get(position);

        holder.title.setText(nearAddressBean.getName());
        holder.address.setText(nearAddressBean.getAddress());

        if (nearAddressBean.isSelect()) {
            holder.icon.setSelected(true);
            holder.title.setSelected(true);
            holder.address.setSelected(true);
            holder.choose.setVisibility(View.VISIBLE);
        } else {
            holder.icon.setSelected(false);
            holder.title.setSelected(false);
            holder.address.setSelected(false);
            holder.choose.setVisibility(View.GONE);
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(nearAddressBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, address;
        private ImageView icon, choose;
        private RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            choose = (ImageView) itemView.findViewById(R.id.iv_choose);
            itemLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(NearAddressBean nearAddressBean, int position);
    }
}
