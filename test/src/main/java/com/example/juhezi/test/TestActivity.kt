package com.example.juhezi.test

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.juhezi.module.router_annotation.annotation.Register

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.extensions.find
import me.juhezi.module.base.extensions.i
import me.juhezi.module.base.widget.GradientTextView

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
class TestActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun installViews() {
        setContentView(R.layout.layout_test)
        showContentView()
        val textView = find(R.id.tv_test) as GradientTextView
        val drawable = resources.getDrawable(R.drawable.gradient_blue)
        if (drawable is GradientDrawable) {
            textView.setGradientDrawable(drawable)
        }
//        textView.setColors(Color.BLUE, Color.GREEN)
    }
}
