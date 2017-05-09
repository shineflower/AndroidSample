package com.jackie.sample.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {
	private static boolean debug = true;

	private static final String TAG = "jackie";

	public static void setDebug(boolean isDebug) {
		debug = isDebug;
	}

	public static void showLog(String text) {
		if (debug) {
			if(!TextUtils.isEmpty(text) && text.length() > 3000) {
				for(int i = 0; i < text.length(); i += 3000){
					if(i + 3000 < text.length()) {
						Log.d(TAG, text.substring(i, i + 3000));
					} else {
						Log.d(TAG, text.substring(i, text.length()));
					}
				}
			} else {
				Log.d(TAG, text);
			}
		}
	}

	public static void showErrLog(String text) {
		if (debug) {
			Log.e(TAG, text);
		}
	}

	public static void showInfoLog(String text){
		if (debug) {
			if(!TextUtils.isEmpty(text)&& text.length() > 3000) {
				for(int i = 0; i < text.length(); i += 3000){
					if(i + 3000 < text.length()) {
						Log.i(TAG, text.substring(i, i + 3000));
					} else {
						Log.i(TAG, text.substring(i, text.length()));
					}
				}
			} else {
				Log.i(TAG, text);
			}
		}
	}
}
