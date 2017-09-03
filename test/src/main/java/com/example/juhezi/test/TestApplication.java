package com.example.juhezi.test;

import com.juhezi.module.router_annotation.annotation.Proxy;

import me.juhezi.module.base.BaseActivity;
import me.juhezi.module.base.BaseApplication;
import me.juhezi.module.router_api.Router;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/3.
 */
@Proxy
public class TestApplication extends BaseApplication {

    @Override
    public void onInject() {
        super.onInject();
        Router.inject(this);
    }
}
