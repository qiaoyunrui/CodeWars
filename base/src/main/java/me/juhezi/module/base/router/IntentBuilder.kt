package me.juhezi.module.base.router

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * Created by Juhezi on 2017/8/17.
 */
class IntentBuilder<T>(context: Context, clazz: Class<out T>) {

    private val intent: Intent = Intent(context, clazz)

    fun modify(action: Intent.() -> Intent) = intent.run(action)

    fun build() = intent

}