package me.juhezi.ding.newcapture.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.support.annotation.StringDef;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将 Camera 的行为包装起来
 * <p>
 * use TextureView to display
 * Need to set PreviewSize
 * Created by Juhezi[juhezix@163.com] on 2017/12/13.
 */

public class CameraWrapper {

    private static final String TAG = "CameraWrapper";

    //默认摄像头 Id 为后置摄像头
    private static final int DEFAULT_CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private static final String FLASH_MODE_TORCH = "torch"; //手电筒模式，直接开启
    private static final String FLASH_MODE_ON = "on";   //普通模式，只有在进行拍照或者录像的时候开启
    private static final String FLASH_MODE_OFF = "off"; //关闭手电筒

    @StringDef({FLASH_MODE_OFF, FLASH_MODE_ON, FLASH_MODE_TORCH})
    private @interface FlashMode {
    }

    private int mHeight;
    private int mWidth;

    private SurfaceTexture mSurfaceTexture;

    private Camera mCamera;

    //    private Callback mCallback;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;

    private int mCurrentCameraId; //当前开启的摄像头ID

    public void prepare(int width, int height, SurfaceTexture surfaceTexture) {
        mCamera = getCameraInstance(mCurrentCameraId);  //获取默认的摄像头
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters parms = mCamera.getParameters();
        choosePreviewSize(parms, width, height);
        mCamera.setParameters(parms);
        int degree = getDisplayDegree(mCurrentCameraId);
        mCamera.setDisplayOrientation(degree);
        Camera.Size size = parms.getPreviewSize();
        mWidth = size.width;
        mHeight = size.height;
        mSurfaceTexture = surfaceTexture;
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startPreview() {
        if (mCamera != null) {
            try {
                mCamera.startPreview();
            } catch (Exception ignored) {

            }
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
            } catch (Exception ignored) {
            }
        }
    }

    public void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public void switchCamera() {
        if (mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {   //切换为前置摄像头
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {    //切换为后置摄像头
            mCurrentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        release();
        prepare(mWidth, mHeight, mSurfaceTexture);
        startPreview();
    }

    /**
     * 获取摄像头个数
     * 以此判断是否能够切换摄像头
     */
    public int getCameraCount() {
        return Camera.getNumberOfCameras();
    }

    public boolean isFrontCamera() {
        return mCurrentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    /**
     * zoom 0.0 - 1.0
     *
     * @param zoom
     */
    public void setZoom(float zoom) {
        if (mCamera == null)
            return;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters == null || !parameters.isZoomSupported())
                return;
            int max = parameters.getMaxZoom();
            parameters.setZoom((int) (max * zoom));
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takeFocus(Camera.Area focusArea, Camera.Area meteringArea) {

        Log.i(TAG, "takeFocus: " + focusArea.rect + " " + meteringArea.rect);
        if (mCamera == null) return;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode("auto");
            ArrayList<Camera.Area> meteringAreas;
            if (parameters.getMaxNumFocusAreas() > 0) {
                meteringAreas = new ArrayList<>();
                meteringAreas.add(focusArea);
                parameters.setFocusAreas(meteringAreas);
            }

            if (parameters.getMaxNumMeteringAreas() > 0) {
                meteringAreas = new ArrayList<>();
                meteringAreas.add(meteringArea);
                parameters.setMeteringAreas(meteringAreas);
            }

            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFlash() {
        internalControlFlash(FLASH_MODE_TORCH);
    }

    public void closeFlash() {
        internalControlFlash(FLASH_MODE_OFF);
    }

    private void internalControlFlash(@FlashMode String flashMode) {
        if (mCamera == null) return;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters == null) return;
            List<String> list = parameters.getSupportedFlashModes();
            if (list != null && list.contains(flashMode)) {
                parameters.setFlashMode(flashMode);
            }
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 系统的拍照方法
     *
     * @param targetPath 生成的照片路径
     * @param callback   拍照完成的回调
     */
    public void takePicture(final String targetPath, final PictureCallback callback) {
        if (mCamera == null) return;
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                if (TextUtils.isEmpty(targetPath)) return;
                try {
                    Bitmap bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                    FileOutputStream fos = new FileOutputStream(targetPath);
                    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                        fos.close();
                    }
                    bitmap.recycle();
                    // TODO: 2017/12/20 根据当前相机角度，判断是否要对图片进行镜像处理
                    if (callback != null) {
                        callback.onPictureToken();  //拍照完成时进行回调
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int getCurrentCameraId() {
        return mCurrentCameraId;
    }

    private void setSurfaceTextureListener(TextureView.SurfaceTextureListener surfaceTextureListener) {
        this.mSurfaceTextureListener = surfaceTextureListener;
    }

    /**
     * 如果 object 为空的话，则直接抛出异常
     *
     * @param object
     */
    public void checkNull(Object object, String name) {
        if (object == null) {
            throw new NullPointerException("The " + name + " can not be null!");
        }
    }

    //应该有统一的错误处理机制
    //-----------------------static ---------------------

    /**
     * include error handling
     */
    public static Camera getCameraInstance() {
        return getCameraInstance(DEFAULT_CAMERA_ID);
    }

    public static Camera getCameraInstance(int cameraId) {
        Camera camera = null;
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            Log.w(TAG, "getCameraInstance: open camera[@id:" + cameraId + "] failed.");
            e.printStackTrace();
        }
        return camera;
    }

    /**
     * 获取显示角度
     *
     * @param cameraId
     * @return
     */
    public static int getDisplayDegree(int cameraId) {
        int degree;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        degree = cameraInfo.orientation % 360;
        if (cameraInfo.facing == 1) {
            degree = (360 - degree) % 360;
        }
        if (degree == 0) {
            degree = 180;
        } else if (degree == 180) {
            degree = 0;
        }
        return degree;
    }

    public static void choosePreviewSize(Camera.Parameters parms, int width, int height) {
        // We should make sure that the requested MPEG size is less than the preferred
        // size, and has the same aspect ratio.
        Camera.Size ppsfv = parms.getPreferredPreviewSizeForVideo();

        for (Camera.Size size : parms.getSupportedPreviewSizes()) {
            if (size.width == width && size.height == height) {
                parms.setPreviewSize(width, height);
                return;
            }
        }

        if (ppsfv != null) {
            parms.setPreviewSize(ppsfv.width, ppsfv.height);
        }
    }

    public interface PictureCallback {
        void onPictureToken();
    }

}
