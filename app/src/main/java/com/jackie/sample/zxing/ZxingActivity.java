package com.jackie.sample.zxing;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackie.sample.R;
import com.jackie.sample.utils.StorageUtils;
import com.jackie.sample.zxing.utils.QRCodeUtils;

import java.io.File;

public class ZxingActivity extends AppCompatActivity {
	private final static int SCANNING_REQUEST_CODE = 1;
	//显示扫描结果
	private TextView mTextView ;
	//显示扫描拍的图片
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zxing);
		
		mTextView = (TextView) findViewById(R.id.scan_result);
		mImageView = (ImageView) findViewById(R.id.scan_image);
		
		///点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		Button scanButton = (Button) findViewById(R.id.scan_qr_code);
		scanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ZxingActivity.this, CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNING_REQUEST_CODE);
			}
		});

		final CheckBox createCheckBox = (CheckBox) findViewById(R.id.create_logo);
		final EditText createEditText = (EditText) findViewById(R.id.create_content);
		Button createButton = (Button) findViewById(R.id.create_qr_code);
		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String createContent = createEditText.getText().toString().trim();

				/**
				 * /storage/emulated/0/Android/data/com.jackie.sample/files/qr_1495419734955.jpg
				 */
				final String filePath =  StorageUtils.getFileRoot(ZxingActivity.this) + File.separator
						+ "qr_" + System.currentTimeMillis() + ".jpg";

				boolean success = QRCodeUtils.createQRImage(createContent, 800, 800,
						createCheckBox.isChecked() ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : null,
						filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mTextView.setText(createEditText.getText().toString().toString());
							mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
						}
					});
				}
			}
		});
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNING_REQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				mTextView.setText(bundle.getString("result"));
				String filePath = data.getStringExtra("bitmap");
				//显示
				mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
			}
			break;
		}
    }
}
