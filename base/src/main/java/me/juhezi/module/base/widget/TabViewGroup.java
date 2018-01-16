package me.juhezi.module.base.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * 支持各种手势
 * 1. 单击
 * 2. 双击
 * 3. 双指缩放
 * 4. 左右滑动 【暂时先不管】
 * Created by Juhezi[juhezix@163.com] on 2017/11/1.
 */
public class TabViewGroup extends FrameLayout {

    private static final float DEFAULT_DX_LIMIT = 3.0f;    //最大速度限制

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private OnEventListener mOnEventListener;
    private float mScale = 0f;
    private float mCurrentScale = 0f;
    private boolean mOnScaleBegin = false;
    private boolean mDispatchSlideEvent = true; //是否分发滑动事件【仅指左右滑动】

    private static final String TAG = "TabViewGroup";

    public TabViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public TabViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScaleGestureDetector = new ScaleGestureDetector(getContext(),
                mOnScaleGestureListener);
        mGestureDetector = new GestureDetector(getContext(),
                mOnGestureListener);
        mCurrentScale = 0f;
        mScale = 0f;
        mOnScaleBegin = false;
        //----X----
        mAnimX.setDuration(100);
        mAnimX.setInterpolator(new DecelerateInterpolator());
        mAnimX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mOnEventListener != null) {
                    mOnEventListener.onSlide((Float) mAnimX.getAnimatedValue(), (float) mAnimY.getAnimatedValue(), true);
                }
            }
        });
        //----Y----
        mAnimY.setDuration(100);
        mAnimY.setInterpolator(new DecelerateInterpolator());
        mAnimSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mDispatchSlideEvent = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mDispatchSlideEvent = true;
            }
        });
        mAnimSet.playTogether(mAnimX, mAnimY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        dispatchSlideEvent(event);
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private float mWidth;   //控件的宽度
    private float mHeight;

    private float mStartX;
    private float mCurrentX;
    private float mEndX;

    private float mStartY;
    private float mCurrentY;
    private float mEndY;

    private float mDy;
    private float mLastY;
    private float mSplitY;

    private float mDx;  //位移
    private float mLastX;   //上一次的坐标
    private float mSplitX;

    private float mEndSplitX;
    private float mEndSplitY;

    private ValueAnimator mAnimX = ValueAnimator.ofFloat();
    private ValueAnimator mAnimY = ValueAnimator.ofFloat();
    private AnimatorSet mAnimSet = new AnimatorSet();


    /**
     * 处理滑动[左右滑动]
     *
     * @param event
     */
    private void dispatchSlideEvent(MotionEvent event) {
        if (!mDispatchSlideEvent) return;
        if (event.getPointerCount() > 1) return;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mLastX = mStartX;
                mLastY = mStartY;
                mSplitX = 0f;
                mSplitY = 0f;
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                mDx = mCurrentX - mLastX;
                mDy = mCurrentY - mLastY;
                mLastX = mCurrentX;
                mLastY = mCurrentY;
                mSplitX = (mCurrentX - mStartX) / mWidth;
                mSplitY = (mCurrentY - mStartY) / mHeight;
                onTouchMove();
                break;
            case MotionEvent.ACTION_UP:
                mEndX = event.getX();
                mEndY = event.getY();
                mSplitX = (mEndX - mStartX) / mWidth;
                mSplitY = (mEndY - mStartY) / mHeight;
                onTouchEnd();
                break;
            case MotionEvent.ACTION_CANCEL:
                mEndX = event.getX();
                mEndY = event.getY();
                mSplitX = (mEndX - mStartX) / mWidth;
                mSplitY = (mEndY - mStartY) / mWidth;
                onTouchEnd();
                break;
        }
    }

    /**
     * 处理触摸事件结束后的事情
     * 首先根据速度进行判断
     * 然后是当前位置
     */
    private void onTouchEnd() {
        //判断滑动的方向
        //首先判断速度
        if (Math.abs(mDx) >= DEFAULT_DX_LIMIT) {    //速度足够大，顺着速度继续走去
            mEndSplitX = mSplitX > 0 ? 1f : -1f;
        } else {
            //然后判断位置
            if (mSplitX < 0.5f && mSplitX > -0.5f) {  //位移不够
                mEndSplitX = 0f;
            } else {
                mEndSplitX = mSplitX > 0 ? 1f : -1f;
            }
        }
        if (Math.abs(mDy) >= DEFAULT_DX_LIMIT) {    //速度足够大，顺着速度继续走去
            mEndSplitY = mSplitY > 0 ? 1f : -1f;
        } else {
            //然后判断位置
            if (mSplitY < 0.5f && mSplitY > -0.5f) {  //位移不够
                mEndSplitY = 0f;
            } else {
                mEndSplitY = mSplitY > 0 ? 1f : -1f;
            }
        }
        if (mSplitX != mEndSplitX && mSplitY != mEndSplitY) {
            mAnimX.setFloatValues(mSplitX, mEndSplitX);
            mAnimY.setFloatValues(mSplitY, mEndSplitY);
            mAnimSet.setupEndValues();
            mAnimSet.start();
        }
    }

    /**
     * 处理滑动后的事件ing
     * 只需要当前位置相对位置
     */
    private void onTouchMove() {
        if (mOnEventListener != null) {
            mOnEventListener.onSlide(mSplitX, mSplitY, false);
        }
    }

    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener
            = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mOnScaleBegin = true;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mOnScaleBegin && mOnEventListener != null) {
                mCurrentScale = mCurrentScale +
                        (detector.getCurrentSpan() - detector.getPreviousSpan()) /
                                detector.getCurrentSpan();
                if (mCurrentScale > 1.0f) {
                    mCurrentScale = 1f;
                } else if (mCurrentScale < 0f) {
                    mCurrentScale = 0f;
                }
                mOnEventListener.onScale(mCurrentScale);
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mOnScaleBegin = false;
        }
    };

    private GestureDetector.OnGestureListener mOnGestureListener
            = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mOnEventListener != null) {
                mOnEventListener.onDoubleTab();
            }
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnEventListener != null) {
                mOnEventListener.onSingleTab(TabViewGroup.this, e);
            }
            return true;
        }

    };


    public void setOnEventListener(OnEventListener onEventListener) {
        this.mOnEventListener = onEventListener;
    }

    public interface OnEventListener {
        void onScale(float scale);

        void onSingleTab(View view, MotionEvent motionEvent);

        void onDoubleTab();

        /**
         * > 0 向右划
         * < 0 向左划
         *
         * @param splitX
         * @param splitY
         * @fling 是否为自由滑动阶段
         */
        void onSlide(float splitX, float splitY, boolean fling);
    }

}