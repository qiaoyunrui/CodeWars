package me.juhezi.codewars.view

import android.annotation.TargetApi
import android.os.Build
import android.widget.Button
import me.juhezi.codewars.R
import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.knife.bindView
import me.juhezi.module.base.router.Repository
import me.juhezi.module.base.router.activity.turn

class MainActivity : BaseActivity() {

    private val mBtnCamera: Button by bindView(R.id.btn_camera)

    @TargetApi(Build.VERSION_CODES.N)
    override fun installViews() {
        setContentView(R.layout.activity_main)
        showContentView()
        Repository.print()
        mBtnCamera.setOnClickListener {
            turn("me.juhezi.module.camera.MainActivity")
        }
    }
}
