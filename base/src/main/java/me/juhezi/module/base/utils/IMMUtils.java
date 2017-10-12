package me.juhezi.module.base.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
                        if (action != null) {
                            action.onAction(heightDifference);
                        }
                    }
                });
    }

    public interface Action {
        void onAction(int size);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getKeyboardHeight(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        Class immClazz = InputMethodManager.class;
        try {
            Method method = immClazz.getMethod("getInputMethodWindowVisibleHeight");
            return (int) method.invoke(imm);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
