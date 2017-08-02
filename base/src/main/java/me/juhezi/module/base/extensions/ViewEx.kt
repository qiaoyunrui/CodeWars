package me.juhezi.module.base.extensions

import android.view.View

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */

fun View.getWidthx() =
        if (width > 0) {
            width
        } else if (layoutParams != null && layoutParams.width > 0) {
            layoutParams.width
        } else {
            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            measuredWidth
        }

fun View.getHeightx() =
        if (height > 0) {
            height
        } else if (layoutParams != null && layoutParams.height > 0) {
            layoutParams.height
        } else {
            measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            measuredHeight
        }
