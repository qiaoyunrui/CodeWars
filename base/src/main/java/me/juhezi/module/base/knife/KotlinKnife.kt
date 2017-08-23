package me.juhezi.module.base.knife

import android.app.Activity
import android.view.View
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/24.
 */

public fun <V : View> View.bindView(id: Int)
        : ReadOnlyProperty<View, V> = required(id, viewFinder)

public fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> = required(id, viewFinder)

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?)
        = Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
        throw IllegalStateException("View ID $id for '${desc.name}' not found.")

private val View.viewFinder: View.(Int) -> View?
    get() = { findViewById(it) }
private val Activity.viewFinder: Activity.(Int) -> View?
    get() = { findViewById(it) }

private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}