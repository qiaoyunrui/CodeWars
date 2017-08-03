package me.juhezi.module.base.extensions

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import me.juhezi.module.base.R

/**
 * Context 的扩展方法
 * Created by Juhezi[juhezix@163.com] on 2017/8/2.
 */

fun Context.dp2px(dp: Float): Int = (resources.displayMetrics.density * dp + 0.5f).toInt()

fun Context.sp2px(sp: Float): Int = (resources.displayMetrics.scaledDensity * sp + 0.5f).toInt()

fun Context.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun Context.px2sp(px: Float): Int {
    val scale = resources.displayMetrics.scaledDensity
    return (px / scale + 0.5f).toInt()
}

private fun Context.getScreenMetrics(): DisplayMetrics {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val outMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(outMetrics)
    return outMetrics
}

fun Context.getScreenHeight() = getScreenMetrics().heightPixels

fun Context.getScreenWidth() = getScreenMetrics().widthPixels

fun Context.toast(message: String = getString(R.string.app_name)) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String = getString(R.string.app_name)) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

