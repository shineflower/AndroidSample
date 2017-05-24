package com.jackie.sample.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {
	
	static ExecutorService mCachedThreadPool = null;
	
	public static void newThread(Runnable runnable) {
		
		if (mCachedThreadPool == null) {
			mCachedThreadPool = Executors.newCachedThreadPool();
		}

		mCachedThreadPool.execute(runnable);
	}
}
