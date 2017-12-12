package me.juhezi.module.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import me.juhezi.module.base.builder.buildUIHandler
import me.juhezi.module.base.extensions.getAppVersion
import me.juhezi.module.base.extensions.getAppVersionName

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/14.
 */
val TAG = "me.juhezi:TAG"

fun run(handler: Handler, runnable: Runnable) = handler.post(runnable)

fun runInUIThread(runnable: Runnable) = buildUIHandler().post(runnable)

fun isDebug() = BuildConfig.DEBUG

fun logi(message: String) {
    if (isDebug()) {
        Log.i(TAG, message)
    }
}

fun logd(message: String) {
    if (isDebug()) {
        Log.d(TAG, message)
    }
}

@SuppressLint("MissingPermission", "HardwareIds")
fun getIMEI(): String {
    var telManager = BaseApplication.getInstance()
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return telManager.deviceId
}

fun getVersionName() =
        BaseApplication.getInstance().getAppVersionName()

fun getVersionCode() =
        BaseApplication.getInstance().getAppVersion()