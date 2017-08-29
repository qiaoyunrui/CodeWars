package me.juhezi.module.base.view.activity

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.R
import me.juhezi.module.base.extensions.i

/**
 * Router 找不到 Activity 的时候，显示这个默认的 DefaultActivty
 * Created by Juhezi[juhezix@163.com] on 2017/8/12.
 */
class DefaultActivity : BaseActivity() {

    override fun installViews() {
        setContentView(R.layout.activity_default)
    }

}