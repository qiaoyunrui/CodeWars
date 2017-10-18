package me.juhezi.module.camera;

import android.app.Application;

import com.juhezi.module.router_annotation.annotation.Proxy;

import me.juhezi.module.base.BaseApplication;
import me.juhezi.module.router_api.Router;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/18.
 */
@Proxy
public class CameraApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.inject(this);
    }
}
