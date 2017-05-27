package com.jackie.sample.mm_alert;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

import com.jackie.sample.R;
import com.jackie.sample.utils.MMAlert;

public class SendToWXActivity extends AppCompatActivity {

	private static final int THUMB_SIZE = 150;

	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	private static final int MMAlertSelect1  =  0;
	private static final int MMAlertSelect2  =  1;
	private static final int MMAlertSelect3  =  2;

	private CheckBox isTimelineCb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_to_wx);
		initView();
	}

	private void initView() {
		isTimelineCb = (CheckBox) findViewById(R.id.is_timeline_cb);
		isTimelineCb.setChecked(false);
		
		// send to weixin
		findViewById(R.id.send_img).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MMAlert.showAlert(SendToWXActivity.this, getString(R.string.send_img),
						SendToWXActivity.this.getResources().getStringArray(R.array.send_img_item),
						null, new MMAlert.OnAlertSelectedListener(){

							@Override
							public void onSelected(int whichButton) {
								switch(whichButton) {
									case MMAlertSelect1:

										break;
									case MMAlertSelect2:

										break;
									case MMAlertSelect3:

										break;
									default:
										break;
								}
							}
					
				});
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case 0x101: {

			break;
		}
		default:
			break;
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
}
