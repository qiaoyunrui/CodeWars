package me.juhezi.module.base.extensions

import android.text.TextUtils
import android.util.Log

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/4.
 */
fun Any.i(message: String) = Log.i(javaClass.simpleName, message)

fun Any.e(message: String) = Log.e(javaClass.simpleName, message)

fun Any.w(message: String) = Log.w(javaClass.simpleName, message)

fun Any.v(message: String) = Log.v(javaClass.simpleName, message)

fun Any.d(message: String) = Log.d(javaClass.simpleName, message)

fun Any.isEmpty(content: String?) = TextUtils.isEmpty(content)
