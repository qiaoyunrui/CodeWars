package me.juhezi.ding

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_video_edit.*
import me.juhezi.ding.view.BottomContainer

/**
 * 视频编辑页
 */
class VideoEditActivity : AppCompatActivity() {

    private lateinit var bottomContainer: BottomContainer

    private var bottomIcons = listOf(
            R.drawable.bottom_music_unselect,
            R.drawable.bottom_music_select,
            R.drawable.bottom_filter_unselect,
            R.drawable.bottom_filter_select,
            R.drawable.bottom_title_unselect,
            R.drawable.bottom_title_select)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_edit)
        initView()
    }

    private fun initView() {
        bottomContainer = BottomContainer(vg_bottom_container, object : BottomContainer.Adapter() {
            override fun onCreateView(view: View?, position: Int) =
                    BottomContainer.SelectHolder(view)

            override fun onBindHolder(holder: BottomContainer.SelectHolder?, position: Int) {
                holder?.setNormalIcon(bottomIcons[position * 2])
                holder?.setSelectIcon(bottomIcons[position * 2 + 1])
            }
        })
    }
}
