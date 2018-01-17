package me.juhezi.ding.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import me.juhezi.ding.R;
import me.juhezi.ding.newcapture.core.NewCameraOperator;
import me.juhezi.ding.newcapture.core.WBCameraKit;
import me.juhezi.ding.newcapture.util.CameraHelper;
import me.juhezi.module.base.BaseActivity;
import me.juhezi.module.base.widget.CameraButton;
import me.juhezi.module.base.widget.MultiSegmentProgressBar;
import me.juhezi.module.base.widget.TabViewGroup;

/**
 * 视频拍摄页
 * Created by Juhezi[juhezix@163.com] on 2017/12/20.
 */

public class VideoCaptureActivity extends BaseActivity {

    private static final String TAG = "TempVideoCaptureActivit";

    private TextureView mGLTVPreview;
    private ViewGroup mVgButtonContainer;
    private ImageView mIvRemove;
    private CameraButton mCbCapture;
    private ImageView mIvNext;
    private ImageView mIvClose;
    private ImageView mIvFlash;
    private ImageView mIvAction;
    private ImageView mIvOverlay;
    private MultiSegmentProgressBar mProgressBar;
    private TabViewGroup mTvgTouch;
    private ImageView mIvTouch;

    private ValueAnimator mFocusAnim;  //聚焦动画

    private WBCameraKit mCameraKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏
    }

    @Override
    protected void installViews() {
        super.installViews();
        setContentView(R.layout.activity_capture);
        Toast.makeText(this, "🐷🐷 正在使用 CameraKit!! 🐷🐷", Toast.LENGTH_SHORT).show();
        initView();
        mCameraKit = WBCameraKit.create(this, mGLTVPreview);
        mFocusAnim = ValueAnimator.ofFloat(1f, 1.5f);
        mFocusAnim.setInterpolator(new AccelerateInterpolator());
        mFocusAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIvTouch.setScaleX((Float) animation.getAnimatedValue());
                mIvTouch.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        mFocusAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mIvTouch.setVisibility(View.GONE);
            }
        });
        initEvent();
    }

    private void initView() {
        showContent();
        setToolBarVisibility(false);    //隐藏 Toolbar
        //Load View
        mGLTVPreview = (TextureView) findViewById(R.id.gltv_video_capture_preview);
        mVgButtonContainer = (ViewGroup) findViewById(R.id.fl_video_capture_button_container);
        mIvRemove = (ImageView) findViewById(R.id.iv_video_capture_remove);
        mCbCapture = (CameraButton) findViewById(R.id.cb_video_capture);
        mIvNext = (ImageView) findViewById(R.id.iv_video_capture_next);
        mIvClose = (ImageView) findViewById(R.id.iv_video_capture_close);
        mIvFlash = (ImageView) findViewById(R.id.iv_video_capture_flash);
        mIvAction = (ImageView) findViewById(R.id.iv_video_capture_action);
        mProgressBar = (MultiSegmentProgressBar) findViewById(R.id.mspb_video_capture_progress);
        mTvgTouch = (TabViewGroup) findViewById(R.id.tvg_video_capture_touch_view);
        mIvTouch = (ImageView) findViewById(R.id.iv_video_capture_touch_sign);
    }

    void initEvent() {
        // 拍照
        mCbCapture.setOnSingleClickListener(new CameraButton.OnSingleClickListener() {
            @Override
            public void onClick() {
                mCameraKit.takePicture(Environment.getExternalStorageDirectory() + "/hello.jpg", new NewCameraOperator.PictureCallback() {
                    @Override
                    public void onPictureToken() {
                        Log.i(TAG, "onPictureToken: 拍照完成");
                    }
                });
            }
        });
        mCbCapture.setOnZoomListener(new CameraButton.OnZoomListener() {
            @Override
            public void onZoom(float scale) {
                float temp = scale / 7f - 0.14f;
                if (temp < 0f) temp = 0f;
                if (temp > 1f) temp = 1f;
                mCameraKit.setZoom(temp);
            }
        });
        mIvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //切换摄像头
                switchCamera();
            }
        });
        mIvFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraKit.toggleFlash();
                updateFlashIcon();
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvgTouch.setOnEventListener(new TabViewGroup.OnEventListener() {
            @Override
            public void onScale(float scale) {
                mCameraKit.setZoom(scale);
            }

            @Override
            public void onSingleTab(View view, MotionEvent event) {
                mIvTouch.setX(event.getX());
                mIvTouch.setY(event.getY());
                mIvTouch.setVisibility(View.VISIBLE);
                mFocusAnim.start();
                Rect focusRect =
                        CameraHelper.calculateTapArea(view, event.getX(), event.getY(), 1f);
                Rect meteringRect =
                        CameraHelper.calculateTapArea(view, event.getX(), event.getY(), 1.5f);
                mCameraKit.takeFocus(focusRect, meteringRect);
            }

            @Override
            public void onDoubleTab() {
                switchCamera();
            }

            @Override
            public void onSlide(float splitX, float splitY, boolean fling) {
            }

        });

    }

    /**
     * 更新闪光灯图标
     */
    private void updateFlashIcon() {
        if (mCameraKit.isFlashEnable()) {
            mIvFlash.setImageResource(R.drawable.video_capture_flash_open);
        } else {
            mIvFlash.setImageResource(R.drawable.video_capture_flash_close);
        }
    }

    private void switchCamera() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.video_capture_switch_camera_anim);
        mIvAction.startAnimation(animation);
        mCameraKit.switchCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraKit.startPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCameraKit.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
