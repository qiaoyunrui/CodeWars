package me.juhezi.module.base.router

import android.content.Intent
import android.support.v4.util.ArrayMap

//v4 包中的 ArrayMap 和 普通的 ArrayMap 的实现原理是一样的。

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/8.
 */
open class ClassRepository<T> : IRepository<Class<out T>> {


    private val mMap = ArrayMap<String, Class<out T>>()

    override fun get(key: String) = mMap[key]

    override fun put(key: String, value: Class<out T>) {
        mMap.put(key, value)
    }

    override fun size() = mMap.size

}