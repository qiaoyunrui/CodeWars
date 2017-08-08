package me.juhezi.module.base.router

import android.content.Intent

/**
 * 路由仓库
 * Created by Juhezi[juhezix@163.com] on 2017/8/8.
 */
interface IRepository<T> {

    fun get(key: String): T?

    fun put(key: String, value: T)

    fun size(): Int

}