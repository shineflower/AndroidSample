package com.jackie.sample.data_binding;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.databinding.ActivityTwoWayBinding;

/**
 * Created by Administrator on 2016/10/29.
 */

public class TwoWayActivity extends AppCompatActivity {
    private ActivityTwoWayBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_two_way);
        FormModel formModel = new FormModel("jackie.cheng", "123456");
        //监听属性发生改变
        formModel.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(TwoWayActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });
        mBinding.setModel(formModel);
    }
}
