package com.jackie.sample.listener;

/**
 * Created by Administrator on 2016/10/30.
 */

public class DisposeDataHandler {
    public DisposeDataListener mListener = null;
    public Class<?> mClazz = null;

    public DisposeDataHandler(DisposeDataListener listener) {
        mListener = listener;
    }

    public DisposeDataHandler(DisposeDataListener listener, Class<?> clazz) {
        mListener = listener;
        mClazz = clazz;
    }
}
