package me.juhezi.module.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

/**
 * 1.0.alpha -> Stop
 * 简单的 TabLayout
 * 有指示器动画
 *
 * Created by Juhezi on 2017/8/21.
 */

public class SimpleTabLayout extends LinearLayout {

    public static final int NO_POSITION = -1;

    public SimpleTabLayout(Context context) {
        this(context, null);
    }

    public SimpleTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public static abstract class TabHolder {
        public final View itemView;
        int mPosition = NO_POSITION;

        public TabHolder(View itemView) {
            if (itemView == null) {
                throw new IllegalArgumentException("itemView can not be null");
            }
            this.itemView = itemView;
        }

    }

}
