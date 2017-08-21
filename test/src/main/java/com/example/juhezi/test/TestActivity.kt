package com.example.juhezi.test

import com.juhezi.module.router_annotation.annotation.Register

import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.widget.SimpleTabLayout

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
@Register
class TestActivity : BaseActivity() {
    override fun installViews() {
        setContentView(R.layout.layout_test_x)
        val simpleTablayout: SimpleTabLayout = findViewById(R.id.stl_tab_layout) as SimpleTabLayout

    }
}