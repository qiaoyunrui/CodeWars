package me.juhezi.module.base.network

import io.reactivex.observers.DisposableObserver

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/24.
 */
class BaseObserver<T> : DisposableObserver<T>() {
    override fun onError(e: Throwable)
            = handleCommonError(e)

    override fun onNext(t: T) {

    }

    override fun onComplete() {}
}