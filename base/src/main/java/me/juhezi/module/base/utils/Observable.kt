package me.juhezi.module.base.utils

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/18.
 */
abstract class Observable<T> {
    val mObservers = ArrayList<T>()

    fun registerObserver(observer: T) {
        synchronized(mObservers) {
            if (mObservers.contains(observer)) {
                throw IllegalStateException("Observer $observer has already registered!!")
            }
            mObservers.add(observer)
        }
    }

    fun unregisterObserver(observer: T) {
        synchronized(mObservers) {
            val index = mObservers.indexOf(observer)
            if (index == -1) {
                throw IllegalStateException("Observer $observer has not registered!!")
            }
            mObservers.removeAt(index)
        }
    }

    fun unregisterAll() {
        synchronized(mObservers) {
            mObservers.clear()
        }
    }

    fun contiansObserver(observer: T) = mObservers.contains(observer)

    fun size() = synchronized(mObservers) {
        mObservers.size
    }

    fun isEmpty() = synchronized(mObservers) {
        mObservers.isEmpty()
    }

}