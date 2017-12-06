package com.juhezi.test

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Button
import com.example.juhezi.test.R

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.knife.bindView
import me.juhezi.module.base.router.Repository
import me.juhezi.module.base.router.activity.turn
import me.juhezi.module.base.widget.GradientTextView

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
class TestActivity : BaseActivity() {

    private val textView: GradientTextView by bindView(R.id.tv_test)
    private val button: Button by bindView(R.id.btn_turn)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun installViews() {
        setContentView(R.layout.layout_test)
        showContentView()
        Repository.print()
        val drawable = resources.getDrawable(R.drawable.gradient_blue)
        if (drawable is GradientDrawable) {
            textView.setGradientDrawable(drawable)
        }
//        textView.setColors(Color.BLUE, Color.GREEN)
        textView.setOnClickListener {
            turn("com.example.juhezi.test.HelloActivity")
            /*turn("", {
                putExtra("name", "Juhezi")
                putExtra("age", 22)
            })*/
        }
        button.setOnClickListener {
            turn("com.example.juhezi.test.IMMActivity")
        }
    }
}
