package me.juhezi.module.base.extensions

import android.graphics.Paint
import android.graphics.Rect
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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

/**
 * 对字符串进行 MD5 加密
 */
fun String.MD5Code(): String {
    try {
        val instance = MessageDigest.getInstance("MD5")
        val digest = instance.digest(toByteArray())
        val sb = StringBuilder()
        for (b in digest) {
            var i = b.toInt() and 0xff  //获取低八位有效值
            var hexString = Integer.toHexString(i)  //将整数转换为 16 进制
            if (hexString.length < 2) {
                hexString = "0" + hexString //如果只有一位的话，补 0
            }
            sb.append(hexString)
        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        return hashCode().toString()
    }
}