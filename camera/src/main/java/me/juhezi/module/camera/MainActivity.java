package me.juhezi.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.juhezi.module.router_annotation.annotation.Register;

import me.juhezi.module.base.widget.CameraButton;

@Register
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);
        CameraButton cameraButton = (CameraButton) findViewById(R.id.cb_test);
        cameraButton.setOnSingleClickListener(new CameraButton.OnSingleClickListener() {
            @Override
            public void onClick() {
                Log.i(TAG, "onSingleClick: ");
            }
        });
        cameraButton.setOnLongClickListener(new CameraButton.OnLongClickListener() {
            @Override
            public void onStartLongClick() {
                Log.i(TAG, "onStartLongClick: ");
            }

            @Override
            public void onEndLongClick() {
                Log.i(TAG, "onEndLongClick: ");
            }

            @Override
            public void onCancelLongClick() {
                Log.i(TAG, "onCancelLongClick: ");
            }
        });
        cameraButton.setOnZoomListener(new CameraButton.OnZoomListener() {
            @Override
            public void onZoom(float scale) {
                Log.i(TAG, "onZoom: " + scale);
            }
        });
    }
}
