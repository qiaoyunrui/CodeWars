package me.juhezi.module.base.router.activity

import android.app.Activity
import android.support.v4.app.Fragment
import me.juhezi.module.base.router.buildIntent

/**
 * Created by max on 2017/8/17.
 */
val respository = ActivityClassRepository()

//startActivity
fun Activity.turn(key: String) = startActivity(buildIntent(this, key)) //这个是最基本的跳转

fun Activity.turnForResult(key: String) {

}

fun Fragment.turn(key: String) = startActivity(buildIntent(context, key))

fun Fragment.turnForResult(key: String) {

}