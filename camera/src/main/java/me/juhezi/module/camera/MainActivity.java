package me.juhezi.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.juhezi.module.router_annotation.annotation.Register;

@Register
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_main);
    }

}

//通过外界来显示进度条
//mProgressBar.update(cameraButton.getDistanceTime() / (float) maxTime);