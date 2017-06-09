package com.jackie.sample.wechat_camera.listener;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import com.jackie.sample.utils.ScreenUtils;
import com.jackie.sample.wechat_camera.util.AngleUtil;
import com.jackie.sample.wechat_camera.util.CameraParamUtil;
import com.jackie.sample.wechat_camera.view.JCameraView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.rotation;

/**
 * Created by Jackie on 2017/6/8.
 * Camera操作单例
 */

public class CameraInterface {
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean mIsPreviewing = false;

    boolean isPreviewing() {
        return mIsPreviewing;
    }

    private static CameraInterface mCameraInterface;

    private int SELECTED_CAMERA = -1;
    private int CAMERA_POST_POSITION = -1;
    private int CAMERA_FRONT_POSITION = -1;

    private SurfaceHolder mHolder = null;
    private float mScreenProps = -1.0f;

    private boolean mIsRecorder = false;
    private MediaRecorder mMediaRecorder;
    private String mVideoFileName;
    private String mSaveVideoPath;
    private String mVideoFileAbsolutePath;

    private ErrorListener mErrorListener;

    private ImageView mSwitchView;

    public void setSwitchView(ImageView switchView) {
        this.mSwitchView = switchView;
    }

    private int mPreviewWidth;
    private int mPreviewHeight;

    private int mAngle = 0;
    private int mRotation = 0;
    private boolean mError = false;

    //视频质量
    private int mediaQuality = JCameraView.MEDIA_QUALITY_MIDDLE;

