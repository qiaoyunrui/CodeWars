package me.juhezi.module.base.router

import me.juhezi.module.base.view.activity.DefaultActivity
import me.juhezi.module.router_api.IRouterProxy

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/3.
 */
class BaseRouterProxy : IRouterProxy {

    override fun proxy() {
        //对 Base 包中的路由进行注册
        //Activity
        Repository.register(DefaultActivity::class.java)
        //Service

        //BroadCast

        //ContentProvider

        //Web
    }
}
