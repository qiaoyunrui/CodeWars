package me.juhezi.module.base.router

import android.app.Activity
import me.juhezi.module.base.router.activity.ActivityClassRepository
import me.juhezi.module.base.router.activity.respository
import me.juhezi.module.base.router.service.ServiceClassRepository
import me.juhezi.module.base.router.web.WebRepository
import me.juhezi.module.base.utils.arrayMapOf

/**
 * 总仓库
 *
 * Created by max on 2017/8/17.
 */
const val ACTIVITY = 0x01
const val SERVICE = 0x02
const val BROADCAST_RECEIVE = 0x03
const val CONTENT_PROVIDER = 0x04
const val WEB = 0x05

class Repository {

    companion object {
        val map = arrayMapOf(listOf(Pair(ACTIVITY, ActivityClassRepository()),
                Pair(SERVICE, ServiceClassRepository()),
                Pair(WEB, WebRepository())))

        fun <T : Any> register(type: Int, key: String, value: T) {
            val repository = map[type] ?: return
        }

    }

}
