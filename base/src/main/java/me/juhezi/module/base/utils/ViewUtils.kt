package me.juhezi.module.base.utils

import android.content.res.Resources

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
class ViewUtils {
    companion object {
        fun getStatusbarHeight() =
                Resources.getSystem()
                        .getDimensionPixelSize(
                                Resources
                                        .getSystem()
                                        .getIdentifier("status_bar_height", "dimen", "android"))
    }
}
