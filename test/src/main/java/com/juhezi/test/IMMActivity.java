package com.juhezi.test;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.btn_save)
    Button mBtnSave;
    @BindView(R.id.btn_edit)
    Button mBtnEdit;
    @BindView(R.id.btn_test)
    Button mBtnTest;

    private boolean mFlag = true;

    @Override
    protected void installViews() {
        setContentView(R.layout.layout_imm);
        ButterKnife.bind(this);
        IMMUtils.work(this, new IMMUtils.Action() {
            @Override
            public void onAction(int size) {
                int keyboardHeight = IMMUtils.getKeyboardHeight(IMMActivity.this);
                Log.i(TAG, "onAction: size is " + size + " " + keyboardHeight);
                mBtnSave.setY(((View) mBtnSave.getParent()).getHeight()
                        - keyboardHeight - mBtnSave.getHeight());
                if (keyboardHeight == 0) {
                    mEtContent.setFocusable(false);
                    mEtContent.setFocusableInTouchMode(false);
                    mBtnSave.setVisibility(View.GONE);
                } else {
                    mEtContent.setFocusable(true);
                    mEtContent.setFocusableInTouchMode(true);
                    mEtContent.requestFocus();
                    mBtnSave.setVisibility(View.VISIBLE);
                }
            }
        });
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        mEtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mFlag) {
                    mEtContent.setFocusable(true);
                    mEtContent.setFocusableInTouchMode(true);
                    mEtContent.requestFocus();
                } else {
                    mEtContent.setFocusableInTouchMode(false);
                    mEtContent.setFocusable(false);
                }*/
                mFlag = !mFlag;
                mEtContent.setText(System.currentTimeMillis() % 10 + "");
                Log.i(TAG, "onClick: " + mEtContent.getText());
            }
        });
    }

    //初始化界面的时候，回调一定会执行
    //修改控件可见性会导致回调执行
    //但是重复设置相同可见性不会调用
    //setFocusable 不会调用
    private void edit() {
        //showKeyboard
        mEtContent.setFocusable(true);
        mEtContent.setFocusableInTouchMode(true);
        mEtContent.requestFocus();
        mBtnSave.setVisibility(View.VISIBLE);
        IMMUtils.showKeyboard(mEtContent);
    }

    private void save() {
        mEtContent.setFocusable(false);
        mEtContent.setFocusableInTouchMode(false);
        mBtnSave.setVisibility(View.GONE);
        IMMUtils.hideKeyboard(mEtContent);
    }

}
