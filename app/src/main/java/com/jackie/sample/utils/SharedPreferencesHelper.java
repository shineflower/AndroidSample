package com.jackie.sample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesHelper {
	private Context mContext;

	private SharedPreferences mSharedPreferences;
	private Editor mEditor;

	public SharedPreferencesHelper(Context context) {
		this.mContext = context;

		mSharedPreferences = context.getSharedPreferences("AndroidSample", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
	}

	public void putValue(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}

	public String getValue(String key) {
		// 第二个参数是默认值， 如果取的时候发现没数据，那就会自动设置为空
		return mSharedPreferences.getString(key, "");
	}

	public String getValue(String key, String defaultValue) {
		return mSharedPreferences.getString(key, defaultValue);
	}

	public void putIntValue(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	public int getIntValue(String key) {
		// 第二个参数是默认值， 如果取的时候发现没数据，那就会自动设置为空
		return mSharedPreferences.getInt(key, -1);
	}

	public void putBooleanValue(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	public boolean getBooleanValue(String key) {
		// 第二个参数是默认值， 如果取的时候发现没数据，那就会自动设置为空
		return mSharedPreferences.getBoolean(key, false);
	}

	public boolean getBooleanValue(String key, boolean defaultValue) {
		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public void deleteValue(Context context, String key) {
		mSharedPreferences = context.getSharedPreferences("AndroidSample", Context.MODE_PRIVATE);
		mEditor = mSharedPreferences.edit();
		mEditor.remove(key);
		mEditor.commit();
	}
}
