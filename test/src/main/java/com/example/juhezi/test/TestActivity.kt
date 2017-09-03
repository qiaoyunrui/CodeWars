package com.example.juhezi.test

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import butterknife.BindView
import butterknife.ButterKnife

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.extensions.i
import me.juhezi.module.base.knife.bindView
import me.juhezi.module.base.router.Repository
import me.juhezi.module.base.router.activity.turn
import me.juhezi.module.base.router.register
import me.juhezi.module.base.widget.GradientTextView

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
class TestActivity : BaseActivity() {

    private val textView: GradientTextView by bindView(R.id.tv_test)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun installViews() {
        setContentView(R.layout.layout_test)
        showContentView()
        val drawable = resources.getDrawable(R.drawable.gradient_blue)
        if (drawable is GradientDrawable) {
            textView.setGradientDrawable(drawable)
        }
//        textView.setColors(Color.BLUE, Color.GREEN)
        textView.setOnClickListener {
            turn("com.example.juhezi.test.TestActivity")
            /*turn("", {
                putExtra("name", "Juhezi")
                putExtra("age", 22)
            })*/
        }
    }
}
