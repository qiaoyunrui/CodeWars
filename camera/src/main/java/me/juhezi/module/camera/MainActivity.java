package me.juhezi.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.juhezi.module.router_annotation.annotation.Register;
import com.weibo.movieeffect.liveengine.display.GLTextureView;
import com.weibo.story.core.CameraMicCallback;

@Register
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final boolean DEFAULT_FLASH_ENABLE = false;

    private GLTextureView mTvPreview;
    private VideoCaptureController mVideoCaptureController;

    private Boolean mFlashEnable = DEFAULT_FLASH_ENABLE;

    private static Handler mUiHandler = new Handler(Looper.getMainLooper());

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        mVideoCaptureController = new VideoCaptureController(this, mTvPreview);
        mVideoCaptureController.setCameraMicCallback(mPreviewStartListener);
    }

    private void initView() {
        mTvPreview = (GLTextureView) findViewById(R.id.tv_video_capture_preview);
    }

    private void initEvent() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoCaptureController.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoCaptureController.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private CameraMicCallback mPreviewStartListener = new CameraMicCallback() {
        @Override
        public void onCameraOpenDone() {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // TODO: 2017/10/27 Deal Flash Icon
                    Log.i(TAG, "run: Hello");
                }
            });
        }

        @Override
        public void onOpenCameraFailed() {

        }

        @Override
        public void onOpenMicFailed() {

        }
    };

}

//通过外界来显示进度条
//mProgressBar.update(cameraButton.getDistanceTime() / (float) maxTime);