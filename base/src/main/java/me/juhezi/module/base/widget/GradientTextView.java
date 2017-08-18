package me.juhezi.module.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;


/**
 * 只能居中显示
 * 不要设置 gravity 属性
 * Created by Juhezi[juhezix@163.com] on 2017/8/18.
 */

public class GradientTextView extends android.support.v7.widget.AppCompatTextView {

    private static final String TAG = "GradientTextView";

    private Shader mShader;
    private Rect mRect = new Rect();
    private Paint mPaint;
    private String mContent;
    private GradientDrawable mDrawable;
    @ColorInt
    private int mStartColor;
    @ColorInt
    private int mEndColor;

    private GradientDrawable.Orientation mOrientation;

    private boolean isDirty = false;

    public GradientTextView(Context context) {
        this(context, null);
    }

    public GradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = getPaint();
        mContent = getText().toString();
        mPaint.getTextBounds(mContent, 0, mContent.length(), mRect);
        if (!isDirty) {
            mStartColor = getCurrentTextColor();
            mEndColor = getCurrentTextColor();
        }
        mShader = createLinearGradient(mStartColor, mEndColor, mOrientation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && mDrawable != null) {
            mShader = convert(mDrawable);
        }
        if (mShader != null) {
            mPaint.setShader(mShader);
        }
        super.onDraw(canvas);
    }

    public void setShader(Shader shader) {
        mShader = shader;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGradientDrawable(GradientDrawable drawable) {
        if (drawable == null) return;
        mDrawable = drawable;
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Shader convert(GradientDrawable drawable) {
        if (drawable == null) return null;
        Shader shader = null;
        switch (drawable.getGradientType()) {
            case GradientDrawable.LINEAR_GRADIENT:
                Rect rect = getRectByOrientation(drawable.getOrientation());
                shader = new LinearGradient(rect.left, rect.top, rect.right, rect.bottom,
                        drawable.getColors(), null, Shader.TileMode.CLAMP);
                break;
            case GradientDrawable.RADIAL_GRADIENT:
                shader = new RadialGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
                        Math.max(getMeasuredWidth(), getMeasuredHeight()) / 2,
                        drawable.getColors(), null, Shader.TileMode.CLAMP);
                break;
            case GradientDrawable.SWEEP_GRADIENT:
                shader = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
                        drawable.getColors(), null);
                break;
        }
        return shader;
    }

    private Rect getRectByOrientation(GradientDrawable.Orientation orientation) {
        if (orientation == null) return new Rect(0, 0, getMeasuredWidth(), 0);
        switch (orientation) {
            case TOP_BOTTOM:
                return new Rect(0, 0, 0, getMeasuredHeight());
            case BOTTOM_TOP:
                return new Rect(0, getMeasuredHeight(), 0, 0);
            case LEFT_RIGHT:
                return new Rect(0, 0, getMeasuredWidth(), 0);
            case RIGHT_LEFT:
                return new Rect(getMeasuredWidth(), 0, 0, 0);
            default:
                return new Rect(0, 0, getMeasuredWidth(), 0);
        }
    }

    public void setColors(@ColorInt int startColor, @ColorInt int endColor) {
        setColors(startColor, endColor, GradientDrawable.Orientation.LEFT_RIGHT);
    }

    public void setColors(@ColorInt int startColor, @ColorInt int endColor, GradientDrawable.Orientation orientation) {
        mStartColor = startColor;
        mEndColor = endColor;
        mOrientation = orientation;
        isDirty = true;
        mDrawable = null;   //否则会被覆盖
        invalidate();
    }

    public Shader getShader() {
        return mShader;
    }

    private LinearGradient createLinearGradient(@ColorInt int startColor, @ColorInt int endColor, GradientDrawable.Orientation orientation) {
        Rect rect = getRectByOrientation(orientation);
        return new LinearGradient(rect.left, rect.top, rect.right, rect.bottom,
                new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP);
    }

}
