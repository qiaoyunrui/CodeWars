package me.juhezi.module.base

import android.app.Application

import com.juhezi.module.router_annotation.annotation.Proxy

import me.juhezi.module.base.router.BaseRouterProxy
import me.juhezi.module.router_api.Router

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        onInject()
    }

    open fun onInject() {
        Router.inject(BaseRouterProxy())
    }

}
