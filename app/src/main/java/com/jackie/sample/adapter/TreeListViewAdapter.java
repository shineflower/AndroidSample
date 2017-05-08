package com.jackie.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jackie.sample.bean.Node;
import com.jackie.sample.utils.TreeViewHelper;

import java.util.List;

/**
 * Created by Jackie on 2016/1/12.
 */
public abstract class TreeListViewAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected ListView mListView;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;

    protected LayoutInflater mInflater;

    public interface OnTreeNoteClickListener {
        void onClick(Node node, int position);
    }

    public OnTreeNoteClickListener mOnTreeNoteClickListener;

    public void setTreeNoteClickListener(OnTreeNoteClickListener onTreeNoteClickListener) {
        this.mOnTreeNoteClickListener = onTreeNoteClickListener;
    }

    public TreeListViewAdapter(Context context, ListView listView, List<T> nodes, int defaultExpandLevel) throws IllegalAccessException {
        this.mContext = context;
        this.mListView = listView;

        mAllNodes = TreeViewHelper.getSortedNodes(nodes, defaultExpandLevel);
        mVisibleNodes = TreeViewHelper.filterVisibleNodes(mAllNodes);

        mInflater = LayoutInflater.from(context);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);

                if (mOnTreeNoteClickListener != null) {
                    mOnTreeNoteClickListener.onClick(mVisibleNodes.get(position), position);
                }
            }
        });
    }

    private void expandOrCollapse(int position) {
        Node node = mVisibleNodes.get(position);
        if (node != null) {
            if (node.isLeaf()) {
                return;
            }
            node.setExpand(!node.isExpand());

            mVisibleNodes = TreeViewHelper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置内边距
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
