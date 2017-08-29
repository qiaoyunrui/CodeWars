package me.juhezi.module.router_api;

import android.app.Application;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */

public class Router {

    public static void inject(Application application) {
        if (application == null) return;
        IRouterProxy iRouterProxy = findProxy(application.getClass().getName());
        if (iRouterProxy != null) {
            iRouterProxy.proxy();
        }
    }

    private static IRouterProxy findProxy(String name) {
        //寻找 Proxy 类
        return null;
    }

}
