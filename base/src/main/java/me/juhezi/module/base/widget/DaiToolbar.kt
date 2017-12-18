package me.juhezi.module.base.widget

import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/18.
 */

class DaiToolbar : Toolbar {

    private var mInflater: LayoutInflater? = null
    private

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) :
            super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr)

}
