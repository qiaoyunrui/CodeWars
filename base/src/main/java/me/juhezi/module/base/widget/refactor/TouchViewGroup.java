package me.juhezi.module.base.widget.refactor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.lang.ref.SoftReference;

/**
 * OK
 * <p>
 * 原生视频拍摄页的手势操作控件K
 * 支持的手势有:
 * 1. 短按
 * 2. 长按
 * 3. 双击
 * 4. 滑动
 * 5. 双指缩放
 * <p>
 * Created by Juhezi on 2018/1/22.
 */
public class TouchViewGroup extends FrameLayout {

    private boolean mCanLongClick = false;   //是否可以长按
    private boolean mCanDoubleClick = true; //是否可以双击

    private static final String TAG = "TouchViewGroup";

    private static final int LONG_CLICK_MESSAGE = 0x01;
    private static final int CLICK_MESSAGE = 0x02;

    private static final long CLICK_LIMIT_TIME = 400;  //单击的最小时间

    private static final long DOUBLE_CLICK_SPACE_TIME = 200;    //双击之间的最大时间间隔

    private static final long LONG_CLICK_LIMIT_TIME = 1000;  //长按的最小时间

    private static final long DOUBLE_CLICK_X_Y_ERROR = 50;  //检测双击的 XY误差

    private static final float DEFAULT_DX_LIMIT = 3.0f;    //最大速度限制

    private boolean isScaling = false;  //是否在缩放模式中
    private boolean isDoubleClicking = false;   //是否处于双击模式
    private boolean isScrolling = false;    //是否处于滑动中

    private float mStartX;
    private float mCurrentX;
    private float mEndX;

    private float mStartY;
    private float mCurrentY;
    private float mEndY;

    private float mLastX;
    private float mLastY;

    private long mLastTime;

    private long mStartTime;
    private long mCurrentTime;
    private long mEndTime;

    private float mTotalDistanceX;
    private float mTotalDistanceY;

    private int mDistanceX;
    private int mDistanceY;

    private float mSplitX;
    private float mSplitY;

    private float mEndSplitX;
    private float mEndSplitY;

    private long mLastUpTime;  //上次离开屏幕的时间
    private float mLastUpX;     //上次离开屏幕的 X 坐标
    private float mLastUpY;     //上次离开屏幕的 Y 坐标

    private float mWidth;
    private float mHeight;

    private ValueAnimator mAnimX = ValueAnimator.ofFloat();
    private ValueAnimator mAnimY = ValueAnimator.ofFloat();
    private AnimatorSet mAnimSet = new AnimatorSet();

    private ScaleGestureDetector mScaleGestureDetector; //用于处理缩放事件
    private float mCurrentScale = 0f;
    private boolean mOnScaleBegin = false;
    private boolean isFling = false;    //是否在惯性滑动

    private OnEventListener mOnEventListener;

    private Handler mHandler;   //用于执行延时事件，检测 UP 事件以后

    private MotionEvent mClickEvent;

    public TouchViewGroup(@NonNull Context context) {
        this(context, null);
    }

