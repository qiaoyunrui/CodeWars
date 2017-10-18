package me.juhezi.module.camera;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.juhezi.module.router_annotation.annotation.Register;

import me.juhezi.module.base.router.Repository;

@Register
public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository.Companion.print();
    }
}
