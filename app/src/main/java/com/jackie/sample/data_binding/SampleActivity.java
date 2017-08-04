package com.jackie.sample.data_binding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.BR;
import com.jackie.sample.R;
import com.jackie.sample.databinding.ActivitySampleBinding;

public class SampleActivity extends AppCompatActivity {
    ActivitySampleBinding mBinding;
    Employee mEmployee = new Employee("Cheng", "Jackie", true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_data_binding);

        //根据资源文件的名字来生成 activity_data_binding -> ActivityMainBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sample);
//        mBinding.firstName.setText(mEmployee.getFirstName());
//        mBinding.lastName.setText(mEmployee.getLastName());

        //绑定
        //1. 方法1
        mBinding.setEmployee(mEmployee);
        //2. 方法2
        mBinding.setVariable(BR.employee, mEmployee);
        mBinding.setPresenter(new Presenter());
    }

    public class Presenter {
        //绑定方法1： 方法引用，方法名和参数必须跟系统提供的保持一致
        public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
            mEmployee.setFirstName(text.toString());
            mBinding.setEmployee(mEmployee);
        }

        public void onClick(View view) {
            Toast.makeText(SampleActivity.this, "点到了", Toast.LENGTH_LONG).show();
        }

        //绑定方法2：监听器绑定
        public void onClickListenerBinding(Employee employee) {
            Toast.makeText(SampleActivity.this, employee.getLastName(), Toast.LENGTH_LONG).show();
        }
    }
}
