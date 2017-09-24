package me.juhezi.module.base.network

import android.text.TextUtils
import me.juhezi.module.base.functions.logd
import me.juhezi.module.base.utils.showShortToast
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/24.
 */
fun handleCommonError(e: Throwable) {
    e.printStackTrace()
    if (e is HttpException) {
        logd("服务暂不可用")
    } else if (e is IOException) {
        logd("连接失败")
    } else if (e is ApiException) {
        when (e.status) {
            "ignore" -> return
        //Other operate
        }
        if (!TextUtils.isEmpty(e.message)) {
            showShortToast(e.message!!)
        }
    } else {
        logd("未知错误")
    }
}