package me.juhezi.codewars;

import com.juhezi.module.router_annotation.annotation.Proxy;

import me.juhezi.module.base.BaseApplication;
import me.juhezi.module.base.router.Repository;
import me.juhezi.module.router_api.Router;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/18.
 */
@Proxy
public class MainApplication extends BaseApplication {

    @Override
    public void onInject() {
        super.onInject();
        Router.inject(this);
        routerRegist();
    }

    /**
     * 路由注册
     */
    private void routerRegist() {
        Repository.register("me.juhezi.module.camera.MainActivity");
    }

}
