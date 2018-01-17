package me.juhezi.module.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 *
 * Created by Juhezi[juhezix@163.com] on 2017/8/3.
 */
open class BaseActivity : AppCompatActivity() {


    //多种状态的 View，这几个 View 都要从具体的 layout 中加载
    protected var mEmptyView: View? = null
    protected var mErrorView: View? = null
    protected var mLoadingView: View? = null
    protected var mNoNetworkView: View? = null
    protected var mContentView: View? = null

    protected val mLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)

    @LayoutRes private var mEmptyViewResId = R.layout.view_empty_default
    @LayoutRes private var mErrorViewResId = R.layout.view_error_default
    @LayoutRes private var mLoadingViewResId = R.layout.view_loading_default
    @LayoutRes private var mNoNetworkViewResId = R.layout.view_no_network_default
    @LayoutRes private var mRootViewResId = R.layout.activity_base

    private var mRootView: View? = null
    protected var mContainer: FrameLayout? = null
    protected var mToolbar: Toolbar? = null

    protected val mBaseViewController: BaseViewController = BaseViewController()

    protected var mInflater: LayoutInflater? = null

    var toolBarVisibility = true
        set(value) {
            field = value
            mToolbar?.visibility = if (value) View.VISIBLE else View.GONE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInflater = LayoutInflater.from(this)
        initRootView()
        installViews()
    }

    /**
     * 加载根布局
     */
    private fun initRootView() {
        super.setContentView(mRootViewResId)
        mRootView = findViewById(R.id.vg_base_activity_root)
        mContainer = findViewById(R.id.vg_base_activity_container)
        mToolbar = findViewById(R.id.tb_base_activity)
        mToolbar?.visibility = if (toolBarVisibility) View.VISIBLE else View.GONE
        //这里应该将基础 View 中添加到界面中
        if (supportActionBar == null) {
            setSupportActionBar(mToolbar)
        }
        loadBaseViews()
        installViews()
    }

    override fun onStart() {
        super.onStart()
        mBaseViewController.init()
    }

    private fun loadBaseViews() {
        onLoadLoadingView()

        onLoadErrorView()

        onLoadEmptyView()

        onLoadNoNetworkView()

        loadViews()
    }

    override fun setContentView(layoutResID: Int) {
        mContentView = mInflater?.inflate(layoutResID, null)
        mContainer?.addView(mContentView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_CONTENT, mContentView)
    }

    open fun onLoadLoadingView() {
        mLoadingView = mInflater?.inflate(mLoadingViewResId, null)
        mContainer?.addView(mLoadingView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_LOADING, mLoadingView)
    }

    open fun onLoadErrorView() {
        mErrorView = mInflater?.inflate(mErrorViewResId, null)
        mContainer?.addView(mErrorView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_ERROR, mErrorView)
    }

    open fun onLoadEmptyView() {
        mEmptyView = mInflater?.inflate(mEmptyViewResId, null)
        mContainer?.addView(mEmptyView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_EMPTY, mEmptyView)
    }

    open fun onLoadNoNetworkView() {
        mNoNetworkView = mInflater?.inflate(mNoNetworkViewResId, null)
        mContainer?.addView(mNoNetworkView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_NO_NETWORK, mNoNetworkView)
    }

    protected fun showContent() {
        show(BaseViewController.STATUS_CONTENT)
    }

    protected fun showEmpty() {
        show(BaseViewController.STATUS_EMPTY)
    }

    protected fun showError() {
        show(BaseViewController.STATUS_ERROR)
    }

    protected fun showLoading() {
        show(BaseViewController.STATUS_LOADING)
    }

    protected fun showNoNetwork() {
        show(BaseViewController.STATUS_NO_NETWORK)
    }

    protected fun show(status: String) {
        mBaseViewController.show(status, true)
    }

    @Deprecated("Will delete")
    open protected fun installViews() {
    }

    open protected fun loadViews() {
    }

    private fun refresh() {
        onRefresh()
    }

    open protected fun onRefresh() {}

}

