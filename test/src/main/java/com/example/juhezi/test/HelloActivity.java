package com.example.juhezi.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.juhezi.module.router_annotation.annotation.Register;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.juhezi.module.base.BaseActivity;
import me.juhezi.module.base.utils.BlurUtils;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/3.
 */
@Register
public class HelloActivity extends BaseActivity {

    @BindView(R.id.iv_background)
    ImageView mIvBackground;

    private Unbinder mUnbinder;

    @Override
    protected void installViews() {
        setContentView(R.layout.layout_hello);
        showContentView();
        mUnbinder = ButterKnife.bind(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        bitmap = BlurUtils.blur(bitmap, 50);
//        Log.i("TAG", "installViews: " + mIvBackground);
        mIvBackground.setImageBitmap(bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    class SubThread extends Thread {

        private Bitmap mBitmap;

        public SubThread(Bitmap bitmap) {
            this.mBitmap = bitmap;
        }

        @Override
        public void run() {
            super.run();
        }
    }

}