    private SensorManager mSensorManager = null;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
                return;
            }

            float[] values = event.values;
            mAngle = AngleUtil.getSensorAngle(values[0], values[1]);
            rotationAnimation();
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    //切换摄像头icon跟随手机角度进行旋转
    private void rotationAnimation() {
        if (mSwitchView == null) {
            return;
        }

        if (rotation != mAngle) {
            int startRotation = 0;
            int endRotation = 0;
            switch (rotation) {
                case 0:
                    startRotation = 0;
                    switch (mAngle) {
                        case 90:
                            endRotation = -90;
                            break;
                        case 270:
                            endRotation = 90;
                            break;
                    }
                    break;
                case 90:
                    startRotation = -90;
                    switch (mAngle) {
                        case 0:
                            endRotation = 0;
                            break;
                        case 180:
                            endRotation = -180;
                            break;
                    }
                    break;
                case 180:
                    startRotation = 180;
                    switch (mAngle) {
                        case 90:
                            endRotation = 270;
                            break;
                        case 270:
                            endRotation = 90;
                            break;
                    }
                    break;
                case 270:
                    startRotation = 90;
                    switch (mAngle) {
                        case 0:
                            endRotation = 0;
                            break;
                        case 180:
                            endRotation = 180;
                            break;
                    }
                    break;
            }
            ObjectAnimator anim = ObjectAnimator.ofFloat(mSwitchView, "rotation", startRotation, endRotation);
            anim.setDuration(500);
            anim.start();
            mRotation = mAngle;
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setSaveVideoPath(String saveVideoPath) {
        this.mSaveVideoPath = saveVideoPath;
        File file = new File(saveVideoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static final int TYPE_RECORDER = 0x090;
    public static final int TYPE_CAPTURE = 0x091;
    private int mNowScaleRate = 0;
    private int mRecordScaleRate = 0;

    public void setZoom(float zoom, int type) {
        if (mCamera == null) {
            return;
        }
        if (mParams == null) {
            mParams = mCamera.getParameters();
        }
        if (!mParams.isZoomSupported() || !mParams.isSmoothZoomSupported()) {
            return;
        }
        switch (type) {
            case TYPE_RECORDER:
                //如果不是录制视频中，上滑不会缩放
                if (!mIsRecorder) {
                    return;
                }
                if (zoom >= 0) {
                    //每移动50个像素缩放一个级别
                    int scaleRate = (int) (zoom / 50);
                    if (scaleRate <= mParams.getMaxZoom() && scaleRate >= mNowScaleRate && mRecordScaleRate != scaleRate) {
                        mParams.setZoom(scaleRate);
                        mCamera.setParameters(mParams);
                        mRecordScaleRate = scaleRate;
                    }
                }
                break;
            case TYPE_CAPTURE:
                if (mIsRecorder) {
                    return;
                }
                //每移动50个像素缩放一个级别
                int scaleRate = (int) (zoom / 50);
                if (scaleRate < mParams.getMaxZoom()) {
                    mNowScaleRate += scaleRate;
                    if (mNowScaleRate < 0) {
                        mNowScaleRate = 0;
                    } else if (mNowScaleRate > mParams.getMaxZoom()) {
                        mNowScaleRate = mParams.getMaxZoom();
                    }
                    mParams.setZoom(mNowScaleRate);
                    mCamera.setParameters(mParams);
                }
                break;
        }

    }

    public void setMediaQuality(int quality) {
        this.mediaQuality = quality;
    }

    public interface CameraOpenCallback {
        void cameraHasOpened();

        void cameraSwitchSuccess();
    }

    private CameraInterface() {
        findAvailableCameras();
        SELECTED_CAMERA = CAMERA_POST_POSITION;
        mSaveVideoPath = "";
    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    /**
     * open Camera
     */
    public void doOpenCamera(CameraOpenCallback cameraOpenCallback) {

        if (mCamera == null) {
            openCamera(SELECTED_CAMERA);
        }

        cameraOpenCallback.cameraHasOpened();
    }

    private void openCamera(int id) {
        try {
            this.mCamera = Camera.open(id);
        } catch (Exception e) {
            if (this.mErrorListener != null) {
                this.mErrorListener.onError();
            }
        }

        if (Build.VERSION.SDK_INT > 17 && this.mCamera != null) {
            this.mCamera.enableShutterSound(false);
        }
    }

    public synchronized void switchCamera(CameraOpenCallback callback) {
        if (SELECTED_CAMERA == CAMERA_POST_POSITION) {
            SELECTED_CAMERA = CAMERA_FRONT_POSITION;
        } else {
            SELECTED_CAMERA = CAMERA_POST_POSITION;
        }

        doStopCamera();
        mCamera = Camera.open(SELECTED_CAMERA);
        doStartPreview(mHolder, mScreenProps);
        callback.cameraSwitchSuccess();
    }

    /**
     * doStartPreview
     */
    public void doStartPreview(SurfaceHolder holder, float screenProps) {
        if (this.mScreenProps < 0) {
            this.mScreenProps = screenProps;
        }

        if (holder == null) {
            return;
        }

        this.mHolder = holder;
        if (mCamera != null) {
            try {
                mParams = mCamera.getParameters();
                Camera.Size previewSize = CameraParamUtil.getInstance().getPreviewSize(mParams
                        .getSupportedPreviewSizes(), 1000, screenProps);
                Camera.Size pictureSize = CameraParamUtil.getInstance().getPictureSize(mParams
                        .getSupportedPictureSizes(), 1200, screenProps);

                mParams.setPreviewSize(previewSize.width, previewSize.height);

                mPreviewWidth = previewSize.width;
                mPreviewHeight = previewSize.height;

                mParams.setPictureSize(pictureSize.width, pictureSize.height);

                if (CameraParamUtil.getInstance().isSupportedFocusMode(
                        mParams.getSupportedFocusModes(),
                        Camera.Parameters.FOCUS_MODE_AUTO)) {
                    mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                if (CameraParamUtil.getInstance().isSupportedPictureFormats(mParams.getSupportedPictureFormats(),
                        ImageFormat.JPEG)) {
                    mParams.setPictureFormat(ImageFormat.JPEG);
                    mParams.setJpegQuality(100);
                }

                mCamera.setParameters(mParams);
                mParams = mCamera.getParameters();
                //SurfaceView
                mCamera.setPreviewDisplay(holder);
                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
                mIsPreviewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                mCamera.stopPreview();
            }
        }
    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            try {
                mCamera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                mCamera.setPreviewDisplay(null);
                mIsPreviewing = false;
                mCamera.release();
                mCamera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doDestroyCamera() {
        if (null != mCamera) {
            try {
                mSwitchView = null;
                mCamera.stopPreview();
                //这句要在stopPreview后执行，不然会卡顿或者花屏
                mCamera.setPreviewDisplay(null);
                mHolder = null;
                mIsPreviewing = false;
                mCamera.release();
                mCamera = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 拍照
     */
    public void takePicture(final TakePictureCallback callback) {
        final int nowAngle = (mAngle + 90) % 360;
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                if (SELECTED_CAMERA == CAMERA_POST_POSITION) {
                    matrix.setRotate(nowAngle);
                } else if (SELECTED_CAMERA == CAMERA_FRONT_POSITION) {
                    matrix.setRotate(270);
                    matrix.postScale(-1, 1);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                if (callback != null) {
                    callback.captureResult(bitmap);
                }
            }
        });
    }

    public void startRecord(Surface surface, ErrorCallback callback) {
        if (mIsRecorder) {
            return;
        }

        int nowAngle = (mAngle + 90) % 360;

        if (mCamera == null) {
            openCamera(SELECTED_CAMERA);
        }

        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }

        if (mParams == null) {
            mParams = mCamera.getParameters();
        }

        List<String> focusModes = mParams.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        mCamera.setParameters(mParams);
        mCamera.unlock();
        mMediaRecorder.reset();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Camera.Size videoSize;
        if (mParams.getSupportedVideoSizes() == null) {
            videoSize = CameraParamUtil.getInstance().getPictureSize(mParams.getSupportedPreviewSizes(), 1000, mScreenProps);
        } else {
            videoSize = CameraParamUtil.getInstance().getPictureSize(mParams.getSupportedVideoSizes(), 1000, mScreenProps);
        }

        if (videoSize.width == videoSize.height) {
            mMediaRecorder.setVideoSize(mPreviewWidth, mPreviewHeight);
        } else {
            mMediaRecorder.setVideoSize(videoSize.width, videoSize.height);
        }

        if (SELECTED_CAMERA == CAMERA_FRONT_POSITION) {
            mMediaRecorder.setOrientationHint(270);
        } else {
            mMediaRecorder.setOrientationHint(nowAngle);
//            mMediaRecorder.setOrientationHint(90);
        }

        mMediaRecorder.setVideoEncodingBitRate(mediaQuality);
        mMediaRecorder.setPreviewDisplay(surface);

        mVideoFileName = "video_" + System.currentTimeMillis() + ".mp4";
        if (mSaveVideoPath.equals("")) {
            mSaveVideoPath = Environment.getExternalStorageDirectory().getPath();
        }

        mVideoFileAbsolutePath = mSaveVideoPath + File.separator + mVideoFileName;
        mMediaRecorder.setOutputFile(mVideoFileAbsolutePath);

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsRecorder = true;
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError();
        }
    }

    public void stopRecord(boolean isShort, StopRecordCallback callback) {
        if (!mIsRecorder) {
            return;
        }

        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException e) {
                e.printStackTrace();
                mMediaRecorder = null;
                mMediaRecorder = new MediaRecorder();
            } catch (Exception e) {
                e.printStackTrace();
                mMediaRecorder = null;
                mMediaRecorder = new MediaRecorder();
            } finally {
                if (mMediaRecorder != null) {
                    mMediaRecorder.release();
                }
                mMediaRecorder = null;
                mIsRecorder = false;
            }

            if (isShort) {
                //delete video file
                boolean result = true;
                File file = new File(mVideoFileAbsolutePath);
                if (file.exists()) {
                    result = file.delete();
                }
                if (result) {
                    callback.recordResult(null);
                }
                return;
            }

            doStopCamera();
            String fileName = mSaveVideoPath + File.separator + mVideoFileName;
            callback.recordResult(fileName);
        }
    }

    private void findAvailableCameras() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int cameraNum = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraNum; i++) {
            Camera.getCameraInfo(i, info);
            switch (info.facing) {
                case Camera.CameraInfo.CAMERA_FACING_FRONT:
                    CAMERA_FRONT_POSITION = info.facing;
                    break;
                case Camera.CameraInfo.CAMERA_FACING_BACK:
                    CAMERA_POST_POSITION = info.facing;
                    break;
            }
        }
    }

    public void handleFocus(final Context context, final float x, final float y, final FocusCallback callback) {
        if (mCamera == null) {
            return;
        }

        final Camera.Parameters params = mCamera.getParameters();
        Rect focusRect = calculateTapArea(x, y, 1f, context);
        mCamera.cancelAutoFocus();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            callback.focusSuccess();
            return;
        }

        final String currentFocusMode = params.getFocusMode();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                    callback.focusSuccess();
                } else {
                    handleFocus(context, x, y, callback);
                }
            }
        });
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Context context) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / ScreenUtils.getScreenHeight(context) * 2000 - 1000);
        int centerY = (int) (y / ScreenUtils.getScreenWidth(context) * 2000 - 1000);

        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }

        if (x < min) {
            return min;
        }

        return x;
    }

    public void setErrorListener(ErrorListener errorListener) {
        this.mErrorListener = errorListener;
    }


    public interface StopRecordCallback {
        void recordResult(String url);
    }

    public interface ErrorCallback {
        void onError();
    }

    public interface TakePictureCallback {
        void captureResult(Bitmap bitmap);
    }

    public interface FocusCallback {
        void focusSuccess();

    }

    public void registerSensorManager(Context context) {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }

        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorManager(Context context) {
        if (mSensorManager == null) {
            mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }

        mSensorManager.unregisterListener(sensorEventListener);
    }
}
