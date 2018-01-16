package me.juhezi.ding.newcapture.core;

import android.graphics.Rect;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/20.
 */

public interface NewCameraOperator {

    void startPreview();

    void stopPreview();

    void release();

    void takePicture(String targetPath, PictureCallback callback);

    void startRecord(String targetPath, RecordCallback callback);

    void stopRecord();

    void switchCamera();

    boolean isFrontCamera();

    void toggleFlash();

    boolean isFlashEnable();

    void takeFocus(Rect focusArea, Rect meteringArea);

    void setZoom(float scale);

    interface PictureCallback {
        void onPictureToken();
    }

    interface RecordCallback {
        void onRecordDone();
    }

}
