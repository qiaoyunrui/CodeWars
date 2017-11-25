package me.juhezi.module.base

import android.app.Application

import me.juhezi.module.base.router.BaseRouterProxy
import me.juhezi.module.router_api.Router

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */
open class BaseApplication : Application() {

    companion object {
        private lateinit var sInstance: BaseApplication
        @JvmStatic
        fun getInstance() = sInstance
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        onInject()
    }

    open fun onInject() {
        Router.inject(BaseRouterProxy())
    }

}
