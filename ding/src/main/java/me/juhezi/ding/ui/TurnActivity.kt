package me.juhezi.ding.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.airbnb.lottie.LottieComposition
import kotlinx.android.synthetic.main.activity_turn.*
import me.juhezi.ding.R
import me.juhezi.module.base.applyClickEffect
import me.juhezi.module.base.createAlphaEffect
import me.juhezi.module.base.createScaleEffect
import me.juhezi.module.base.extensions.i
import java.io.File

/**
 *
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
            //调用系统相机拍摄
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            val file = File("${Environment.getExternalStorageDirectory()}/123.mp4")
            i(file.path)
            val uri = Uri.fromFile(file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivity(intent)
        }
        btn_pick_from_photo_album.setOnClickListener {
            //从相册选择
        }
//        lav_anim.setAnimation("anim.json")
        lav_anim.imageAssetsFolder = "images"
        fab_add.setOnClickListener {
            lav_anim.playAnimation()
        }
        applyClickEffect(iv_finger, createAlphaEffect(0.7f))
        iv_finger.setOnClickListener {}
    }

}