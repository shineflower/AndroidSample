package com.jackie.sample.data_binding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.databinding.ActivityListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/29.
 */

public class ListActivity extends AppCompatActivity {
    private ActivityListBinding mBinding;
    private EmployeeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);

        mBinding.setPresenter(new Presenter());

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EmployeeAdapter(this);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new EmployeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Employee employee) {
                Toast.makeText(ListActivity.this, employee.getFirstName(), Toast.LENGTH_SHORT).show();
            }
        });

        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("Cheng1", "Jackie1", false));
        employeeList.add(new Employee("Cheng2", "Jackie2", false));
        employeeList.add(new Employee("Cheng3", "Jackie3", true));
        employeeList.add(new Employee("Cheng4", "Jackie4", false));
        mAdapter.addAll(employeeList);
    }

    public class Presenter {
        public void onClickAddItem(View view) {
            mAdapter.add(new Employee("Huang", "Ashia", false));
        }

        public void onClickRemoveItem(View view) {
            mAdapter.remove();
        }
    }
}
