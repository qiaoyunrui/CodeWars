package me.juhezi.module.base

import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.View

/**
 * 此 Activity 可以看作是 Fragment 的 容器
 * Created by Juhezi[juhezix@163.com] on 2017/9/1.
 */
abstract class SingleFragmentActivity : BaseActivity() {

    override fun installViews() {
        mToolbar?.visibility = View.GONE
        setContentView(getActivityLayoutRes())
        var fragment = supportFragmentManager.findFragmentById(getFragmentContainerId())
        if (fragment == null)
            fragment = getFragment()
        supportFragmentManager.beginTransaction()
                .add(getFragmentContainerId(), fragment)
                .commit()
    }

    abstract fun getFragment(): Fragment

    @LayoutRes
    open fun getActivityLayoutRes() = R.layout.activity_single_fragment

    @LayoutRes
    open fun getFragmentContainerId() = R.id.vg_act_single_frag

}