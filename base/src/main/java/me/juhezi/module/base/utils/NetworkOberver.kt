package me.juhezi.module.base.utils

import android.net.NetworkInfo

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/18.
 * 观察者
 */
abstract class NetworkOberver {

    /**
     * 网络连接状态发生改变
     */
    abstract fun onNetworkStateChanged(networkConnected: Boolean,
                                       currentNetwork: NetworkInfo?,
                                       nextNetwork: NetworkInfo?)

}
