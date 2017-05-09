package com.jackie.sample.listener;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.jackie.sample.adapter.SlideDeleteAdapter;
import com.jackie.sample.bean.SlideDeleteBean;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jackie on 16/10/6.
 */
public class SimpleItemTouchCallback extends ItemTouchHelper.Callback {
    private SlideDeleteAdapter mAdapter;
    private List<SlideDeleteBean> mList;
    public SimpleItemTouchCallback(SlideDeleteAdapter adapter, List<SlideDeleteBean> list){
        mAdapter = adapter;
        mList = list;
    }

    /**
     * 设置支持的拖拽、滑动的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();
        Collections.swap(mList, from, to);
        mAdapter.notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        mList.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    /**
     * 状态改变时回调
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        Log.i("Callback", actionState + "");
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            SlideDeleteAdapter.ViewHolder holder = (SlideDeleteAdapter.ViewHolder)viewHolder;
            holder.itemView.setBackgroundColor(0xffbcbcbc);
        }
    }

    /**
     * 拖拽或滑动完成之后调用，用来清除一些状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        SlideDeleteAdapter.ViewHolder holder = (SlideDeleteAdapter.ViewHolder)viewHolder;
        holder.itemView.setBackgroundColor(0xffeeeeee);
    }
}
