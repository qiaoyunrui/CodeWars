package me.juhezi.module.base.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/25.
 */

public class CameraButton extends View {

    private static final String TAG = "CameraButton";

    private static final int STATE_IDLE = 0x10; //初始状态
    private static final int STATE_EXPANDING = 0x11;    //扩大中
    private static final int STATE_EXPANDED = 0x12;  //扩大
    private static final int STATE_UNEXPANDING = 0x13;  //缩小中

    private static final int INIT_STATE = STATE_IDLE;   //初始状态

    private static final int INVALID_DRAWABLE = 0x00;
    private static final int COLOR_DRAWABLE = 0x01; //Color
    private static final int BITMAP_DRAWABLE = 0x02;    //Bitmap
    //add Gradient-Support if have more time

    private static final long DEFAULT_ANIM_DURATION = 1000;

    private long mAnimDuration = DEFAULT_ANIM_DURATION;

    @CameraButton.State
    private int mCurrentState = INIT_STATE; //当前状态

    @IntDef({INVALID_DRAWABLE, COLOR_DRAWABLE, BITMAP_DRAWABLE})
    private @interface DrawableType {
    }

    @IntDef({STATE_IDLE, STATE_EXPANDING, STATE_EXPANDED, STATE_UNEXPANDING})
    private @interface State {
    }

    private static final float DEFAULT_INNER_CIRCLE_RADIUS = 100.0f;
    private static final float DEFAULT_OUTER_CIRCLE_RADIUS = 150.0f;

    private static final float DEFAULT_INNER_CIRCLE_SCALE = 0.8f;
    private static final float DEFAULT_OUTER_CIRCLE_SCALE = 1.2f;

    private static final int DEFAULT_INNER_NORMAL_COLOR = Color.RED;
    private static final int DEFAULT_OUTER_NORMAL_COLOR = Color.GREEN;

    private static final int DEFAULT_INNER_PRESSED_COLOR = Color.BLACK;
    private static final int DEFAULT_OUTER_PRESSED_COLOR = Color.BLUE;

    private float mInnerCircleRadius = DEFAULT_INNER_CIRCLE_RADIUS;   //内圆半径(初始)
    private float mOuterCircleRadius = DEFAULT_OUTER_CIRCLE_RADIUS;   //外圆半径(初始)

    private float mInnerCircleScale = DEFAULT_INNER_CIRCLE_SCALE;    //内圆缩放倍数 < 1
    private float mOuterCircleScale = DEFAULT_OUTER_CIRCLE_SCALE;    //外圆缩放倍数 > 1

    private Drawable mInnerCircleNormalDrawable = createColorDrawable(DEFAULT_INNER_NORMAL_COLOR);    //内圆未按压状态下的 Drawable
    private Drawable mOuterCircleNormalDrawable = createColorDrawable(DEFAULT_OUTER_NORMAL_COLOR);    //外圆未按压状态下的 Drawable

    private Drawable mInnerCirclePressedDrawable = createColorDrawable(DEFAULT_INNER_PRESSED_COLOR);   //内圆按压状态下的 Drawable
    private Drawable mOuterCirclePressedDrawable = createColorDrawable(DEFAULT_OUTER_PRESSED_COLOR);   //外圆按压状态下的 Drawable

    private float mHeight;
    private float mWidth;
    private Point mCircleCenter = new Point();  //圆心

    private Paint mInnerCirclePaint;
    private Paint mOuterCirclePaint;

    private ValueAnimator mExpendAnim;
    private ValueAnimator mUnexpendAnim;

    private float mCurrentPercent = 0f;

    public CameraButton(Context context) {
        this(context, null);
    }

