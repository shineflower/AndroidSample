package com.jackie.sample.wechat_camera.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.jackie.sample.R;
import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.wechat_camera.listener.CameraInterface;
import com.jackie.sample.wechat_camera.listener.CaptureListener;
import com.jackie.sample.wechat_camera.listener.ErrorListener;
import com.jackie.sample.wechat_camera.listener.JCameraListener;
import com.jackie.sample.wechat_camera.listener.ReturnListener;
import com.jackie.sample.wechat_camera.listener.TypeListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.File;
import java.io.IOException;

/**
 * Created by Jackie on 2017/6/8.
 * 仿微信拍照控件
 */

public class JCameraView extends RelativeLayout implements CameraInterface.CameraOpenCallback, SurfaceHolder.Callback {
    private static final int TYPE_PICTURE = 0x001;
    private static final int TYPE_VIDEO = 0x002;

    public static final int MEDIA_QUALITY_HIGH = 20 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 16 * 100000;
    public static final int MEDIA_QUALITY_LOW = 12 * 100000;
    public static final int MEDIA_QUALITY_POOR = 8 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 4 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 2 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 1 * 80000;
    public static final int MEDIA_QUALITY_SORRY_YOU_ARE_GOOD_MAN = 1 * 100000;

    //只能拍照
    public static final int BUTTON_STATE_ONLY_CAPTURE = 0x101;
    //只能录像
    public static final int BUTTON_STATE_ONLY_RECORDER = 0x102;
    //两者都可以
    public static final int BUTTON_STATE_BOTH = 0x103;

    private JCameraListener mJCameraListener;

    private Context mContext;
    private VideoView mVideoView;
    private ImageView mPhoto;
    private ImageView mSwitchCamera;
    private CaptureLayout mCaptureLayout;
    private FocusView mFocusView;
    private MediaPlayer mMediaPlayer;

    private int mWidth;
    private int mFocusSize;
    private float mScreenProps;

    private Bitmap mCaptureBitmap;
    private String mVideoUrl;
    private int mType = -1;

    private int CAMERA_STATE = -1;
    private static final int STATE_IDLE = 0x010;
    private static final int STATE_RUNNING = 0x020;
    private static final int STATE_WAIT = 0x030;

    private boolean mIsStopping = false;
    private boolean mIsBorrowing = false;
    private boolean mTakePicturing = false;
    private boolean mForbiddenSwitch = false;

    /**
     * switch button param
     */
    private int mIconSize = 0;
    private int mIconMargin = 0;
    private int mIconSrc = 0;
    private int mDuration = 0;

    public JCameraView(Context context) {
        this(context, null);
    }

