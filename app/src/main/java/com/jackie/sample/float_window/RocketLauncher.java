package com.jackie.sample.float_window;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jackie.sample.R;

public class RocketLauncher extends LinearLayout {

	//记录火箭发射台的宽度
	public int mLauncherWidth;

	//记录火箭发射台的高度
	public int mLauncherHeight;

	//火箭发射台的背景图片
	private ImageView mLauncherImage;

	public RocketLauncher(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.rocket_launcher, this);

		mLauncherImage = (ImageView) findViewById(R.id.launcher_image);
		mLauncherWidth = mLauncherImage.getLayoutParams().width;
		mLauncherHeight = mLauncherImage.getLayoutParams().height;
	}

	/**
	 * 更新火箭发射台的显示状态。如果小火箭被拖到火箭发射台上，就显示发射。
	 */
	public void updateLauncherStatus(boolean isReadyToLaunch) {
		if (isReadyToLaunch) {
			mLauncherImage.setImageResource(R.drawable.bg_launcher_fire);
		} else {
			mLauncherImage.setImageResource(R.drawable.bg_launcher_holder);
		}
	}

	public int getLauncherWidth() {
		return mLauncherWidth;
	}

	public int getLauncherHeight() {
		return mLauncherHeight;
	}
}
