package me.juhezi.module.base

import me.juhezi.module.base.framework.IPresenter
import me.juhezi.module.base.framework.IView

/**
 * 还有待修改
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
open class BasePresenter<T : IView> : IPresenter<T> {

    var mView: T? = null
        private set

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView(View: T) {
        mView = null
    }
}