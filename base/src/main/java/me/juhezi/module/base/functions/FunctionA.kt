package me.juhezi.module.base.functions

import android.os.Handler
import me.juhezi.module.base.builder.buildUIHandler

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/14.
 */
fun run(handler: Handler, runnable: Runnable) = handler.post(runnable)

fun runInUIThread(runnable: Runnable) = buildUIHandler().post(runnable)
