package me.juhezi.module.task.tasklist

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.task.R

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/8.
 */
class TaskListActivity : BaseActivity() {

    override fun installViews() {
        setContentView(R.layout.activity_task_list)
        showContentView()
    }
}
