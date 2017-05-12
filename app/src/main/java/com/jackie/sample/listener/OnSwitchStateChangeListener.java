package com.jackie.sample.listener;

/**
 * Created by Jackie on 2015/12/15.
 * 监听Switch的状态改变
 */
public interface OnSwitchStateChangeListener {
    /**
     * 当开关状态改变时回调此方法
     * @param state 开关当前的状态
     */
    void onSwitchStateChange(boolean state);
}
