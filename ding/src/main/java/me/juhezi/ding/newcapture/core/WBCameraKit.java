package me.juhezi.ding.newcapture.core;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.Surface;
import android.view.TextureView;

/**
 * StoryController 中关于拍照录像的一部分
 * <p>
 * Created by Juhezi[juhezix@163.com] on 2017/12/20.
 */
public class WBCameraKit implements NewCameraOperator {

    private static final String TAG = "WBCameraKit";

    private final static boolean DEFAULT_FLASH_ENABLE = false;

    private CameraWrapper mCameraWrapper;
    private TextureView mTextureView;

    private Surface mSurface;

    private final Object mRenderMutex;

    private Context mContext;

    private boolean mFlashEnbale = DEFAULT_FLASH_ENABLE;

    public static WBCameraKit create(Context context, TextureView textureView) {
        WBCameraKit cameraKit = new WBCameraKit(context);
        cameraKit.init(textureView);
        return cameraKit;
    }

    private WBCameraKit(Context context) {
        mContext = context.getApplicationContext();
        mCameraWrapper = new CameraWrapper();
        mRenderMutex = new Object();
    }

    private void init(TextureView textureView) {
        mTextureView = textureView;
        /*mCameraWrapper.prepare(1920, 1080,tet);*/
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mCameraWrapper.prepare(1920, 1080, surface);
                mCameraWrapper.startPreview();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mCameraWrapper.release();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    @Override
    public void startPreview() {
        mCameraWrapper.startPreview();
    }

    @Override
    public void stopPreview() {
        mCameraWrapper.stopPreview();
    }

    @Override
    public void release() {
        mCameraWrapper.release();
    }

    @Override
    public void takePicture(String targetPath, final PictureCallback callback) {
        // TODO: 2017/12/20 当有 AR 的时候，使用截屏的方式
        mCameraWrapper.takePicture(targetPath, new CameraWrapper.PictureCallback() {
            @Override
            public void onPictureToken() {
                if (callback != null)
                    callback.onPictureToken();
            }
        });
    }

    @Override
    public void startRecord(String targetPath, RecordCallback callback) {

    }

    @Override
    public void stopRecord() {

    }

    @Override
    public void switchCamera() {
        mCameraWrapper.switchCamera();
    }

    @Override
    public boolean isFrontCamera() {
        return Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraWrapper.getCurrentCameraId();
    }

    @Override
    public void toggleFlash() {
        if (mFlashEnbale) {     //关闭闪光灯
            mCameraWrapper.closeFlash();
        } else {    //开启闪光灯
            mCameraWrapper.openFlash();
        }
        mFlashEnbale = !mFlashEnbale;
    }

    @Override
    public boolean isFlashEnable() {
        return mFlashEnbale;
    }

    @Override
    public void takeFocus(Rect focusArea, Rect meteringArea) {
        mCameraWrapper.takeFocus(new Camera.Area(focusArea, 1000), new Camera.Area(meteringArea, 1000));
    }

    @Override
    public void setZoom(float scale) {
        mCameraWrapper.setZoom(scale);
    }


}
