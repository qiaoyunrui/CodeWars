package me.juhezi.ding.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.juhezi.ding.R;


/**
 * 底部操作栏
 * Layout: view_bottom_container
 * Created by Juhezi[juhezix@163.com] on 2017/9/22.
 */
public class BottomContainer {

    private static final String TAG = "BottomContainer";

    private Action mAction;
    private View mRootView;
    private ViewGroup mButtonWrapper;    //LinearLayout
    private SelectHolder[] mHolders;    //底部操作按钮
    private Adapter mAdapter;

    public BottomContainer(View rootView, Adapter adapter) {
        mRootView = rootView;
        if (mRootView == null) {
            throw new NullPointerException("The root view can not be null!");
        }
        mButtonWrapper = mRootView.findViewById(R.id.vg_bottom_wrapper);
        if (mButtonWrapper == null) {
            throw new NullPointerException("The wrapper view can not be null!");
        }
        mAdapter = adapter;
        if (mAdapter == null) {
            throw new NullPointerException("The adapter can not be null!");
        }
        int size = mAdapter.getItemCount(mButtonWrapper.getChildCount());
        mHolders = new SelectHolder[size];
        SelectHolder tempHolder;
        for (int i = 0; i < size; i++) {
            if (i < mButtonWrapper.getChildCount()) {
                tempHolder = mAdapter.onCreateView(mButtonWrapper.getChildAt(i), i);
            } else {
                tempHolder = mAdapter.onCreateView(null, i);
            }
            if (tempHolder == null) {
                throw new NullPointerException("The selectHolder can not be null!");
            }
            mHolders[i] = tempHolder;
            mAdapter.onBindHolder(mHolders[i], i);
            mHolders[i].init(false);    //初始化为未点击状态
            final int tempIndex = i;

            //将点击事件暴露到外部
            mHolders[i].setOnClickListener(new Action1() {
                @Override
                public void onAction(boolean isSelect) {
                    if (isSelect) {     //选中某个按钮，isSelect 为 false ?
                        unSelectOther(tempIndex);
                    }
                    if (mAction != null) {
                        mAction.onAction(tempIndex, isSelect);
                    }
                }
            });
        }
    }

    /**
     * 点击按钮，显示点击状态
     *
     * @param position
     */
    public void select(int position) {
        mHolders[position].select();
    }

    /**
     * 取消点击状态
     *
     * @param position
     */
    public void unSelect(int position) {
        mHolders[position].unSelect();
    }

    public void unSelectOther(int position) {
        for (int i = 0; i < mHolders.length; i++) {
            if (i != position) {
                mHolders[i].unSelect();
            }
        }
    }

    /**
     * 是否有被选中的选项
     *
     * @return
     */
    public boolean isSelect() {
        for (SelectHolder holder : mHolders) {
            if (holder.isSelect()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 全部取消点击状态
     */
    public void unSelectAll() {
        for (SelectHolder holder : mHolders) {
            holder.unSelect();
        }
    }

    public void setOnClickListener(Action action) {
        this.mAction = action;
    }

    public static class SelectHolder {

        private View mView;
        private boolean isSelect;
        private Context mContext;
        @DrawableRes
        int mNormalIcon = -1;  //未选中时的图标
        @DrawableRes
        int mSelectIcon = -1;  //选中时的图标
        private Action1 mAction;

        public SelectHolder(View view) {
            mView = view;
            if (mView == null) {
                throw new NullPointerException("The view can not be null!");
            }
            mContext = mView.getContext();
            isSelect = false;   //初始化未选中
        }

        public SelectHolder setNormalIcon(int normalIcon) {
            this.mNormalIcon = normalIcon;
            return this;
        }

        public SelectHolder setSelectIcon(int selectIcon) {
            this.mSelectIcon = selectIcon;
            return this;
        }

        private void setOnClickListener(Action1 action) {
            this.mAction = action;
        }

        private boolean isSelect() {
            return isSelect;
        }

        private void init(final boolean isSelect) {
            if (mSelectIcon != -1 && mView instanceof ImageView) {
                if (isSelect) {
                    ((ImageView) mView).setImageDrawable(mContext.getResources().getDrawable(mSelectIcon));
                } else {
                    ((ImageView) mView).setImageDrawable(mContext.getResources().getDrawable(mNormalIcon));
                }
            }
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelect()) {
                        unSelect();
                    } else {
                        select();   //这里将 isSelect = true
                    }
                    if (mAction != null) {
                        mAction.onAction(isSelect());
                    }
                }
            });
        }

        private void select() {
            if (!isSelect) {
                if (mSelectIcon != -1 && mView instanceof ImageView) {
                    ((ImageView) mView).setImageDrawable(mContext.getResources().getDrawable(mSelectIcon));
                }
                isSelect = true;
            }
        }

        private void unSelect() {
            if (isSelect) {
                if (mSelectIcon != -1 && mView instanceof ImageView) {
                    ((ImageView) mView).setImageDrawable(mContext.getResources().getDrawable(mNormalIcon));
                }
                isSelect = false;
            }
        }

    }

    /**
     * 提供数据
     */
    public static abstract class Adapter {

        /**
         * 创建 SelectHolder
         *
         * @param view     不为空则代表该 View 是从 XML 上加载的，如果为空，则代表需要手动创建一个 View，如果没有重写 getItemCount 方法，则 View 一定不为空
         * @param position
         * @return
         */
        public abstract SelectHolder onCreateView(View view, int position);

        /**
         * 设置图标资源
         *
         * @param holder
         * @param position
         */
        public abstract void onBindHolder(SelectHolder holder, int position);

        /**
         * @param attrsSize 布局文件中所写的子 View 个数
         * @return 要展示的子 View 个数
         */
        public int getItemCount(int attrsSize) {
            return attrsSize;
        }

    }

    public interface Action {
        void onAction(int position, boolean isSelect);
    }

    public interface Action1 {
        void onAction(boolean isSelect);
    }

}
