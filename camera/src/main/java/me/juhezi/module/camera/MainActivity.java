package me.juhezi.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.juhezi.module.router_annotation.annotation.Register;

import me.juhezi.module.base.widget.CameraButton;
import me.juhezi.module.base.widget.MultiSegmentProgressBar;

@Register
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private long maxTime = 5000;
    private long usedTime = 0;  //已经使用的时间

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);
        final MultiSegmentProgressBar mProgressBar = (MultiSegmentProgressBar) findViewById(R.id.mspb_test);
        final CameraButton cameraButton = (CameraButton) findViewById(R.id.cb_test);
        cameraButton.setClickMaxTime(maxTime);
        cameraButton.setOnSingleClickListener(new CameraButton.OnSingleClickListener() {
            @Override
            public void onClick() {
                Log.i(TAG, "onSingleClick: ");
            }
        });
        cameraButton.setOnLongClickListener(new CameraButton.OnLongClickListener() {
            @Override
            public void onStartLongClick() {
                mProgressBar.newSegmentProgress();
            }

            @Override
            public void onEndLongClick() {
                mProgressBar.done();
            }

            @Override
            public void onCancelLongClick() {
                mProgressBar.cancel();
            }
        });
        cameraButton.setOnZoomListener(new CameraButton.OnZoomListener() {
            @Override
            public void onZoom(float scale) {
            }
        });

    }

}

//通过外界来显示进度条
//mProgressBar.update(cameraButton.getDistanceTime() / (float) maxTime);