package me.juhezi.ding.ui

import android.os.Bundle
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_test.*
import me.juhezi.ding.R
import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.extensions.i
import me.juhezi.module.base.logi
import me.juhezi.module.base.widget.refactor.TouchViewGroup

/**
 * Created by Juhezi on 2018/2/6.
 */
class TestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        showContent()
        vg_test_root.setOnEventListener(object : TouchViewGroup.OnEventListener {
            override fun onClick(event: MotionEvent?) {
                logi("点击")
            }

            override fun onLongClick() {
                //搞定
                logi("长按")
            }

            override fun onDoubleClick() {
                //搞定
                logi("双击")
            }

            override fun onScale(scale: Float) {
//                搞定
                logi("缩放：$scale")
            }

            override fun onSlide(x: Float, y: Float, fling: Boolean) {
                logi("x: $x \ny: $y \nfling? : $fling")
            }

        })
    }

}