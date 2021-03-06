package me.juhezi.module.base.router

import android.content.Context
import android.content.Intent

/**
 * Created by Juhezi on 2017/8/17.
 */
fun Intent.modify(action: Intent.() -> Unit) = apply(action)

fun buildIntent(context: Context, key: String, action: Intent.() -> Unit = {}): Intent {
    val clazz = Repository.getActivityClass(key)    // -> 这里需要修改
    return IntentBuilder(context, clazz).build().modify(action)
}

fun Any.register() = Repository.register(this::class.java)