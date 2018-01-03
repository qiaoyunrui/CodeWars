package me.juhezi.ding.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn.*
import me.juhezi.ding.R

/**
 * Created by Juhezi on 2018/1/3.
 */
class TurnActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn)
        initEvent()
    }

    fun initEvent() {
        btn_capture.setOnClickListener {
            //调用系统拍摄界面
            var intent = Intent(Intent.ACTION_VIEW)
            startActivity(intent)
        }
        btn_pick_from_photo_album.setOnClickListener {
            //打开相机录像
        }
    }

}