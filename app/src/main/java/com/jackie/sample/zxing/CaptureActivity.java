package com.jackie.sample.zxing;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.jackie.sample.R;
import com.jackie.sample.utils.StorageUtils;
import com.jackie.sample.zxing.camera.CameraManager;
import com.jackie.sample.zxing.decoding.CaptureActivityHandler;
import com.jackie.sample.zxing.decoding.InactivityTimer;
import com.jackie.sample.zxing.view.ViewfinderView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private boolean isLightOpen;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		
		Button buttonBack = (Button) findViewById(R.id.button_back);
		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		ImageView lightOn = (ImageView) findViewById(R.id.light_on);
		ImageView selectImg = (ImageView) findViewById(R.id.select_img);

		lightOn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isLightOpen = !isLightOpen;

				if(isLightOpen){
					CameraManager.get().openLight();
				}else{
					CameraManager.get().closeLight();
				}
			}
		});

		selectImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * handle scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		} else {
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
			/**
			 * 如果传递的图片太大
			 * android.os.TransactionTooLargeException: data parcel size 728504 bytes
			 * 因此，不推荐在activity之间传递图片，可以将图片保存，然后传递图片的路径
			 * http://blog.csdn.net/u010886101/article/details/54171845
			 */

//			bundle.putParcelable("bitmap", barcode);

			String filePath = StorageUtils.getFileRoot(this) + File.separator
					+ "img_" + System.currentTimeMillis() + ".jpg";
			try {
				barcode.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
				bundle.putString("bitmap", filePath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}

		CaptureActivity.this.finish();


		/*
		 * 实现多次扫描
		 * 把CaptureActivityHandler这个类里面的restartPreviewAndDecode这个方法的private改成public，发现是可以用的
		 * 但速度太快，因此用定时器每隔3s去扫描一次
		 */
//		if (handler != null) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        handler.restartPreviewAndDecode();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 3000);
//        }
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
}