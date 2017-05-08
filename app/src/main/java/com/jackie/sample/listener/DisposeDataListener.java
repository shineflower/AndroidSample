package com.jackie.sample.listener;

/**
 * Created by Administrator on 2016/10/30.
 */

public interface DisposeDataListener {
    void onSuccess(Object response);

    void onFailure(Object error);
}
