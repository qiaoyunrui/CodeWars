package me.juhezi.module.engine.demo

import android.graphics.SurfaceTexture
import android.opengl.GLES20

/**
 * 创建一个 SurfaceTexture
 * Created by Juhezi[juhezix@163.com] on 2017/12/27.
 */
class TextureBuilder {

    private val mTextureId: IntArray = IntArray(1)
    private lateinit var mSurfaceTexture: SurfaceTexture
    private val mTransformMatrix = FloatArray(16)
    val closure = { surfaceTexture: SurfaceTexture? ->
        try {
            /*更新图像流数据到其绑定纹理。
            纹理会默认绑定到 OpenGL Context 的 GL_TEXTURE_EXTERNAL_OES 纹理目标对象中*/
            surfaceTexture?.updateTexImage()
            surfaceTexture?.getTransformMatrix(mTransformMatrix)    //获取图像数据流坐标变换矩阵
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun build(): SurfaceTexture {
        GLES20.glGenTextures(mTextureId.size, mTextureId, 0)    //生成 OpenGL 纹理
        mSurfaceTexture = SurfaceTexture(mTextureId[0]) //创建 SurfaceTexture，SurfaceTexture 接收的数据将传入该纹理
        return mSurfaceTexture
    }

}
