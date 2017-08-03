package me.juhezi.module.base

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import butterknife.Unbinder

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        const val STATUS_CONTENT: Long = 0x00
        const val STATUS_LOADING: Long = 0x01
        const val STATUS_EMPTY: Long = 0x02
        const val STATUS_ERROR: Long = 0x03
        const val STATUS_NO_NETWORK: Long = 0x04
    }

    @IntDef(STATUS_LOADING, STATUS_CONTENT, STATUS_EMPTY, STATUS_ERROR, STATUS_NO_NETWORK)
    annotation class ViewStatus

    @ViewStatus var mStatus = STATUS_EMPTY

    //多种状态的 View
    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null

    @LayoutRes private var mEmptyViewResId = android.R.layout.activity_list_item
    @LayoutRes private var mErrorViewResId = android.R.layout.activity_list_item
    @LayoutRes private var mLoadingViewResId = android.R.layout.activity_list_item
    @LayoutRes private var mNoNetworkViewResId = android.R.layout.activity_list_item
    @LayoutRes private var mRootViewResId = R.layout.activity_base

    private var mRootView: View? = null
    private var mContainer: FrameLayout? = null
    protected var mToolbar: Toolbar? = null

    private val refreshAction = onRefresh()

    private var mInflater: LayoutInflater? = null

    private var mUnBinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInflater = LayoutInflater.from(this)
        initBaseData()
        installViews()

    }

    private fun initBaseData() {}

    protected fun installViews() {}

    protected fun onRefresh() {}

}

