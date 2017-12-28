package me.juhezi.module.engine.core.camera

import android.hardware.Camera

/**
 * Always check for exceptions when using Camera.open().
 * Failing to check for exceptions if the camera is in use or does not exist will cause your application to be shut down by the system.
 *
 * @param closure do something when error
 * Created by Juhezi[juhezix@163.com] on 2017/12/12.
 *
 */
inline fun getCameraInstance(closure: () -> Unit): Camera? {
    var camera: Camera? = null
    try {
        camera = Camera.open()
    } catch (e: Exception) {
        e.printStackTrace()
        closure()
    }
    return camera
}