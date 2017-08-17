package me.juhezi.module.base.utils

import android.util.ArrayMap
import me.juhezi.module.base.router.IRepository

/**
 * Created by max on 2017/8/17.
 */
fun <T, R> arrayMapOf(list: List<Pair<T, R>>): ArrayMap<T, R> {
    val map = ArrayMap<T, R>()
    list.forEach { map.put(it) }
    return map
}

fun <T, R> MutableMap<T, R>.put(pair: Pair<T, R>) {
    put(pair.first, pair.second)
}