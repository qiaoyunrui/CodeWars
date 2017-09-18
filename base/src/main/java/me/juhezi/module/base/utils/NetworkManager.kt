package me.juhezi.module.base.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Singleton
 * Created by Juhezi[juhezix@163.com] on 2017/9/18.
 */
class NetworkManager private constructor() {
    private var mContext: Context? = null
    private var mConnectivityManager: ConnectivityManager? = null
    private val mNetworkObservable = NetworkObservable()

    private var mNetworkConnected = false
    private var mCurrentNetwork: NetworkInfo? = null
    private var mInitialized = false

    private var mNetworkReceiver: NetworkReceiver? = null

    private object Holder {
        val sInstance = NetworkObservable()
    }

    companion object {
        @JvmStatic
        fun getInstance() = Holder.sInstance
    }

    fun init(context: Context) {
        if (!mInitialized) {
            mContext = context
            mNetworkReceiver = NetworkReceiver()
            mConnectivityManager = mContext!!.getSystemService(
                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            mInitialized = true
        }
        //初始化网络状态
        mCurrentNetwork = mConnectivityManager?.activeNetworkInfo
        mNetworkConnected = ((null != mCurrentNetwork) and
                mCurrentNetwork!!.isConnected)
    }

    private inner class NetworkReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            if (ConnectivityManager.CONNECTIVITY_ACTION != intent.action) {
                return
            }
            val lastNetwork = mCurrentNetwork
            val noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            mNetworkConnected = !noConnectivity
            if (mNetworkConnected) {
                mCurrentNetwork = mConnectivityManager?.activeNetworkInfo
            } else {
                mCurrentNetwork = null
            }
            mNetworkObservable.notifyNetworkChanged(mNetworkConnected, mCurrentNetwork, lastNetwork)
        }
    }

}
