package me.juhezi.module.engine.core.camera

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.view.SurfaceHolder
import me.juhezi.module.base.extensions.e

/**
 * 对 Camera 的所有操作
 * Created by Juhezi[juhezix@163.com] on 2017/12/12.
 */
class CameraKit {

    companion object {
        private val TAG = "CameraKit"
        private val LOG = true //是否打印日志
        private val L = me.juhezi.module.base.log(TAG, LOG)

        private val DEFAULT_CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT

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

        /**
         * 获取显示角度
         *
         * @param cameraId
         * @return
         */
        fun getDisplayDegree(cameraId: Int): Int {
            var degree: Int
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(cameraId, cameraInfo)
            degree = cameraInfo.orientation % 360
            if (cameraInfo.facing == 1) {
                degree = (360 - degree) % 360
            }
            if (degree == 0) {
                degree = 180
            } else if (degree == 180) {
                degree = 0
            }
            return degree
        }

    }

    private var mCamera: Camera? = null
    var holder: SurfaceHolder? = null
        set(value) {
            field = value
            field?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
            field?.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder?) {
                    L("SurfaceCreated")
                    // set a specific size for the camera preview here
                    field = holder!!
                    try {
                        mCamera?.setPreviewDisplay(field)
                        mCamera?.startPreview()
                    } catch (e: Exception) {
                    }
                }

                override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                    if (field?.surface == null) {
                        return
                    }
                    try {
                        mCamera?.stopPreview()
                    } catch (e: Exception) {
                    }
                    try {
                        mCamera?.setPreviewDisplay(field)
                        mCamera?.startPreview()
                    } catch (ex: Exception) {
                        e("Error starting camera preview. ${ex.message}")
                    }
                }

                override fun surfaceDestroyed(holder: SurfaceHolder?) {
                    release()
                }

            })
        }

    var surfaceTexture: SurfaceTexture? = null
        set(value) {
            field = value
            mCamera?.setPreviewTexture(field)
        }

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var mCurrentCameraId = DEFAULT_CAMERA_ID

    //--------------------------- New ------------------------
    fun prepare(width: Int = -1, height: Int = -1) {
        if (mCamera != null)
            throw RuntimeException("camera already initialized")
        mCamera = getCameraInstance()
        if (mCamera == null) {
            throw RuntimeException("Unable to open camera")
        }
        val params = mCamera!!.parameters
        val size = choosePreviewSize(params, Pair(width, height))
        mCamera!!.parameters = params
        L("Camera preview size is ${size.first} x ${size.second}")
        mWidth = size.first
        mHeight = size.second
        val degree = getDisplayDegree(mCurrentCameraId)
        mCamera!!.setDisplayOrientation(degree)
    }

    fun startPreview() {
        mCamera?.startPreview()
    }

    fun stopPreview() {
        mCamera?.stopPreview()
    }

    fun release() {
        L("releasing camera")
        mCamera?.stopPreview()
        mCamera?.release()
        mCamera = null
    }

}