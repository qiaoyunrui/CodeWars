package me.juhezi.module.base.framework

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
interface IPresenter<in V : IView> {
    fun attachView(view: V)

    fun detachView(View: V)
}