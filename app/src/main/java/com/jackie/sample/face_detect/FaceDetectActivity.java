package com.jackie.sample.face_detect;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facepp.error.FaceppParseException;
import com.jackie.sample.R;
import com.jackie.sample.utils.FaceDetect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaceDetectActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mPhotoImage;
    private Button mPickImage, mDetectImage;
    private TextView mFaceCount;
    private FrameLayout mLoadingLayout;

    private String mCurrentPath;
    private Bitmap mBitmap;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mLoadingLayout.setVisibility(View.GONE);

            switch (msg.what) {
                case RESULT_OK:
                    JSONObject result = (JSONObject) msg.obj;
                    parseResultBitmap(result);

                    mPhotoImage.setImageBitmap(mBitmap);
                    break;
                case RESULT_CANCELED:
                    String errorMessage = (String) msg.obj;

                    if (TextUtils.isEmpty(errorMessage)) {
                        mFaceCount.setText("Error");
                    } else {
                        mFaceCount.setText(errorMessage);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        initView();
        initEvent();
    }

    private void initView() {
        mPhotoImage = (ImageView) findViewById(R.id.photo_image);
        mPickImage = (Button) findViewById(R.id.pick_image);
        mDetectImage = (Button) findViewById(R.id.detect_image);
        mFaceCount = (TextView) findViewById(R.id.face_count);

        mLoadingLayout = (FrameLayout) findViewById(R.id.loading);
    }

    private void initEvent() {
        mPickImage.setOnClickListener(this);
        mDetectImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_image:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, RESULT_FIRST_USER);
                break;
            case R.id.detect_image:
                mLoadingLayout.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(mCurrentPath)) {
                    resizeImage();
                } else {
                    mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_image);
                }

                FaceDetect.detect(mBitmap, new FaceDetect.FaceDetectCallback() {
                    @Override
                    public void success(JSONObject result) {
                        Message msg = Message.obtain();
                        msg.what = RESULT_OK;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void error(FaceppParseException e) {
                        Message msg = Message.obtain();
                        msg.what = RESULT_CANCELED;
                        msg.obj = e.getErrorMessage();
                        mHandler.sendMessage(msg);
                    }
                });
                break;
        }
    }

    private void resizeImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPath, options);

        double ratio = Math.max(options.outWidth * 1.0d / 1024, options.outHeight * 1.0d / 1024);
        options.inSampleSize = (int) Math.ceil(ratio);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(mCurrentPath, options);
    }

    private void parseResultBitmap(JSONObject jsonObject) {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(3);
        paint.setColor(0xFFFFFFFF);

        try {
            JSONArray faceArray = jsonObject.getJSONArray("face");
            int count = faceArray.length();
            mFaceCount.setText("Find: " + count);

            for (int i = 0; i < count; i++) {
                JSONObject faceObject = faceArray.getJSONObject(i);
                JSONObject positionObject = faceObject.getJSONObject("position");
                float x = (float) positionObject.getJSONObject("center").getDouble("x");
                float y = (float) positionObject.getJSONObject("center").getDouble("y");

                float width = (float) positionObject.getDouble("width");
                float height = (float) positionObject.getDouble("height");

                x = x / 100 * mBitmap.getWidth();
                y = y / 100 * mBitmap.getHeight();

                width = width / 100 * mBitmap.getWidth();
                height = height / 100 * mBitmap.getHeight();

                //face rect
                canvas.drawLine(x - width / 2, y - height / 2, x + width / 2, y - height / 2, paint);
                canvas.drawLine(x - width / 2, y + height / 2, x + width / 2, y + height / 2, paint);
                canvas.drawLine(x - width / 2, y - height / 2, x - width / 2, y + height / 2, paint);
                canvas.drawLine(x + width / 2, y - height / 2, x + width / 2, y + height / 2, paint);

                //get age and gender
                int age = faceObject.getJSONObject("attribute").getJSONObject("age").getInt("value");
                String gender = faceObject.getJSONObject("attribute").getJSONObject("gender").getString("value");
                Bitmap ageGenderBitmap = buildAgeGenderBitmap(age, "Male".equals(gender) ? true : false);

                int ageGenderWidth = ageGenderBitmap.getWidth();
                int ageGenderHeight = ageGenderBitmap.getHeight();

                if (bitmap.getWidth() < mBitmap.getWidth() && bitmap.getHeight() < mBitmap.getHeight()) {
                    float ratio = Math.max(bitmap.getWidth() * 1.0f / mBitmap.getWidth(), bitmap.getHeight() * 1.0f / mBitmap.getHeight());
                    ageGenderBitmap = Bitmap.createScaledBitmap(ageGenderBitmap, (int)(ageGenderWidth * ratio), (int)(ageGenderHeight * ratio), false);
                }

                canvas.drawBitmap(ageGenderBitmap, x - ageGenderBitmap.getWidth() / 2, y - width / 2 - ageGenderBitmap.getHeight(), null);

                mBitmap = bitmap;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap buildAgeGenderBitmap(int age, boolean isMale) {
        TextView ageGender = (TextView) mLoadingLayout.findViewById(R.id.detect_result);
        ageGender.setText(age + "");

        if (isMale) {
            ageGender.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.male), null, null, null);
        } else {
            ageGender.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.female), null, null, null);
        }

        ageGender.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(ageGender.getDrawingCache());
        ageGender.destroyDrawingCache();

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == RESULT_FIRST_USER) {
            Uri uri = data.getData();
            /**
             * 在红米手机上，Uri uri = data.getData();
             * uri返回的值是 file:///storage/emulated/0/DCIM/Camera/IMG_20170512_132530_HDR.jpg
             * 正常的uri的格式是content://，所以需要进行单独处理
             */
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

            //压缩图片
            resizeImage();
            mPhotoImage.setImageBitmap(mBitmap);

            mFaceCount.setText("Click Detect ==>");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}