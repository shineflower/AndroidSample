package com.jackie.sample.utils;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class ApkTools {
	private Context mContext;
	private static ApkTools mInstance;

	private ApkTools(Context context) {
		mContext = context;
	}

	public static ApkTools getInstance(Context context) {
		if (mInstance == null) {
			synchronized (ApkTools.class) {
				if (mInstance == null) {
					mInstance = new ApkTools(context);
				}
			}
		}
		return mInstance;
	}

	public boolean isInstalledOnSDCard(String packageName) {
		try {
			PackageManager packageManager = mContext.getPackageManager();
			ApplicationInfo applicationInfo;
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);

			if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// 判断微信是否安装
	public static boolean isWeixinInstalled(Context context) {
		PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息

		if (packageInfoList != null) {
			for (int i = 0; i < packageInfoList.size(); i++) {
				String pn = packageInfoList.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 判断qq是否可用
	 * @param context
	 * @return
	 */
	public static boolean isQQInstalled(Context context) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);

		if (packageInfoList != null) {
			for (int i = 0; i < packageInfoList.size(); i++) {
				String pn = packageInfoList.get(i).packageName;
				if (pn.equals("com.tencent.mobileqq")) {
					return true;
				}
			}
		}

		return false;
	}
}
