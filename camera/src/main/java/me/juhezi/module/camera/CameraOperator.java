package me.juhezi.module.camera;

import android.app.Activity;
import android.graphics.Rect;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/27.
 */

public interface CameraOperator {

    public void startPreview(CameraPreviewConfig config);

    public void stopPreview();

    public boolean startRecord(CameraRecordConfig config);

    public void stopRecord();

    public void takePicture(Activity activity, OnSaveCameraPictureListener callback);

    public void destroy();

    public void switchCamera();

    public void setZoom(float ratio);

    public void toggleFlash();

    public boolean isFrontCamera();

    public int getCameraId();

    public int startTouchAutoFocus(Rect focusArea, Rect meteringArea);

}
