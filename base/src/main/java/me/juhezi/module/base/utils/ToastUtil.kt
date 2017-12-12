package me.juhezi.module.base.utils

import android.widget.Toast
import me.juhezi.module.base.BaseApplication
import me.juhezi.module.base.runInUIThread

/**
 * Show Short Toast
 * Show Long Toast
 * Created by Juhezi[juhezix@163.com] on 2017/9/24.
 */

fun showShortToast(message: String) =
        runInUIThread(Runnable {
            showToast(message, Toast.LENGTH_SHORT)
        })

fun showLongToast(message: String) =
        runInUIThread(Runnable {
            showToast(message, Toast.LENGTH_LONG)
        })

private fun showToast(message: String, duration: Int) {
    Toast.makeText(BaseApplication.getInstance(), message, duration).show()
}
