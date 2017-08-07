package me.juhezi.module.base

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.Unbinder

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        const val STATUS_CONTENT = 0x00
        const val STATUS_LOADING = 0x01
        const val STATUS_EMPTY = 0x02
        const val STATUS_ERROR = 0x03
        const val STATUS_NO_NETWORK = 0x04
    }

    var mStatus = STATUS_EMPTY

    //多种状态的 View
    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null

    private val mLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)

    @LayoutRes private var mEmptyViewResId = R.layout.view_empty_default
    @LayoutRes private var mErrorViewResId = R.layout.view_error_default
    @LayoutRes private var mLoadingViewResId = R.layout.view_loading_default
    @LayoutRes private var mNoNetworkViewResId = R.layout.view_no_network_default
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

    private fun initBaseData() {
        super.setContentView(mRootViewResId)
        mRootView = findViewById(R.id.vg_base_activity_root)
        mContainer = findViewById(R.id.vg_base_activity_container) as FrameLayout?
        mToolbar = findViewById(R.id.tb_base_activity) as Toolbar?
    }

    override fun setContentView(layoutResID: Int) {
        mContentView = mInflater?.inflate(layoutResID, null)
        mContainer?.addView(mContentView, 0, mLayoutParams)
    }

    protected fun installViews() {}

    protected fun onRefresh() {}

    protected fun showContentView() {
        mStatus = STATUS_CONTENT
        showView(mStatus)
    }

    protected fun showLoading(layoutResID: Int = mLoadingViewResId, action: ((View?) -> Unit)? = null) {
        mStatus = STATUS_LOADING
        //Load View
        action?.invoke(mLoadingView)
        showView(mStatus)
    }

    protected fun showError(layoutResID: Int = mErrorViewResId, action: ((View?) -> Unit)? = null) {
        mStatus = STATUS_ERROR
        action?.invoke(mErrorView)
        showView(mStatus)
    }

    protected fun showNoNetWork(layoutResID: Int = mNoNetworkViewResId, action: ((View?) -> Unit)? = null) {
        mStatus = STATUS_NO_NETWORK
        action?.invoke(mNoNetworkView)
        showView(mStatus)
    }

    protected fun showEmpty(layoutResID: Int = mEmptyViewResId, action: ((View?) -> Unit)? = null) {
        mStatus = STATUS_EMPTY
        action?.invoke(mEmptyView)
        showView(mStatus)
    }

    private fun showView(viewStatus: Int) {
        when (viewStatus) {
            STATUS_CONTENT -> mContentView?.visibility = View.VISIBLE
            STATUS_EMPTY -> mEmptyView?.visibility = View.VISIBLE
            STATUS_ERROR -> mErrorView?.visibility = View.VISIBLE
            STATUS_NO_NETWORK -> mErrorView?.visibility = View.VISIBLE
            STATUS_LOADING -> mLoadingView?.visibility = View.VISIBLE
            else -> mEmptyView?.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mContentView = null
        mEmptyView = null
        mErrorView = null
        mLoadingView = null
        mNoNetworkView = null
        mUnBinder?.unbind()
    }

}

