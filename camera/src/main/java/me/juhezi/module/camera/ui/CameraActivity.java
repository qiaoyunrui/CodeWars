package me.juhezi.module.camera.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.juhezi.module.router_annotation.annotation.Register;

@Register
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(me.juhezi.module.camera.R.layout.activity_camera);     //这里要注意一定要指定对应的包名
    }

}