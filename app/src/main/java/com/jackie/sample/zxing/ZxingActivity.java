package com.jackie.sample.zxing;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class ZxingActivity extends AppCompatActivity implements OnClickListener {
	private Button mScanButton;

	private CheckBox mCreateCheckBox;
	private EditText mCreateEditText;
	private Button mCreateButton;

	private Button mChooseButton;

	//显示扫描结果
	private TextView mTextView;
	//显示扫描拍的图片
	private ImageView mImageView;

	private String mCurrentPath;

	private final static int SCANNING_REQUEST_CODE = 1;
	private final static int CHOOSE_REQUEST_CODE = 2;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zxing);

		mTextView = (TextView) findViewById(R.id.scan_result);
		mImageView = (ImageView) findViewById(R.id.scan_image);

		///点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		mScanButton = (Button) findViewById(R.id.scan_qr_code);
		mScanButton.setOnClickListener(this);

		mCreateCheckBox = (CheckBox) findViewById(R.id.create_logo);
		mCreateEditText = (EditText) findViewById(R.id.create_content);
		mCreateButton = (Button) findViewById(R.id.create_qr_code);
		mCreateButton.setOnClickListener(this);

		mChooseButton = (Button) findViewById(R.id.choose_qr_code);
		mChooseButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		resetStatus();

		Intent intent;
		switch (v.getId()) {
			case R.id.scan_qr_code:
				intent = new Intent();
				intent.setClass(ZxingActivity.this, CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNING_REQUEST_CODE);
				break;
			case R.id.create_qr_code:
				String createContent = mCreateEditText.getText().toString().trim();

				/**
				 * /storage/emulated/0/Android/data/com.jackie.sample/files/qr_1495419734955.jpg
				 */
				final String filePath = StorageUtils.getFileRoot(ZxingActivity.this) + File.separator
						+ "qr_" + System.currentTimeMillis() + ".jpg";

				boolean success = QRCodeUtils.createQRImage(createContent, 800, 800,
						mCreateCheckBox.isChecked() ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher) : null,
						filePath);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mTextView.setText(mCreateEditText.getText().toString().toString());
							mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
						}
					});
				}
				break;
			case R.id.choose_qr_code:
				intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, CHOOSE_REQUEST_CODE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SCANNING_REQUEST_CODE:
					Bundle bundle = data.getExtras();
					//显示扫描到的内容
					mTextView.setText(bundle.getString("result"));
					String filePath = data.getStringExtra("bitmap");
					//显示
					mImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
					break;
				case CHOOSE_REQUEST_CODE:
					Uri uri = data.getData();

					if (!TextUtils.isEmpty(uri.getAuthority())) {
						Cursor cursor = getContentResolver().query(uri, null, null, null, null);
						if (cursor != null) {
							cursor.moveToFirst();
							int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
							mCurrentPath = cursor.getString(index);
						}
					} else {
						mCurrentPath = uri.getPath();
					}

					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(mCurrentPath, options);

					double ratio = Math.max(options.outWidth * 1.0d / 1024, options.outHeight * 1.0d / 1024);
					options.inSampleSize = (int) Math.ceil(ratio);
					options.inJustDecodeBounds = false;

					Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPath, options);
					String qrCodeString = QRCodeUtils.getStringFromQRCode(bitmap);

					mTextView.setText(TextUtils.isEmpty(qrCodeString) ? "未检测到二维码" : qrCodeString);
					break;
			}
		}
	}

	private void resetStatus() {
		mTextView.setText("");
		mImageView.setImageBitmap(null);
	}
}
