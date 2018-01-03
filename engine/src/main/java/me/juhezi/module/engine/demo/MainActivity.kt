package me.juhezi.module.engine.demo

import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import me.juhezi.module.base.extensions.i
import me.juhezi.module.engine.R
import me.juhezi.module.engine.core.camera.CameraKit
import me.juhezi.module.engine.jni.Knife

class MainActivity : AppCompatActivity(), SurfaceTexture.OnFrameAvailableListener {
    private val mTextureBuilder = TextureBuilder()

    private lateinit var mSurfaceTexture: SurfaceTexture
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)   //设置全屏
        setContentView(R.layout.activity_main)
        sv_show.setRenderer(DRenderer())
        sv_show.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        val cameraKit = CameraKit()
        cameraKit.prepare()
//        cameraKit.holder = sv_show.holder
        mSurfaceTexture = mTextureBuilder.build()
        mSurfaceTexture.setOnFrameAvailableListener(this)
        cameraKit.surfaceTexture = mSurfaceTexture
        cameraKit.startPreview()
        btn_do.text = Knife().stringFromJNI()
        btn_do.setOnClickListener {
            sv_show.requestRender()
        }
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
//        i("Thread: ${Thread.currentThread().name}")
        sv_show.queueEvent {
            i("Thread: ${Thread.currentThread().name}")
            mTextureBuilder.closure.invoke(surfaceTexture)
        }
        sv_show.requestRender()
    }

}
