package me.juhezi.module.base.router.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import me.juhezi.module.base.router.buildIntent

/**
 * Created by Juhezi on 2017/8/17.
 */

fun Activity.turn(key: String, action: Intent.() -> Unit = {}) = startActivity(buildIntent(this, key, action)) //这个是最基本的跳转

fun Activity.turnForResult(key: String, requestCode: Int, action: Intent.() -> Unit = {}) =
        startActivityForResult(buildIntent(this, key, action), requestCode)

fun Fragment.turn(key: String, action: Intent.() -> Unit = {}) {
    if (context != null) {
        startActivity(buildIntent(context!!, key, action))
    }
}

fun Fragment.turnForResult(key: String, requestCode: Int, action: Intent.() -> Unit = {}) {
    if (context != null) {
        startActivityForResult(buildIntent(context!!, key, action), requestCode)
    }
}
