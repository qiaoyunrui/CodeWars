package me.juhezi.codewars.view

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import me.juhezi.codewars.R
import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.extensions.i
import me.juhezi.module.base.router.Repository
import me.juhezi.module.base.router.activity.turn

class MainActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showContent()
        Repository.print()
        btn_camera.setOnClickListener {
            i("Hello")
            turn("me.juhezi.module.camera.ui.CameraActivity")
        }
    }

}
