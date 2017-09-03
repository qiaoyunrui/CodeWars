package com.example.juhezi.test;

import com.juhezi.module.router_annotation.annotation.Register;

import me.juhezi.module.base.BaseActivity;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/3.
 */
@Register
public class HelloActivity extends BaseActivity {

    @Override
    protected void installViews() {
        setContentView(android.R.layout.activity_list_item);
        showContentView();
    }
}