    public CameraButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CameraButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initPaint();
        initAnim();
    }

    private void initView(Context context) {

    }

    private void initPaint() {
        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);
        configPaint(mInnerCirclePaint, mInnerCircleNormalDrawable);
        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setStyle(Paint.Style.FILL);
        configPaint(mOuterCirclePaint, mOuterCircleNormalDrawable);
    }

    private void initAnim() {
        mExpendAnim = ValueAnimator.ofFloat(0f, 1f);
        mExpendAnim.setDuration(mAnimDuration);
        mExpendAnim.setInterpolator(new LinearInterpolator());
        mExpendAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurrentState = STATE_EXPANDING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_EXPANDED;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentState = STATE_UNEXPANDING;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mExpendAnim.addUpdateListener(mAnimatorUpdateListener);
        mUnexpendAnim = ValueAnimator.ofFloat(1f, 0f);
        mUnexpendAnim.setDuration(mAnimDuration);
        mUnexpendAnim.setInterpolator(new LinearInterpolator());
        mUnexpendAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCurrentState = STATE_UNEXPANDING;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentState = STATE_IDLE;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentState = STATE_EXPANDING;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mUnexpendAnim.addUpdateListener(mAnimatorUpdateListener);
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mCurrentPercent = (float) animation.getAnimatedValue();
            invalidate();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int maxOuterCircleRadius = Math.min(widthSize, heightSize) / 2;  //计算最大半径
        if (maxOuterCircleRadius < mOuterCircleRadius)
            mOuterCircleRadius = maxOuterCircleRadius;
        if (maxOuterCircleRadius < mInnerCircleRadius)
            mInnerCircleRadius = maxOuterCircleRadius;
        if (mOuterCircleRadius <= 0) mOuterCircleScale = 1;
        float maxOuterCircleScale = maxOuterCircleRadius / mOuterCircleRadius; //计算外圆最大放大系数
        if (maxOuterCircleScale < mOuterCircleScale) {
            mOuterCircleScale = maxOuterCircleScale;
        }

        mWidth = widthSize;
        mHeight = heightSize;
        mCircleCenter.x = mWidth / 2;
        mCircleCenter.y = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updatePaint();
        drawCircle(canvas);
    }

    private Drawable createColorDrawable(int color) {
        return new ColorDrawable(color);
    }

    private float getCurrentScale(float startValue, float endValue) {
        float distance = endValue - startValue;
        return startValue + mCurrentPercent * distance;
    }

    //绘制两个同心圆
    //1. Outer circle
    //2. Inner circle
    private void drawCircle(Canvas canvas) {
        switch (mCurrentState) {
            case STATE_IDLE:
                drawIdleCircle(canvas);
                break;
            case STATE_EXPANDING:
            case STATE_UNEXPANDING:
                drawDynamicCircle(canvas);
                break;
            case STATE_EXPANDED:
                drawExpandedCircle(canvas);
                break;
        }
    }

    private void drawIdleCircle(Canvas canvas) {
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mOuterCircleRadius, mOuterCirclePaint);
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mInnerCircleRadius, mInnerCirclePaint);
    }

    private void drawExpandedCircle(Canvas canvas) {
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mOuterCircleRadius * mOuterCircleScale, mOuterCirclePaint);
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mInnerCircleRadius * mInnerCircleScale, mInnerCirclePaint);
    }

    private void drawDynamicCircle(Canvas canvas) {
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y,
                mOuterCircleRadius * getCurrentScale(1.0f, mOuterCircleScale), mOuterCirclePaint);
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y,
                mInnerCircleRadius * getCurrentScale(1.0f, mInnerCircleScale), mInnerCirclePaint);
    }

    /**
     * Color
     */
    private void updatePaint() {
        if (mCurrentState == STATE_IDLE) {
            configPaint(mInnerCirclePaint, mInnerCircleNormalDrawable);
            configPaint(mOuterCirclePaint, mOuterCircleNormalDrawable);
        } else {
            configPaint(mInnerCirclePaint, mInnerCirclePressedDrawable);
            configPaint(mOuterCirclePaint, mOuterCirclePressedDrawable);
        }
    }

    @DrawableType
    private int checkDrawableType(Drawable drawable) {
        if (drawable == null) {
            return INVALID_DRAWABLE;
        }
        if (drawable instanceof ColorDrawable) {
            return COLOR_DRAWABLE;
        }
        if (drawable instanceof BitmapDrawable) {
            return BITMAP_DRAWABLE;
        }
        return INVALID_DRAWABLE;
    }

    /**
     * set color for Paint
     *
     * @param paint
     * @param drawable
     */
    private void configPaint(Paint paint, Drawable drawable) {
        if (paint == null) return;
        switch (checkDrawableType(drawable)) {
            case COLOR_DRAWABLE:
                ColorDrawable colorDrawable = (ColorDrawable) drawable;
                paint.setColor(colorDrawable.getColor());
                return;
            case BITMAP_DRAWABLE:
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                BitmapShader bitmapShader = new BitmapShader(bitmapDrawable.getBitmap(),
                        Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setShader(bitmapShader);
                return;
            case INVALID_DRAWABLE:
                break;
        }
    }

    /**
     * Handle the TouchEvent
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mCurrentState == STATE_IDLE) {
                    mExpendAnim.setFloatValues(0f, 1f);
                    mExpendAnim.setDuration(mAnimDuration);
                    mExpendAnim.start();
                }
                if (mCurrentState == STATE_UNEXPANDING && mUnexpendAnim.isRunning()) {
                    mUnexpendAnim.cancel();
                    mExpendAnim.setFloatValues(mCurrentPercent, 1f);
                    mExpendAnim.setDuration((long) ((1 - mCurrentPercent) * mAnimDuration));
                    mExpendAnim.start();
                }
                break;
            case MotionEvent.ACTION_SCROLL:
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentState == STATE_EXPANDED) {
                    mUnexpendAnim.setFloatValues(1f, 0f);
                    mUnexpendAnim.start();
                }
                if (mCurrentState == STATE_EXPANDING && mExpendAnim.isRunning()) {
                    mExpendAnim.cancel();
                    mUnexpendAnim.setFloatValues(mCurrentPercent, 0f);
                    mUnexpendAnim.setDuration((long) (mCurrentPercent * mAnimDuration));
                    mUnexpendAnim.start();
                }
                break;
        }
        return true;
    }

    //------------------------get & set--------------------------

    public float getInnerCircleRadius() {
        return mInnerCircleRadius;
    }

    public void setInnerCircleRadius(float innerCircleRadius) {
        if (innerCircleRadius <= 0) return;
        this.mInnerCircleRadius = innerCircleRadius;
    }

    public float getOuterCircleRadius() {
        return mOuterCircleRadius;
    }

    public void setOuterCircleRadius(float outerCircleRadius) {
        if (outerCircleRadius <= 0) return;
        this.mOuterCircleRadius = outerCircleRadius;
    }

    public float getInnerCircleScale() {
        return mInnerCircleScale;
    }

    public void setInnerCircleScale(float innerCircleScale) {
        if (innerCircleScale <= 0) return;
        this.mInnerCircleScale = innerCircleScale;
    }

    public float getOuterCircleScale() {
        return mOuterCircleScale;
    }

    public void setOuterCircleScale(float outerCircleScale) {
        if (outerCircleScale <= 0) return;
        this.mOuterCircleScale = outerCircleScale;
    }

    public Drawable getInnerCircleNormalDrawable() {
        return mInnerCircleNormalDrawable;
    }

    public void setInnerCircleNormalDrawable(Drawable innerCircleNormalDrawable) {
        if (innerCircleNormalDrawable == null) return;
        this.mInnerCircleNormalDrawable = innerCircleNormalDrawable;
    }

    public Drawable getOuterCircleNormalDrawable() {
        return mOuterCircleNormalDrawable;
    }

    public void setOuterCircleNormalDrawable(Drawable outerCircleNormalDrawable) {
        if (outerCircleNormalDrawable == null) return;
        this.mOuterCircleNormalDrawable = outerCircleNormalDrawable;
    }

    public Drawable getInnerCirclePressedDrawable() {
        return mInnerCirclePressedDrawable;
    }

    public void setInnerCirclePressedDrawable(Drawable innerCirclePressedDrawable) {
        if (innerCirclePressedDrawable == null) return;
        this.mInnerCirclePressedDrawable = innerCirclePressedDrawable;
    }

    public Drawable getOuterCirclePressedDrawable() {
        return mOuterCirclePressedDrawable;
    }

    public void setOuterCirclePressedDrawable(Drawable outerCirclePressedDrawable) {
        if (outerCirclePressedDrawable == null) return;
        this.mOuterCirclePressedDrawable = outerCirclePressedDrawable;
    }

    //-------------------------------Inner Class--------------------------------

    private static class Point {
        float x;
        float y;

        Point() {
            this.x = 0f;
            this.y = 0f;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
