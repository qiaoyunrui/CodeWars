package me.juhezi.ding.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_video_edit.*
import me.juhezi.ding.R
import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.engine.edit.VideoEditKit
import me.juhezi.module.base.extensions.isValidPath
import me.juhezi.module.base.logi

/**
 * 视频编辑页
 */
class VideoEditActivity : BaseActivity() {

    private lateinit var videoEditKit: VideoEditKit

    private var videoPath: String = "storage/emulated/0/video.mp4"  //视频地址

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_edit)
        showContent()
        videoEditKit = VideoEditKit(this)
        logi("文件是否存在？ ${videoPath.isValidPath()}")
        videoEditKit.init(videoPath, tv_video_edit)
    }

    override fun onResume() {
        super.onResume()
        videoEditKit.play()
    }

    override fun onPause() {
        super.onPause()
        videoEditKit.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoEditKit.destroy()
    }

}