    public TouchViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TouchViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mHandler = new DelayHandler(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, mOnScaleGestureListener);
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
                isFling = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isFling = false;
            }
        });
        mAnimSet.playTogether(mAnimX, mAnimY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG, "onMeasure: " + mWidth + " x " + mHeight);
    }

    private ScaleGestureDetector.OnScaleGestureListener mOnScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:   //第一个手指初次接触到屏幕时触发
                dealActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                dealActionMove(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: //最后一个手指离开屏幕的时候
                dealActionUp(event);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:   //有非主要的手指按下(即按下之前已经有手指在屏幕上)
                //进入缩放模式
                if (!isScaling) {
                    isScaling = true;
//                    ToastUtilKt.showShortToast("进入缩放模式");
                    mHandler.removeMessages(LONG_CLICK_MESSAGE);
                    mHandler.removeMessages(CLICK_MESSAGE);
                    if (isScrolling) {  //如果正在进行滑动，立刻进行惯性滑动
                        fling();
                        isScrolling = false;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: //有非主要的手指抬起(即抬起之后仍然有手指在屏幕上)。
                break;
        }
        return true;
    }

    private void dealActionDown(MotionEvent event) {

        mStartTime = event.getEventTime();
        mStartX = event.getX();
        mStartY = event.getY();

        //通过距离上次点击的时间判断这是初次点击还是二次点击
        long dTime = mStartTime - mLastUpTime;
        float dx = mStartX - mLastUpX;
        float dy = mStartY - mLastUpY;

//        Log.i(TAG, "dealActionDown: " + dTime + "\n" + dx + "\n" + dy);

        mSplitX = 0f;
        mSplitY = 0f;

        isScrolling = false;

        mLastX = mCurrentX;
        mLastY = mCurrentY;
        mLastTime = mCurrentTime;

        if (mCanDoubleClick && checkDoubleClick(dTime, dx, dy)) {  //允许双击并且确实为双击操作，调用双击回调
            isDoubleClicking = true;
            mHandler.removeMessages(CLICK_MESSAGE);
            if (mOnEventListener != null) {
                mOnEventListener.onDoubleClick();
            }
        } else {
            isDoubleClicking = false;
            //发送长按延时事件
            mHandler.sendEmptyMessageDelayed(LONG_CLICK_MESSAGE, LONG_CLICK_LIMIT_TIME);
        }
        //----
    }

    private void dealActionMove(MotionEvent event) {

//        Log.i(TAG, "dealActionMove: MOVE");
        mLastTime = mCurrentTime;
        mCurrentTime = event.getEventTime();
        mCurrentX = event.getX();
        mCurrentY = event.getY();
        //计算位移
        mDistanceX = (int) (mCurrentX - mLastX);
        mDistanceY = (int) (mCurrentY - mLastY);

        mTotalDistanceX = mCurrentX - mStartX;
        mTotalDistanceY = mCurrentY - mStartY;

        mSplitX = div(mTotalDistanceX, mWidth);
        mSplitY = div(mTotalDistanceY, mHeight);

        mLastX = mCurrentX;
        mLastY = mCurrentY;
        mLastTime = mCurrentTime;

//        Log.i(TAG, "dealActionMove: splitX " + mSplitX + " " + mTotalDistanceX + " " + mWidth);

        if (isScaling) {   //缩放模式不做处理
            return;
        }

        if (!isScrolling) {
            //判断是否为滑动，如果是的话，则 isDoubleClicking = false
//            Log.i(TAG, "dealActionMove: " + mDistanceX + " " + mDistanceY);
            if (Math.abs(mTotalDistanceX) > DOUBLE_CLICK_X_Y_ERROR || Math.abs(mTotalDistanceY) > DOUBLE_CLICK_X_Y_ERROR && !isFling) { //移动距离超过XY误差，
//                Log.i(TAG, "dealActionMove: 进入滑动模式");
                isScrolling = true; //判定为滑动模式
                mHandler.removeMessages(LONG_CLICK_MESSAGE);
            }
        }

        if (isDoubleClicking) { //双击操作中不进行后续操作
            return;
        }

        if (isScrolling) {  //进行滑动的切换
            if (mOnEventListener != null) {
                if (mSplitX <= 1f && mSplitX >= -1f && mSplitY <= 1f && mSplitY >= -1f) {   //这里可能会因为 float 相除导致误差
                    mOnEventListener.onSlide(mSplitX, mSplitY, false);
                }
            }
            return;
        }
    }

    /**
     * 优先检测是否为缩放模式
     *
     * @param event
     */
    private void dealActionUp(MotionEvent event) {
        mEndTime = event.getEventTime();
        mEndX = event.getX();
        mEndY = event.getY();
        mTotalDistanceX = mEndX - mStartX;  //总位移
        mTotalDistanceY = mEndY - mStartY;  //总位移

        /*mDistanceX = (int) (mEndX - mLastX);     //上一段位移
        mDistanceY = (int) (mEndY - mLastY);     //上一段位移*/

//        Log.i(TAG, "dealActionUp: " + mDistanceX + " -- " + mDistanceY);

        //存储离开屏幕的时候，对应的时间 & 坐标
        mLastUpTime = mEndTime;
        mLastUpX = mEndX;
        mLastUpY = mEndY;

        mSplitX = div(mTotalDistanceX, mWidth);
        mSplitY = div(mTotalDistanceY, mHeight);

        if (isScaling) {   //缩放模式下
            isScaling = false;
            return;
        }

        if (isDoubleClicking) {     //双击模式下
            isDoubleClicking = false;
            return;
        }

        if (isScrolling) {  //滑动模式下
            isScrolling = false;
            //执行惯性滑动
//            Log.i(TAG, "dealActionUp: 执行惯性滑动");
            fling();
            return;
        }

        //检测点击时间是否大于长按的 limit
        if (mCanLongClick) {    //如果可以长按
            if (mEndTime - mStartTime >= LONG_CLICK_LIMIT_TIME) {   //点击时间大于长按的 limit 时间，这时长按的回调已经被调用，忽略掉最后的操作
                return;
            } else {    //点击时间小于长按的 limit 时间，不属于长按，所以要把延时事件取消掉
                mHandler.removeMessages(LONG_CLICK_MESSAGE);
            }
        }

        mClickEvent = event;

        if (mCanDoubleClick) {  //如果允许双击事件，则发送延时事件，判断上一次操作是否为双击操作
            //发送延时事件，该时间内进行判断是否已经进行第二次点击
            //如果未发生，则视为单击，否则，不是单击
            mHandler.sendEmptyMessageDelayed(CLICK_MESSAGE, DOUBLE_CLICK_SPACE_TIME);
        } else {    //不允许双击事件，则直接调用单击回调
//            Log.i(TAG, "dealActionUp: 单击");
            if (mOnEventListener != null) {
                mOnEventListener.onClick(event);
            }
        }

    }

    private void fling() {
        if (isFling) return;
//        Log.i(TAG, "fling: Hello 上一段移动距离： " + mDistanceX + " limit " + DEFAULT_DX_LIMIT);
        if (Math.abs(mDistanceX) >= DEFAULT_DX_LIMIT) {    //速度足够大，顺着速度继续走去
//            Log.i(TAG, "fling: 速度足够大");
            mEndSplitX = mSplitX > 0 ? 1f : -1f;
        } else {
//            Log.i(TAG, "fling: 速度不够，根据距离判断方向");
            //然后判断位置
            if (mSplitX < 0.5f && mSplitX > -0.5f) {  //位移不够
                mEndSplitX = 0f;    //回原位置
            } else {
                mEndSplitX = mSplitX > 0 ? 1f : -1f;
            }
        }
//        Log.i(TAG, "fling: " + mEndSplitX);
//        Log.i(TAG, "fling: " + mDistanceY);
        if (Math.abs(mDistanceY) >= DEFAULT_DX_LIMIT) {    //速度足够大，顺着速度继续走去
            mEndSplitY = mSplitY > 0 ? 1f : -1f;
        } else {
            //然后判断位置
            if (mSplitY < 0.5f && mSplitY > -0.5f) {  //位移不够
                mEndSplitY = 0f;
            } else {
                mEndSplitY = mSplitY > 0 ? 1f : -1f;
            }
        }
//        Log.i(TAG, "fling: " + mSplitX + " " + mEndSplitX);
        if (mSplitX != mEndSplitX && mSplitY != mEndSplitY) {
            mAnimX.setFloatValues(mSplitX, mEndSplitX);
            mAnimY.setFloatValues(mSplitY, mEndSplitY);
            mAnimSet.setupEndValues();
            mAnimSet.start();
        }
    }

    /**
     * 检测是否为 双击
     * <p>
     * 判定条件为：
     * 1. 时间间隔小于 标准的双击间隔
     * 2. x & y 差值小于 X & Y 误差
     *
     * @return 是否为双击
     */
    private boolean checkDoubleClick(long dTime, float dx, float dy) {
        return dTime <= DOUBLE_CLICK_SPACE_TIME && (dx <= DOUBLE_CLICK_X_Y_ERROR) && (dy <= DOUBLE_CLICK_X_Y_ERROR);
    }

    /**
     * 上次离开屏幕后一段时间（DOUBLE_CLICK_SPACE_TIME）后执行该函数
     * 判定是否为单击，而不是双击
     * 判定条件为：
     */
    private void determineClick() {
        if (mOnEventListener != null && System.currentTimeMillis() - mLastUpTime > DOUBLE_CLICK_SPACE_TIME) {
            mOnEventListener.onClick(mClickEvent);
        }
    }

    /**
     * 判定是否为长按事件
     */
    private void determineLongClick() {
        if (mOnEventListener != null && System.currentTimeMillis() - mStartTime >= LONG_CLICK_LIMIT_TIME) {
            mOnEventListener.onLongClick();
        }
    }

    public void setOnEventListener(OnEventListener onEventListener) {
        this.mOnEventListener = onEventListener;
    }

    private static class DelayHandler extends Handler {

        private SoftReference<TouchViewGroup> mRef;

        DelayHandler(TouchViewGroup touchViewGroup) {
            mRef = new SoftReference<>(touchViewGroup);
        }

        @Override
        public void handleMessage(Message msg) {
            TouchViewGroup touchViewGroup = mRef.get();
            if (touchViewGroup == null) return;
            switch (msg.what) {
                //在这里进行对应的调用
                case LONG_CLICK_MESSAGE:
                    touchViewGroup.determineLongClick();
                    break;
                case CLICK_MESSAGE:
                    touchViewGroup.determineClick();
                    break;
            }
        }

    }

    public static float div(float a, float b) {
        if (b == 0) return 0f;
        return a / b;
    }


    public interface OnEventListener {

        /**
         * 单击
         */
        void onClick(MotionEvent event);

        /**
         * 长按
         */
        void onLongClick();

        /**
         * 双击
         */
        void onDoubleClick();

        /**
         * 缩放
         *
         * @param scale
         */
        void onScale(float scale);

        /**
         * 非惯性滑动和惯性滑动
         *
         * @param x     X 轴方向的滑动
         * @param y     Y 轴方向的滑动
         * @param fling 是否为惯性滑动，即手指已经离开屏幕
         */
        void onSlide(float x, float y, boolean fling);

    }

}

/*
Note:
1.  缩放模式是优先级最高的，如果处于缩放模式的话，其他的情况一律不处理
    缩放处理 OK

2.  在 MOVE 事件中判断时候为滑动事件，优先级第二

3.  在 UP 事件中判断是否为双击，优先级第三
    双击事件 OK

4.  在 DOWN 事件中，Handler 发送一个延时事件（调用长按回调），
    在 UP 事件中，检测 [End - Start] 事件是否小于长按事件的时间，如果小于的话，Handler 就取消延时事件。

5.  在 UP 事件中，Handler 发送一个延时事件（检测并调用单击回调）
    在 UP 事件中，优先检测上一次 UP 时间距离当前 DOWN 时间小于双击事件间隔，Handler 就取消延时事件。
 */