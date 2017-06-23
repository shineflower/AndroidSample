package com.jackie.sample.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackie.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2017/6/22.
 * 自定义虚拟键盘按键
 */

public class VirtualKeyView extends RelativeLayout {
    private Context mContext;

    //身份证键盘
    public static final int ID_KEYBOARD = 0x000001;
    //小数键盘
    public static final int FLOAT_KEYBOARD = 0x000002;
    //数字键盘
    public static final int NUMBER_KEYBOARD = 0x000003;

    private GridView mGridView;   //用GridView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能

    private ArrayList<String> mKeyList;
    private KeyBoardAdapter mAdapter;

    public VirtualKeyView(Context context) {
        this(context, null);
    }

    public VirtualKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VirtualKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initView();
    }

    private void initView() {
        View view = View.inflate(mContext, R.layout.virtual_key_view, null);

        mGridView = (GridView) view.findViewById(R.id.grid_view);

        mKeyList = new ArrayList<>();

        addView(view);
    }

    public void initData(int type) {
        switch(type) {
            case ID_KEYBOARD:
                for (int i = 1; i < 13; i++) {
                    if (i < 10) {
                        mKeyList.add(String.valueOf(i));
                    } else if (i == 10) {
                        mKeyList.add("X");
                    } else if (i == 11) {
                        mKeyList.add(String.valueOf(0));
                    } else if (i == 12) {
                        mKeyList.add("");
                    }
                }
                break;
            case FLOAT_KEYBOARD:
                for (int i = 1; i < 13; i++) {
                    if (i < 10) {
                        mKeyList.add(String.valueOf(i));
                    } else if (i == 10) {
                        mKeyList.add("");
                    } else if (i == 11) {
                        mKeyList.add(String.valueOf(0));
                    } else if (i == 12) {
                        mKeyList.add(".");
                    }
                }
                break;
            case NUMBER_KEYBOARD:
                for (int i = 1; i < 13; i++) {
                    if (i < 10) {
                        mKeyList.add(String.valueOf(i));
                    } else if (i == 10) {
                        mKeyList.add("");
                    } else if (i == 11) {
                        mKeyList.add(String.valueOf(0));
                    } else if (i == 12) {
                        mKeyList.add("");
                    }
                }
                break;
        }

        mAdapter = new KeyBoardAdapter(mContext);
        mAdapter.setKeyList(mKeyList);
        mGridView.setAdapter(mAdapter);
    }

    private class KeyBoardAdapter extends BaseAdapter {
        private Context context;
        private List<String> keyList;

        public KeyBoardAdapter(Context context) {
            this.context = context;
        }

        public void setKeyList(@NonNull List<String> keyList) {
            this.keyList = keyList;
        }

        @Override
        public int getCount() {
            return keyList.size();
        }

        @Override
        public Object getItem(int position) {
            return keyList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_keyboard, null);

                holder = new ViewHolder();
                holder.key = (TextView) convertView.findViewById(R.id.key);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if ("".equals(keyList.get(position))) {
                holder.key.setBackgroundResource(R.drawable.shape_keyboard_normal);
            } else {
                holder.key.setVisibility(View.VISIBLE);
            }

            holder.key.setText(keyList.get(position));

            return convertView;
        }

        private class ViewHolder {
            TextView key;
        }
    }

    public ArrayList<String> getKeyList() {
        return mKeyList;
    }

    public GridView getGridView() {
        return mGridView;
    }
}
