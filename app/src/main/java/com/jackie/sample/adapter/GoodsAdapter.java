package com.jackie.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jackie.sample.R;
import com.jackie.sample.bean.GoodsBean;

import java.util.ArrayList;

public class GoodsAdapter extends BaseAdapter {
    private ArrayList<GoodsBean> mGoodsBeans;
    private LayoutInflater mLayoutInflater;
    private OnCartItemClickListener mOnCartItemClickListener;

    public interface OnCartItemClickListener {
        void onCartItemClick(ImageView shoppingCartItem);
    }

    public void setOnCartItemClickListener(OnCartItemClickListener onCartItemClickListener){
        this.mOnCartItemClickListener = onCartItemClickListener;
    }

    public GoodsAdapter(Context context, ArrayList<GoodsBean> goodsBeans){
        mLayoutInflater = LayoutInflater.from(context);
        this.mGoodsBeans = goodsBeans;
    }

    @Override
    public int getCount() {
        return mGoodsBeans != null ? mGoodsBeans.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return mGoodsBeans != null ? mGoodsBeans.get(position): null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.item_shopping_cart, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.shoppingCartItem = (ImageView) convertView.findViewById(R.id.iv_shopping_cart_item);
        holder.shoppingCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shoppingCartItem != null && mOnCartItemClickListener != null) {
                    mOnCartItemClickListener.onCartItemClick(holder.shoppingCartItem);
                }
            }
        });

        if (position < mGoodsBeans.size()) {
            holder.updateUI(mGoodsBeans.get(position));
        }

        return convertView;
    }

    private class  ViewHolder {
        private ImageView shoppingCartItem;

        public void updateUI(GoodsBean goodsBean){
            if (goodsBean != null
                    && goodsBean.getGoodsBitmap() != null
                    && shoppingCartItem != null)
                shoppingCartItem.setImageBitmap(goodsBean.getGoodsBitmap());
        }
    }
}