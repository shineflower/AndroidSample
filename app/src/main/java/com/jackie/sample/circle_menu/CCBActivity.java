package com.jackie.sample.circle_menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jackie.sample.R;
import com.jackie.sample.custom_view.CircleMenuLayout;

/**
 * Created by Jackie on 2017/5/15
 * 仿建行圆形菜单
 */
public class CCBActivity extends AppCompatActivity {
	private CircleMenuLayout mCircleMenuLayout;

	private int[] mItemIcons = new int[] { R.drawable.home_bank_1_normal,
			R.drawable.home_bank_2_normal, R.drawable.home_bank_3_normal,
			R.drawable.home_bank_4_normal, R.drawable.home_bank_5_normal,
			R.drawable.home_bank_6_normal };

	private String[] mItemTexts = new String[] { "安全中心 ", "特色服务", "投资理财",
			"转账汇款", "我的账户", "信用卡" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        //自已切换布局文件看效果
        setContentView(R.layout.activity_ccb);
//		setContentView(R.layout.activity_circle);

		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.circle_menu_layout);
		mCircleMenuLayout.setMenuItemLayoutId(R.layout.item_circle_menu);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemIcons, mItemTexts);

		mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
			@Override
			public void itemClick(View view, int position) {
				Toast.makeText(CCBActivity.this, mItemTexts[position],
						Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void itemCenterClick(View view) {
				Toast.makeText(CCBActivity.this,
						"you can do something just like ccb  ",
						Toast.LENGTH_SHORT).show();
				
			}
		});
	}
}
