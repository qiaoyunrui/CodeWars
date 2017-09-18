package me.juhezi.module.base.utils

import android.net.NetworkInfo

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/18.
 * 被观察者
 */
class NetworkObservable : Observable<NetworkOberver>() {

    fun notifyNetworkChanged(networkConnected: Boolean,
                             currentNetwork: NetworkInfo?,
                             nextNetwork: NetworkInfo?) =
            synchronized(mObservers) {
                mObservers.forEach {
                    it.onNetworkStateChanged(networkConnected,
                            currentNetwork,
                            nextNetwork)
                }
            }

}
