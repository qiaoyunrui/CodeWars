package me.juhezi.module.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 输入法工具类
 * Created by Juhezi[juhezix@163.com] on 2017/10/10.
 */

public class IMMUtils {

    private static final String TAG = "IMMUtils";

    public static void work(Activity activity, final Action action) {
        if (activity == null)
            return;
        final View rootView = activity.findViewById(android.R.id.content);
        rootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect rect = new Rect();
                        rootView.getWindowVisibleDisplayFrame(rect);
                        int screenHeight = rootView.getRootView().getHeight();
                        int heightDifference = screenHeight - (rect.bottom - rect.top);
                        Log.i(TAG, "onGlobalLayout: " + screenHeight + " " + rect.bottom + " " + rect.top);
                        if (action != null) {
                            action.onAction(heightDifference);
                        }
                    }
                });
    }

    public interface Action {
        void onAction(int size);
    }

}
