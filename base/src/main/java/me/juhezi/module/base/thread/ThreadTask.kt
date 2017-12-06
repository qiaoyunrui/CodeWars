package com.yixia.weiboeditor.utils

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

abstract class ThreadTask {

    private val mHandlerThread: HandlerThread = HandlerThread("ThreadTask", android.os.Process.THREAD_PRIORITY_BACKGROUND)
    private val mHandler: TaskHandler
    private val mUiHandler: TaskHandler
    private var mParams: Map<String, Any>? = null
    @get:Synchronized
    var isRunning = true
        private set
    private var mStartTime: Long = 0
    private var mEndTime: Long = 0

    val isCancelled: Boolean
        get() = !isRunning

    //获取doInBackground的执行时间 毫秒
    val executeTime: Long
        get() = mEndTime - mStartTime

    init {
        //降低线程的优先级
        mHandlerThread.start()
        mHandler = TaskHandler(mHandlerThread.looper)
        mUiHandler = TaskHandler(Looper.getMainLooper())
    }

    protected abstract fun doInBackground(params: Map<String, Any>?): Any

    protected fun onPreExecute() = Unit

    @Synchronized protected fun onProgressUpdate(grogresses: Map<String, Any>?) = Unit

    @Synchronized protected fun onPostExecute(result: Any) = Unit

    protected fun publishProgress(progresses: Map<String, Any>) {
        mUiHandler.obtainMessage(MESSAGE_PROGRESS, progresses).sendToTarget()
    }


    fun cancel() {
        isRunning = false
        mHandlerThread.quit()
    }

    fun execute(params: Map<String, Any>?) {
        isRunning = true
        mParams = params
        onPreExecute()
        mHandler.sendEmptyMessage(MESSAGE_IN_BACKGROUND)
    }

    private inner class TaskHandler(looper: Looper) : Handler(looper) {

        @SuppressWarnings("unchecked")
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_IN_BACKGROUND -> {
                    mStartTime = System.currentTimeMillis()
                    mUiHandler.obtainMessage(MESSAGE_POST_EXECUTE, doInBackground(mParams)).sendToTarget()
                }
                MESSAGE_POST_EXECUTE -> {
                    mEndTime = System.currentTimeMillis()
                    isRunning = false
                    onPostExecute(msg.obj)
                    mHandlerThread.quit()
                }
                MESSAGE_PROGRESS -> {
                    if (msg.obj is Map<*, *>) {
                        onProgressUpdate(msg.obj as Map<String, Any>?)
                    } else {
                        onProgressUpdate(null)
                    }
                }
            }
        }
    }

    companion object {

        private val MESSAGE_IN_BACKGROUND = 0
        private val MESSAGE_POST_EXECUTE = 1
        private val MESSAGE_PROGRESS = 2
    }
}
