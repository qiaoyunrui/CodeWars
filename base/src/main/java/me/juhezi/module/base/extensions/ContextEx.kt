package me.juhezi.module.base.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import me.juhezi.module.base.R
import java.io.File

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

/**
 * 获取缓存地址
 * 如果 SD 卡存在或者 SD 卡不可被移除的时候，获取到的是：/sdcard/Android/data/<application package>/cache
 * 否则，获取到的是：/data/data/<application package>/cache
 */
fun Context.getDiskCacheDir(uniqueName: String = "common"): File {
    val cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() ||
            !Environment.isExternalStorageRemovable())
        externalCacheDir.path
    else
        cacheDir.path
    return File("$cachePath${File.separator}$uniqueName")
}

/**
 * 获取应用程序版本号
 */
fun Context.getAppVersion(): Int {
    try {
        val info = packageManager.getPackageInfo(packageName, 0)
        return info.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return 1
}




