package me.juhezi.module.base.router;

import me.juhezi.module.router_api.IRouterProxy;

/**
 * 注解生成的代理类，在 Application 中调用
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */

public class Router$Proxy implements IRouterProxy {

    @Override
    public void proxy() {
        Repository.register(me.juhezi.module.base.view.activity.DefaultActivity.class);
    }
}
