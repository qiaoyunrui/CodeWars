package me.juhezi.module.base.camera

import android.hardware.Camera
import android.view.SurfaceHolder
import me.juhezi.module.base.extensions.e

/**
 * 对 Camera 的所有操作
 * Created by Juhezi[juhezix@163.com] on 2017/12/12.
 */
class CameraKit(var mHolder: SurfaceHolder) {

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

}
