package com.example.juhezi.test;

import android.util.Log;
import android.view.View;

import com.juhezi.module.router_annotation.annotation.Register;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.juhezi.module.base.BaseActivity;
import me.juhezi.module.base.utils.IMMUtils;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/10.
 */
@Register
public class IMMActivity extends BaseActivity {

    private static final String TAG = "IMMActivity";

    @BindView(R.id.view)
    View view;

    @Override
    protected void installViews() {
        setContentView(R.layout.layout_imm);
        ButterKnife.bind(this);
        IMMUtils.work(this, new IMMUtils.Action() {
            @Override
            public void onAction(int size) {
                Log.i(TAG, "onAction: " + size);
                view.setY(size - view.getHeight() / 2);
            }
        });
    }
}
