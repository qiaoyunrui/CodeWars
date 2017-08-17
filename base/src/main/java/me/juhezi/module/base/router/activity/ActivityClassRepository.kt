package me.juhezi.module.base.router.activity

import android.app.Activity
import me.juhezi.module.base.router.ClassRepository
import me.juhezi.module.base.view.activity.DefaultActivity

/**
 * 存放 Activity Class 的仓库
 *
 * Created by Juhezi[juhezix@163.com] on 2017/8/12.
 */
class ActivityClassRepository : ClassRepository<Activity>() {

    companion object {
        private val DEFAULT_ACTIVITY_CLASS = DefaultActivity::class.java
    }

    var mDefaultActivityClass: Class<out Activity> = DEFAULT_ACTIVITY_CLASS

    override fun get(key: String) = super.get(key) ?: mDefaultActivityClass

}
