package com.example.juhezi.test;

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

    @Override
    protected void installViews() {
        setContentView(R.layout.layout_imm);
        ButterKnife.bind(this);
        IMMUtils.work(this, new IMMUtils.Action() {
            @Override
            public void onAction(int size) {
                int keyboardHeight = IMMUtils.getKeyboardHeight(IMMActivity.this);
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
    }

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
