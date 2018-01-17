package me.juhezi.ding.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_turn.*
import me.juhezi.ding.R
import me.juhezi.module.base.BaseActivity
import me.juhezi.module.base.BaseViewController
import me.juhezi.module.base.applyClickEffect
import me.juhezi.module.base.createAlphaEffect
import me.juhezi.module.base.extensions.i
import java.io.File

/**
 *
 * Created by Juhezi on 2018/1/3.
 */
class TurnActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn)
        initEvent()
    }

    var index = 0

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
        iv_finger.setOnClickListener {
            val intent = Intent(this, VideoCaptureActivity::class.java)
            startActivity(intent)
        }
        mToolbar?.setOnClickListener {
            index++
            when (index % 5) {
                0 -> showEmpty()
                1 -> showError()
                2 -> showLoading()
                3 -> showNoNetwork()
                4 -> showContent()
            }
        }
    }

    //sample
    /*override fun onLoadEmptyView() {
//        super.onLoadEmptyView()
        mEmptyView = mInflater?.inflate(R.layout.activity_default, null)
        mContainer?.addView(mEmptyView, mLayoutParams)
        mBaseViewController.load(BaseViewController.STATUS_EMPTY, mEmptyView)
    }*/

}