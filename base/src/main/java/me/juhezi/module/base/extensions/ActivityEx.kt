package me.juhezi.module.base.extensions

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/18.
 */

fun Activity.find(@IdRes id: Int): View? = findViewById(id)

