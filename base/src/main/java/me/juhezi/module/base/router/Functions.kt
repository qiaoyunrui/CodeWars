package me.juhezi.module.base.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import me.juhezi.module.base.router.activity.turn

/**
 * Created by Juhezi on 2017/8/17.
 */
fun Intent.modify(action: Intent.() -> Unit) = apply(action)

fun buildIntent(context: Context, key: String): Intent {
    val clazz = Repository.getActivityClass(key)    // -> 这里需要修改
    return IntentBuilder(context, clazz).build()
}