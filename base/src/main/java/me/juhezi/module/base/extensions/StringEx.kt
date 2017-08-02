package me.juhezi.module.base.extensions

import android.graphics.Paint
import android.graphics.Rect

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */

fun String.getTextWidth(paint: Paint): Float {
    return paint.measureText(this)
}

fun String.getTextHeight(paint: Paint): Float {
    var bounds = Rect()
    paint.getTextBounds(this, 0, length, bounds)
    return bounds.height().toFloat()
}
