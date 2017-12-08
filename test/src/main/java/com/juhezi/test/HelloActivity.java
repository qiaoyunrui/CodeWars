package com.juhezi.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.juhezi.module.router_annotation.annotation.Register;

import me.juhezi.module.base.BaseActivity;
import me.juhezi.module.base.utils.BlurUtils;

import static me.juhezi.module.base.functions.FunctionAKt.runInUIThread;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/3.
 */
@Register
public class HelloActivity extends BaseActivity {

    private static final String TAG = "HelloActivity";
    ImageView mIvBackground;

    @Override
    protected void installViews() {
        setContentView(R.layout.layout_hello);
        showContentView();
        mIvBackground = findViewById(R.id.iv_background);
        SubThread thread = new SubThread(mIvBackground);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class SubThread extends Thread {

        private ImageView mImageView;

        public SubThread(ImageView imageView) {
            this.mImageView = imageView;
        }

        @Override
        public void run() {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
            final Bitmap newBitmap = BlurUtils.blur(bitmap, 50);
            runInUIThread(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(newBitmap);
                }
            });
        }
    }

}
