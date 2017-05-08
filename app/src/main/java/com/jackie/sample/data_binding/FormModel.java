package com.jackie.sample.data_binding;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.jackie.sample.BR;

/**
 * Created by Administrator on 2016/10/29.
 */

public class FormModel extends BaseObservable {
    private String username;
    private String password;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public FormModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
