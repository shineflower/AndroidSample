package com.jackie.sample.listener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {
	private Context mContext;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	
	private float mLastX ;
	
	private OnOrientationListener mOnOrientationListener ;

	public MyOrientationListener(Context context)
	{
		this.mContext = context;
	}

	// 开始
	public void start() {
		// 获得传感器管理器
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager != null) {
			// 获得方向传感器
			mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		}

		// 注册
		if (mSensor != null)
		{//SensorManager.SENSOR_DELAY_UI
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}

	// 停止检测
	public void stop() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// 接受方向感应器的类型  
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            // 这里我们可以得到数据，然后根据需要来处理  
            float x = event.values[SensorManager.DATA_X];  
            
            if(Math.abs(x- mLastX) > 1.0) {
            	mOnOrientationListener.onOrientationChanged(x);
            }
//            Log.e("DATA_X", x+"");
            mLastX = x ;
        }  
	}
	
	public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
		this.mOnOrientationListener = onOrientationListener ;
	}
	
	
	public interface OnOrientationListener {
		void onOrientationChanged(float x);
	}

}
