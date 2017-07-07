package com.jackie.sample.contact.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.ContactBean;
import com.jackie.sample.contact.utils.ColorGenerator;
import com.jackie.sample.contact.utils.TextDrawable;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<ContactBean> mContactList;

    // declare the color generator and drawable builder
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();

    public ContactAdapter() {
        mContactList = new ArrayList<>();
    }

    public void addAll(List<ContactBean> contactList) {
        if (mContactList.size() > 0) {
            mContactList.clear();
        }

        mContactList.addAll(contactList);

        notifyDataSetChanged();
    }

    public void add(ContactBean contactBean, int position) {
        mContactList.add(position, contactBean);
        notifyItemInserted(position);
    }

    public void add(ContactBean bean) {
        mContactList.add(bean);
        notifyItemChanged(mContactList.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meizu_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mContactList == null || mContactList.size() == 0 || mContactList.size() <= position) {
            return;
        }

        ContactBean contactBean = mContactList.get(position);

        if (contactBean != null) {
            holder.textView.setText(contactBean.getName());
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(contactBean.getName().charAt(0)), mColorGenerator.getColor(contactBean.getName()));
            holder.imageView.setImageDrawable(drawable);
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.text_view);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}