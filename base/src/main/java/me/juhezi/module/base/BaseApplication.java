package me.juhezi.module.base;

import android.app.Application;

import com.juhezi.module.router_annotation.annotation.Proxy;

import me.juhezi.module.router_api.Router;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */
@Proxy
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.inject(this);
    }
}
