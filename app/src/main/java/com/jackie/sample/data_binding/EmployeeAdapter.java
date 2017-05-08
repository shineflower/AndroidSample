package com.jackie.sample.data_binding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.BR;
import com.jackie.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/29.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<BindingViewHolder> {
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;
    private List<Employee> mEmployeeList;

    public static final int ITEM_VIEW_TYPE_ON = 1;
    public static final int ITEM_VIEW_TYPE_OFF = 2;

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Employee employee);
    }

    public EmployeeAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mEmployeeList = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        Employee employee = mEmployeeList.get(position);
        if (employee.isFired()) {
            return ITEM_VIEW_TYPE_OFF;
        } else {
            return ITEM_VIEW_TYPE_ON;
        }
    }

    @Override
    public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        if (viewType == ITEM_VIEW_TYPE_ON) {
            binding = DataBindingUtil.inflate(mInflater, R.layout.item_employee_on, parent, false);
        } else {
            binding = DataBindingUtil.inflate(mInflater, R.layout.item_employee_off, parent, false);
        }
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder holder, int position) {
        final Employee employee = mEmployeeList.get(position);
        holder.getBinding().setVariable(BR.item_employee, employee);
        holder.getBinding().executePendingBindings();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(employee);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEmployeeList.size();
    }

    public void addAll(List<Employee> employeeList) {
        mEmployeeList.addAll(employeeList);
    }

    public void add(Employee employee) {
        int position = new Random().nextInt(mEmployeeList.size()) + 1;
        mEmployeeList.add(position, employee);
        notifyItemInserted(position);
    }

    public void remove() {
        if (mEmployeeList.size() == 0) {
            return;
        }

        int position = new Random().nextInt(mEmployeeList.size());
        mEmployeeList.remove(position);
        notifyItemRemoved(position);
    }
}