    public JCameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.JCameraView, defStyleAttr, 0);
        mIconSize = ta.getDimensionPixelSize(R.styleable.JCameraView_icon_size, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 35, getResources().getDisplayMetrics()));
        mIconMargin = ta.getDimensionPixelSize(R.styleable.JCameraView_icon_margin, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
        mIconSrc = ta.getResourceId(R.styleable.JCameraView_camera_icon_src, R.drawable.ic_sync_black_24dp);
        mDuration = ta.getInteger(R.styleable.JCameraView_duration_max, 10 * 1000);
        ta.recycle();

        initView();
        initData();
    }

    private void initView() {
        setWillNotDraw(false);

        this.setBackgroundColor(0xff000000);

        //VideoView
        mVideoView = new VideoView(mContext);
        LayoutParams videoViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        videoViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mVideoView.setLayoutParams(videoViewParams);

        //Photo
        mPhoto = new ImageView(mContext);
        LayoutParams photoParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mPhoto.setLayoutParams(photoParams);
        mPhoto.setBackgroundColor(0xff000000);
        mPhoto.setVisibility(INVISIBLE);

        //SwitchCamera
        mSwitchCamera = new ImageView(mContext);
        LayoutParams switchParams = new LayoutParams(mIconSize, mIconSize);
        switchParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        switchParams.setMargins(0, mIconMargin, mIconMargin, 0);
        mSwitchCamera.setLayoutParams(switchParams);
        mSwitchCamera.setImageResource(mIconSrc);

        mSwitchCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBorrowing || mSwitching || mForbiddenSwitch) {
                    return;
                }
                mSwitching = true;
                new Thread() {
                    /**
                     * switch camera
                     */
                    @Override
                    public void run() {
                        CameraInterface.getInstance().switchCamera(JCameraView.this);
                    }
                }.start();
            }
        });

        //CaptureLayout
        mCaptureLayout = new CaptureLayout(mContext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.setMargins(0, 0, 0, 40);
        mCaptureLayout.setLayoutParams(layoutParams);
        mCaptureLayout.setDuration(mDuration);

        //FocusView
        mFocusView = new FocusView(mContext, mFocusSize);
        LayoutParams focusParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mFocusView.setLayoutParams(focusParams);
        mFocusView.setVisibility(INVISIBLE);

        //add view to parent layout
        this.addView(mVideoView);
        this.addView(mPhoto);
        this.addView(mSwitchCamera);
        this.addView(mCaptureLayout);
        this.addView(mFocusView);

        //captureLayout listener callback
        mCaptureLayout.setCaptureListener(new CaptureListener() {
            @Override
            public void takePicture() {
                if (CAMERA_STATE != STATE_IDLE || mTakePicturing) {
                    return;
                }

                CAMERA_STATE = STATE_RUNNING;
                mTakePicturing = true;
                mFocusView.setVisibility(INVISIBLE);

                CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() {
                    @Override
                    public void captureResult(Bitmap bitmap) {
                        mCaptureBitmap = bitmap;
                        CameraInterface.getInstance().doStopCamera();
                        mType = TYPE_PICTURE;
                        mIsBorrowing = true;
                        CAMERA_STATE = STATE_WAIT;
                        mPhoto.setImageBitmap(bitmap);
                        mPhoto.setVisibility(VISIBLE);
                        mCaptureLayout.startTipAlphaAnimation();
                        mCaptureLayout.startTypeTranslationAnimation();
                        mTakePicturing = false;
                        mSwitchCamera.setVisibility(INVISIBLE);
                        CameraInterface.getInstance().doOpenCamera(JCameraView.this);
                    }
                });
            }

            @Override
            public void recordShort(long time) {
                if (CAMERA_STATE != STATE_RUNNING && mIsStopping) {
                    return;
                }

                mIsStopping = true;
                mCaptureLayout.setTextWithAnimation("录制时间过短");

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CameraInterface.getInstance().stopRecord(true, new
                                CameraInterface.StopRecordCallback() {
                                    @Override
                                    public void recordResult(String url) {
                                        mCaptureLayout.setIsRecording(false);
                                        CAMERA_STATE = STATE_IDLE;
                                        mIsStopping = false;
                                        mIsBorrowing = false;
                                    }
                                });
                    }
                }, 1500 - time);
            }

            @Override
            public void recordStart() {
                if (CAMERA_STATE != STATE_IDLE && mIsStopping) {
                    return;
                }

                mCaptureLayout.setIsRecording(true);
                mIsBorrowing = true;
                CAMERA_STATE = STATE_RUNNING;
                mFocusView.setVisibility(INVISIBLE);

                CameraInterface.getInstance().startRecord(mVideoView.getHolder().getSurface(), new CameraInterface
                        .ErrorCallback() {
                    @Override
                    public void onError() {
                        mCaptureLayout.setIsRecording(false);
                        CAMERA_STATE = STATE_WAIT;
                        mIsStopping = false;
                        mIsBorrowing = false;
                    }
                });
            }

            @Override
            public void recordStop(long time) {
                CameraInterface.getInstance().stopRecord(false, new CameraInterface.StopRecordCallback() {
                    @Override
                    public void recordResult(final String url) {
                        CAMERA_STATE = STATE_WAIT;
                        mVideoUrl = url;
                        mType = TYPE_VIDEO;
                        new Thread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                try {
                                    if (mMediaPlayer == null) {
                                        mMediaPlayer = new MediaPlayer();
                                    } else {
                                        mMediaPlayer.reset();
                                    }
                                    mMediaPlayer.setDataSource(url);
                                    mMediaPlayer.setSurface(mVideoView.getHolder().getSurface());
                                    mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                    mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer
                                            .OnVideoSizeChangedListener() {
                                        @Override
                                        public void
                                        onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                                            updateVideoViewSize(mMediaPlayer.getVideoWidth(), mMediaPlayer
                                                    .getVideoHeight());
                                        }
                                    });
                                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mMediaPlayer.start();
                                        }
                                    });
                                    mMediaPlayer.setLooping(true);
                                    mMediaPlayer.prepare();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
            }

            @Override
            public void recordZoom(float zoom) {
                CameraInterface.getInstance().setZoom(zoom, CameraInterface.TYPE_RECORDER);
            }
        });

        mCaptureLayout.setTypeListener(new TypeListener() {
            @Override
            public void cancel() {
                if (CAMERA_STATE == STATE_WAIT) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }

                    handlePictureOrVideo(mType, false);
                }
            }

            @Override
            public void confirm() {
                if (CAMERA_STATE == STATE_WAIT) {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = null;
                    }

                    handlePictureOrVideo(mType, true);
                }
            }
        });

        mCaptureLayout.setReturnListener(new ReturnListener() {
            @Override
            public void onReturn() {
                if (mJCameraListener != null && !mTakePicturing) {
                    mJCameraListener.quit();
                }
            }
        });

        mVideoView.getHolder().addCallback(this);
    }

    private void initData() {
        mWidth = ScreenUtils.getScreenWidth(mContext);
        mFocusSize = mWidth / 4;
        CAMERA_STATE = STATE_IDLE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float widthSize = MeasureSpec.getSize(widthMeasureSpec);
        float heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mScreenProps = heightSize / widthSize;
    }

    @Override
    public void cameraHasOpened() {
        CameraInterface.getInstance().doStartPreview(mVideoView.getHolder(), mScreenProps);
    }

    private boolean mSwitching = false;

    @Override
    public void cameraSwitchSuccess() {
        mSwitching = false;
    }

    /**
     * start preview
     */
    public void onResume() {
        CameraInterface.getInstance().registerSensorManager(mContext);
        CameraInterface.getInstance().setSwitchView(mSwitchCamera);
    }

    /**
     * stop preview
     */
    public void onPause() {
        CameraInterface.getInstance().unregisterSensorManager(mContext);
        CameraInterface.getInstance().doStopCamera();
    }

    private boolean mFirstTouch = true;
    private float mFirstTouchLength = 0;

    /**
     * handler touch focus
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1) {
                    //显示对焦指示器
                    setFocusViewWidthAnimation(event.getX(), event.getY());
                }

                if (event.getPointerCount() == 2) {
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    mFirstTouch = true;
                }

                if (event.getPointerCount() == 2) {
                    //第一个点
                    float point_1_X = event.getX(0);
                    float point_1_Y = event.getY(0);
                    //第二个点
                    float point_2_X = event.getX(1);
                    float point_2_Y = event.getY(1);

                    float result = (float) Math.sqrt(Math.pow(point_1_X - point_2_X, 2) + Math.pow(point_1_Y -
                            point_2_Y, 2));

                    if (mFirstTouch) {
                        mFirstTouchLength = result;
                        mFirstTouch = false;
                    }

                    if ((int) (result - mFirstTouchLength) / 50 != 0) {
                        mFirstTouch = true;
                        CameraInterface.getInstance().setZoom(result - mFirstTouchLength, CameraInterface.TYPE_CAPTURE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mFirstTouch = true;
                break;
        }

        return true;
    }

    /**
     * focus view animation
     */
    private void setFocusViewWidthAnimation(float x, float y) {
        if (mIsBorrowing) {
            return;
        }

        if (y > mCaptureLayout.getTop()) {
            return;
        }

        mFocusView.setVisibility(VISIBLE);

        if (x < mFocusView.getWidth() / 2) {
            x = mFocusView.getWidth() / 2;
        }

        if (x > mWidth - mFocusView.getWidth() / 2) {
            x = mWidth - mFocusView.getWidth() / 2;
        }

        if (y < mFocusView.getWidth() / 2 + mIconMargin) {
            y = mFocusView.getWidth() / 2 + mIconMargin;
        }

        if (y > mCaptureLayout.getTop() - mFocusView.getWidth() / 2) {
            y = mCaptureLayout.getTop() - mFocusView.getWidth() / 2;
        }

        CameraInterface.getInstance().handleFocus(mContext, x, y, new CameraInterface.FocusCallback() {
            @Override
            public void focusSuccess() {
                mFocusView.setVisibility(INVISIBLE);
            }
        });

        mFocusView.setX(x - mFocusView.getWidth() / 2);
        mFocusView.setY(y - mFocusView.getHeight() / 2);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFocusView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFocusView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFocusView, "alpha", 1f, 0.3f, 1f, 0.3f, 1f, 0.3f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
    }

    public void setJCameraListener(JCameraListener jCameraListener) {
        this.mJCameraListener = jCameraListener;
    }

    private void handlePictureOrVideo(int type, boolean confirm) {
        if (mJCameraListener == null || type == -1) {
            return;
        }

        switch (type) {
            case TYPE_PICTURE:
                mPhoto.setVisibility(INVISIBLE);
                if (confirm && mCaptureBitmap != null) {
                    mJCameraListener.captureSuccess(mCaptureBitmap);
                } else {
                    if (mCaptureBitmap != null) {
                        mCaptureBitmap.recycle();
                    }
                    mCaptureBitmap = null;
                }
                break;
            case TYPE_VIDEO:
                if (confirm) {
                    //回调录像成功后的URL
                    mJCameraListener.recordSuccess(mVideoUrl);
                } else {
                    //删除视频
                    File file = new File(mVideoUrl);
                    if (file.exists()) {
                        file.delete();
                    }
                }

                mCaptureLayout.setIsRecording(false);
                LayoutParams videoViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                videoViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mVideoView.setLayoutParams(videoViewParams);
                CameraInterface.getInstance().doOpenCamera(JCameraView.this);
                break;
        }

        mIsBorrowing = false;
        mSwitchCamera.setVisibility(VISIBLE);
        CAMERA_STATE = STATE_IDLE;
    }

    public void setSaveVideoPath(String path) {
        CameraInterface.getInstance().setSaveVideoPath(path);
    }

    /**
     * TextureView resize
     */
    public void updateVideoViewSize(float videoWidth, float videoHeight) {
        if (videoWidth > videoHeight) {
            LayoutParams videoViewParam;
            int height = (int) ((videoHeight / videoWidth) * getWidth());
            videoViewParam = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            videoViewParam.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            mVideoView.setLayoutParams(videoViewParam);
        }
    }

    /**
     * forbidden audio
     */
    public void enableShutterSound(boolean enable) {
    }

    public void forbiddenSwitchCamera(boolean forbiddenSwitch) {
        this.mForbiddenSwitch = forbiddenSwitch;
    }

    //启动Camera错误回调
    public void setErrorListener(ErrorListener errorListener) {
        CameraInterface.getInstance().setErrorListener(errorListener);
    }

    //设置CaptureButton功能(拍照和录像)
    public void setButtonState(int state) {
        this.mCaptureLayout.setButtonState(state);
    }

    //设置录制质量
    public void setMediaQuality(int quality) {
        CameraInterface.getInstance().setMediaQuality(quality);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new Thread() {
            @Override
            public void run() {
                CameraInterface.getInstance().doOpenCamera(JCameraView.this);
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraInterface.getInstance().doDestroyCamera();
    }
}
