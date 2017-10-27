package me.juhezi.module.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.weibo.movieeffect.liveengine.Constants;
import com.weibo.movieeffect.liveengine.config.Config;
import com.weibo.movieeffect.liveengine.core.EncodingEngine;
import com.weibo.movieeffect.liveengine.display.GLTextureView;
import com.weibo.movieeffect.liveengine.log.StatisticsInfoBundle;
import com.weibo.story.config.StoryBundle;
import com.weibo.story.core.CameraMicCallback;
import com.weibo.story.core.StoryController;
import com.weibo.story.util.ConfigMessageUtil;

public class VideoCaptureController implements CameraOperator {

    private static final String TAG = "VideoCaptureController";

    private static final int DEFAULT_CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private StoryController mController;
    private Context mContext;
    private boolean isRecycled; //?
    private GLTextureView mTextureView;
    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    public VideoCaptureController(Context context,
                                  GLTextureView textureView) {
        mContext = context;
        mTextureView = textureView;
        Config config = new Config();
        config.setCurrentCameraId(DEFAULT_CAMERA_ID);
        config.setVideoEncodec(Config.ENCODEC_MEDIACODEC);
        mController = new StoryController(mContext.getApplicationContext(),
                mTextureView,
                new EncodingEngine.StateCallback() {

                    @Override
                    public void onEncodingSuccess() {

                    }

                    @Override
                    public void onError(final int error) {
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (isRecycled) {
                                    return;
                                }
                                String errorMessage = "该视频无法播放，请重新选择";
                                if (ConfigMessageUtil.getErrorCode(error) == StoryBundle.STORY_TYPE_PHOTO) {
                                    errorMessage = "该图片不存在，请重新选择";
                                }
                                if (error == EncodingEngine.StateCallback.ERROR_OPEN_MIC_FILED) {
                                    errorMessage = "请在手机“设置”中，允许微博访问你的相机和麦克风";
                                }
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onRenderConfigDone() {

                    }

                    @Override
                    public void encodingProgress(int i) {

                    }

                    @Override
                    public void onPreviewProgress(int i) {

                    }

                    @Override
                    public void onStatInfo(StatisticsInfoBundle statisticsInfoBundle) {

                    }
                }, config);
    }

    @Override
    public void startPreview(CameraPreviewConfig config) {
        mController.onBack(Constants.STORY_CONTROLLER_STATE.RECORDER);
        mController.startPreview();
    }

    @Override
    public void stopPreview() {
        mController.onLeave();
    }

    @Override
    public boolean startRecord(CameraRecordConfig config) {
        StoryBundle bundle = new StoryBundle();
        bundle.setType(StoryBundle.STORY_TYPE_VIDEO);
        mController.setStoryBundle(bundle);
        // TODO: 2017/10/27 Continue
        //Media Cache
        return false;
    }

    @Override
    public void stopRecord() {

    }

    @Override
    public void takePicture(Activity activity, OnSaveCameraPictureListener callback) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void switchCamera() {

    }

    @Override
    public void setZoom(float ratio) {

    }

    @Override
    public void toggleFlash() {

    }

    @Override
    public boolean isFrontCamera() {
        return false;
    }

    @Override
    public int getCameraId() {
        return 0;
    }

    @Override
    public int startTouchAutoFocus(Rect focusArea, Rect meteringArea) {
        return 0;
    }

    public StoryController getInternalController() {
        return mController;
    }

    public void setCameraMicCallback(CameraMicCallback callback) {
        mController.setCameraMicCallback(callback);
    }

    public void onResume() {
        mController.onResume();
        Log.i(TAG, "onResume: what?");
    }

    public void onPause() {
        mController.onPause();
    }
}
