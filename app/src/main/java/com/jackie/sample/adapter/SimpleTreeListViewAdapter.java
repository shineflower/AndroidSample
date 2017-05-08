package com.jackie.sample.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.bean.FileBean;
import com.jackie.sample.bean.Node;
import com.jackie.sample.utils.TreeViewHelper;

import java.util.List;

/**
 * Created by Jackie on 2016/1/14.
 * 适配器
 */
public class SimpleTreeListViewAdapter extends TreeListViewAdapter<FileBean> {
    public SimpleTreeListViewAdapter(Context context, ListView listView, List<FileBean> nodes, int defaultExpandLevel) throws IllegalAccessException {
        super(context, listView, nodes, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_tree, parent, false);
            holder = new ViewHolder();
            holder.mIcon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.mName = (TextView) convertView.findViewById(R.id.item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            holder.mIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageResource(node.getIcon());
        }
        holder.mName.setText(node.getName());

        return convertView;
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mName;
    }

    /**
     * 动态添加节点
     * @param position
     * @param name
     */
    public void addExtraNode(int position, String name) {
        Node node = mVisibleNodes.get(position);
        //创建的节点存放的位置是在mAllNodes中
        int index = mAllNodes.indexOf(node);
        Node extraNode = new Node(-1, node.getId(), name);
        //设置节点关系
        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        if (!node.isExpand()) {
            node.setExpand(true);
        }
        //将创建的节点放入mAllNotes的指定位置后面
        mAllNodes.add(index + 1, extraNode);
        mVisibleNodes = TreeViewHelper.filterVisibleNodes(mAllNodes);
        notifyDataSetChanged();
    }
}
