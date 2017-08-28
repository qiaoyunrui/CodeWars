package me.juhezi.module.base.extensions

import android.text.TextUtils
import android.util.Log
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/4.
 */
fun Any.i(message: String) = Log.i(javaClass.simpleName, message)

fun Any.e(message: String) = Log.e(javaClass.simpleName, message)

fun Any.w(message: String) = Log.w(javaClass.simpleName, message)

fun Any.v(message: String) = Log.v(javaClass.simpleName, message)

fun Any.d(message: String) = Log.d(javaClass.simpleName, message)

fun Any.i() = Log.i(javaClass.simpleName, this.toString())

fun Any.e() = Log.e(javaClass.simpleName, this.toString())

fun Any.w() = Log.w(javaClass.simpleName, this.toString())

fun Any.v() = Log.v(javaClass.simpleName, this.toString())

fun Any.d() = Log.d(javaClass.simpleName, this.toString())

fun Any.isEmpty(content: String?) = TextUtils.isEmpty(content)

fun Any.println() = println(this)

fun Any.print() = print(this)

inline fun <T : Any> T.desc() = this::class.memberProperties
        .joinToString(separator = ";") { "${it.name} : ${it.getUnsafed(this@desc)}" }

@Suppress("UNCHECKED_CAST")
fun <T, R> KProperty1<T, R>.getUnsafed(receiver: Any): R = get(receiver as T)
