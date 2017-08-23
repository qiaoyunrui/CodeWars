package com.example.juhezi.test

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import butterknife.BindView
import butterknife.ButterKnife

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.widget.GradientTextView

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
class TestActivity : BaseActivity() {

    lateinit var textView: GradientTextView

    @RequiresApi(Build.VERSION_CODES.N)
    override fun installViews() {
        setContentView(R.layout.layout_test)
        ButterKnife.bind(this)
        showContentView()
        val drawable = resources.getDrawable(R.drawable.gradient_blue)
        if (drawable is GradientDrawable) {
            textView.setGradientDrawable(drawable)
        }
//        textView.setColors(Color.BLUE, Color.GREEN)
    }
}
