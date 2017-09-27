package com.jackie.sample.wechat_tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackie.sample.R;

public class ChatFragment extends Fragment {
//	private TaskFragment mTaskFragment;
//	private FunctionFragment mFunctionFragment;
//	private Fragment mCurrentFragment;
//
//	private String TAG_TASK = "tab_task";
//	private String TAG_FUNCTION = "tab_function";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_chat, container, false);
	}

//	private void switchFragment(String tag) {
//		FragmentManager fragmentManager = getFragmentManager();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//		Fragment toFragment = fragmentManager.findFragmentByTag(tag);
//
//		if (toFragment == null) {
//			if (TAG_TASK.equals(tag)) {
//				mTaskFragment = new TaskFragment();
//				toFragment = mTaskFragment;
//			} else if (TAG_FUNCTION.equals(tag)) {
//				mFunctionFragment = new FunctionFragment();
//				toFragment = mFunctionFragment;
//			}
//		}
//
//		if (mCurrentFragment != null) {
//			if (mCurrentFragment != toFragment) {
//				if (!toFragment.isAdded()) {
//					fragmentTransaction.add(R.id.container, toFragment, tag).hide(mCurrentFragment);
//				} else {
//					fragmentTransaction.show(toFragment).hide(mCurrentFragment);
//				}
//			}
//		} else {
//			if (!toFragment.isAdded()) {
//				fragmentTransaction.add(R.id.container, toFragment, tag);
//			} else {
//				fragmentTransaction.show(toFragment);
//			}
//		}
//
//		mCurrentFragment = toFragment;
//		fragmentTransaction.commitAllowingStateLoss();
//	}
}