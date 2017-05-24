package com.jackie.sample.clip_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jackie.sample.R;

/**
 * Created by Jackie on 2017/5/15
 * 显示图片
 */
public class ShowImageActivity extends AppCompatActivity {
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		mImageView = (ImageView) findViewById(R.id.iv_show_image);
		byte[] buffer = getIntent().getByteArrayExtra("bitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

		if (bitmap != null) {
			mImageView.setImageBitmap(bitmap);
		}
	}
}
