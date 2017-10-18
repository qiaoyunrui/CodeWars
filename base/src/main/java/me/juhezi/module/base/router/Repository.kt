package me.juhezi.module.base.router

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.os.Build
import android.support.annotation.RequiresApi
import me.juhezi.module.base.extensions.i
import me.juhezi.module.base.router.activity.ActivityClassRepository
import me.juhezi.module.base.router.service.ServiceClassRepository
import me.juhezi.module.base.router.web.WebRepository
import me.juhezi.module.base.utils.arrayMapOf

/**
 * 总仓库
 *
 * Created by Juhezi on 2017/8/17.
 */
val ACTIVITY = Activity::class.java.name
val SERVICE = Service::class.java.name
val BROADCAST_RECEIVE = BroadcastReceiver::class.java.name
val CONTENT_PROVIDER = ContentProvider::class.java.name
val OBJECT = Object::class.java.name
val INVALID = "invalid"

const val WEB = 0x05

/**
 * 获取类型
 */
fun Class<*>.type(): String {
    while (OBJECT != name) {
        if (ACTIVITY == name) return ACTIVITY
        if (SERVICE == name) return SERVICE
        if (BROADCAST_RECEIVE == name) return BROADCAST_RECEIVE
        if (CONTENT_PROVIDER == name) return CONTENT_PROVIDER
        return if (superclass == null) {
            INVALID
        } else {
            superclass.type()
        }
    }
    return OBJECT
}

infix fun Class<*>.type(t: String): Boolean {
    while (OBJECT != name) {
        if (t == name) return true
        return if (superclass == null) {
            false
        } else {
            superclass.type(t)
        }
    }
    return false
}

class Repository {

    companion object {
        private val map = arrayMapOf(listOf(ACTIVITY to ActivityClassRepository(),
                SERVICE to ServiceClassRepository(),
                WEB to WebRepository()))

        @RequiresApi(Build.VERSION_CODES.N)
        fun print() {
            map.forEach { t, u -> "$t : $u".i() }
        }

        /**
         * 使用 Class 注册
         */
        @JvmStatic
        fun register(clazz: Class<*>) {
            //获取 Class 的类型
            val key = clazz.name
            val type = clazz.type()
            when (type) {
                ACTIVITY -> {
                    @Suppress("UNCHECKED_CAST")
                    (map[type] as ActivityClassRepository).put(key, clazz as Class<out Activity>)
                }
                SERVICE -> {
                    @Suppress("UNCHECKED_CAST")
                    (map[type] as ServiceClassRepository).put(key, clazz as Class<out Service>)
                }
            }
        }

        /**
         * 使用类名进行注册
         */
        @JvmStatic
        fun register(className: String) {
            val clazz = Class.forName(className)
            register(clazz)
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun getActivityClass(key: String): Class<out Activity> =
                (map[ACTIVITY] as ActivityClassRepository).get(key)

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun getServiceClass(key: String): Class<out Service>? =
                (map[SERVICE] as ServiceClassRepository).get(key)

    }

}
