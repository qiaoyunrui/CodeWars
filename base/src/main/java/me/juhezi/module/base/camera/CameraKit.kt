package me.juhezi.module.base.camera

import android.hardware.Camera
import android.view.SurfaceHolder
import me.juhezi.module.base.extensions.e

/**
 * 对 Camera 的所有操作
 * Created by Juhezi[juhezix@163.com] on 2017/12/12.
 */
class CameraKit(var mHolder: SurfaceHolder) {

    companion object {
        private val TAG = "CameraKit"
        private val LOG = false //是否打印日志
        private val L = me.juhezi.module.base.log(TAG, LOG)

        private val DEFAULT_CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK

        //---  functions ---

        fun getCameraInstance(cameraId: Int = DEFAULT_CAMERA_ID): Camera? {
            var camera: Camera? = null
            try {
                camera = Camera.open(cameraId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return camera
        }

        /**
         * 根据提供的尺寸，选取预览尺寸
         */
        @JvmStatic
        fun choosePreviewSize(params: Camera.Parameters, size: Pair<Int, Int>? = null): Pair<Int, Int> {
            val ppsfv = params.preferredPreviewSizeForVideo
            fun setAndReturnPreferredSize(): Pair<Int, Int> {
                return if (ppsfv != null) {
                    params.setPreviewSize(ppsfv.width, ppsfv.height)
                    Pair(ppsfv.width, ppsfv.height)
                } else {
                    Pair(0, 0)
                }
            }
            if (ppsfv != null)
                L("Camera preferred preview size for video is ${ppsfv.width} x ${ppsfv.height}")

            if (size == null) {
                return setAndReturnPreferredSize()
            }
            params.supportedPreviewSizes.forEach {
                if (it.width == size.first && it.height == size.second) {
                    params.setPreviewSize(size.first, size.second)
                    return size
                }
            }
            L("Unable to set preview to ${size.first} x ${size.second}")
            return setAndReturnPreferredSize()
        }

    }

    private var mCamera: Camera? = null

    init {
        mCamera = getCameraInstance {
            e("get Camera Instance Error")
        }
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        mHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                // set a specific size for the camera preview here
                try {
                    mCamera?.setPreviewDisplay(mHolder)
                    mCamera?.startPreview()
                } catch (e: Exception) {
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                if (mHolder.surface == null) {
                    return
                }
                try {
                    mCamera?.stopPreview()
                } catch (e: Exception) {
                }
                try {
                    mCamera?.setPreviewDisplay(mHolder)
                    mCamera?.startPreview()
                } catch (ex: Exception) {
                    e("Error starting camera preview. ${ex.message}")
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                mCamera?.release()
            }

        })
    }

    //--------------------------- New ------------------------
    fun prepare(width: Int, height: Int) {
        if (mCamera != null)
            throw RuntimeException("camera already initialized")
        val info = Camera.CameraInfo()
        mCamera = getCameraInstance()
        if (mCamera == null) {
            throw RuntimeException("Unable to open camera")
        }
        val params = mCamera!!.parameters
        val size = choosePreviewSize(params, Pair(width, height))
        mCamera!!.parameters = params
        L("Camera preview size is ${size.first} x ${size.second}")
    }

    fun release() {
        L("releasing camera")
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

}
