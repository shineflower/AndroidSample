package com.jackie.sample.wechat_slide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.MessageItem;
import com.jackie.sample.custom_view.ListViewCompat;
import com.jackie.sample.custom_view.SlideView;

import java.util.ArrayList;
import java.util.List;

public class WechatSlideActivity extends AppCompatActivity implements OnItemClickListener, SlideView.OnSlideListener {
    private ListViewCompat mListView;
    private SlideAdapter mSlideAdapter;

    private List<MessageItem> mMessageItems = new ArrayList<>();

    private SlideView mLastSlideViewStatusOn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_slide);
        initView();
    }

    private void initView() {
        mListView = (ListViewCompat) findViewById(R.id.list_view);

        for (int i = 0; i < 20; i++) {
            MessageItem messageItem = new MessageItem();

            if (i % 3 == 0) {
                messageItem.setIconRes(R.drawable.default_qq_avatar);
                messageItem.setTitle("腾讯新闻");
                messageItem.setMsg("青岛爆炸满月：大量鱼虾死亡");
                messageItem.setTime("晚上18:18");
            } else {
                messageItem.setIconRes(R.drawable.wechat_icon);
                messageItem.setTitle( "微信团队");
                messageItem.setMsg("欢迎你使用微信");
                messageItem.setTime("12月18日");
            }

            mMessageItems.add(messageItem);
        }

        mSlideAdapter = new SlideAdapter();
        mListView.setAdapter(mSlideAdapter);
        mListView.setOnItemClickListener(this);
    }

    private class SlideAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mMessageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mMessageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            SlideView slideView = (SlideView) convertView;

            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.item_list_view_compat, null);

                slideView = new SlideView(WechatSlideActivity.this);
                slideView.setContentView(itemView);

                holder = new ViewHolder(slideView);
                slideView.setOnSlideListener(WechatSlideActivity.this);
                slideView.setTag(holder);
            } else {
                holder = (ViewHolder) slideView.getTag();
            }

            MessageItem messageItem = mMessageItems.get(position);
            messageItem.setSlideView(slideView);
            messageItem.getSlideView().shrink();

            holder.icon.setImageResource(messageItem.getIconRes());
            holder.title.setText(messageItem.getTitle());
            holder.msg.setText(messageItem.getMsg());
            holder.time.setText(messageItem.getTime());
            holder.deleteHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMessageItems.remove(position);
                    mSlideAdapter.notifyDataSetChanged();
                }
            });

            return slideView;
        }
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.icon);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
            deleteHolder = (ViewGroup) view.findViewById(R.id.delete_holder);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewStatusOn != null && mLastSlideViewStatusOn != view) {
            mLastSlideViewStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewStatusOn = (SlideView) view;
        }
    }
}
