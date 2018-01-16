package me.juhezi.module.base.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


import java.util.LinkedList;
import java.util.List;

import me.juhezi.module.base.BaseApplication;
import me.juhezi.module.base.extensions.ContextExKt;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/26.
 */

public class MultiSegmentProgressBar extends View {

    private static final String TAG = "MultiSegmentProgressBar";

    private static final float DEFAULT_SEPARATOR_HEIGHT = ContextExKt.dp2px(BaseApplication.getContext(), 6);//6dp
    private static final float DEFAULT_SEPARATOR_WIDTH = ContextExKt.dp2px(BaseApplication.getContext(), 2);  //2dp
    private static final float DEFAULT_PROGRESS_HEIGHT = ContextExKt.dp2px(BaseApplication.getContext(), 6);    //6dp
    private static final float DEFAULT_LIMIT_SEPARATOR_WIDTH = ContextExKt.dp2px(BaseApplication.getContext(), 3);    //3dp

    private static final int DEFAULT_PROGRESS_COLOR = 0xFF3697F1;
    private static final int DEFAULT_SEPARATOR_COLOR = Color.WHITE;
    private static final int DEFAULT_LIMIT_SEPARATOR_COLOR = 0x4D000000;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x1A000000;     //半透明黑

    private static final float DEFAULT_MAX_PROGRESS = 1.0f;
    private static final float DEFAULT_LIMIT_PROGRESS = 0.2f;   //  1/5

    private float mProgressHeight = DEFAULT_PROGRESS_HEIGHT;
    private float mSeparatorHeight = DEFAULT_SEPARATOR_HEIGHT;
    private float mSeparatorWidth = DEFAULT_SEPARATOR_WIDTH;
    private float mLimitSeparatorWidth = DEFAULT_LIMIT_SEPARATOR_WIDTH;

    private int mProgressColor = DEFAULT_PROGRESS_COLOR;
    private int mSeparatorColor = DEFAULT_SEPARATOR_COLOR;
    private int mLimitSeparatorColor = DEFAULT_LIMIT_SEPARATOR_COLOR;
    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private int mWillDeleteProgressColor = 0xFFEF3F1E;

    private float mMaxProgress = DEFAULT_MAX_PROGRESS;
    private float mLimitProgress = DEFAULT_LIMIT_PROGRESS;

    private float mHeight;
    private float mWidth;
    private float mTotalProgressWidth;
    private float mCenterAxisY;

    private Paint mProgressPaint;
    private Paint mSeparatorPaint;
    private Paint mBackgroundPaint;
    private Paint mLimitSeparatorPaint;
    private Paint mWillDeleteProgressPaint;

    private boolean mWillDelete = false;    //true -> 进入即将删除阶段

    private boolean isDone = false;

    private List<ProgressHolder> mProgressList = new LinkedList<>();

    public MultiSegmentProgressBar(Context context) {
        this(context, null);
    }

    public MultiSegmentProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MultiSegmentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initPaint();
    }

    private void initView(Context context) {

    }

    private void initPaint() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mProgressColor);
        mSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSeparatorPaint.setStyle(Paint.Style.FILL);
        mSeparatorPaint.setColor(mSeparatorColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);
        mLimitSeparatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLimitSeparatorPaint.setStyle(Paint.Style.FILL);
        mLimitSeparatorPaint.setColor(mLimitSeparatorColor);
        mWillDeleteProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWillDeleteProgressPaint.setStyle(Paint.Style.FILL);
        mWillDeleteProgressPaint.setColor(mWillDeleteProgressColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mTotalProgressWidth = mWidth;
        mCenterAxisY = mHeight / 2;
        if (mProgressHeight > mHeight) {
            mProgressHeight = mHeight;
        }
        if (mSeparatorHeight > mHeight) {
            mSeparatorHeight = mHeight;
        }
        if (mSeparatorWidth > mWidth) {
            mSeparatorWidth = mWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mProgressList.isEmpty()) {
            drawBackground(canvas);
            drawLimitSeparator(canvas);
        }
        for (int i = 0; i < mProgressList.size(); i++) {
            if (i == mProgressList.size() - 1) {    //最后一个
                if (mWillDelete) {  //即将删除状态
                    drawProgress(canvas, mProgressList.get(i), mWillDeleteProgressPaint);
                } else {
                    drawProgress(canvas, mProgressList.get(i), mProgressPaint);
                }
            } else {    //前面的几个
                drawProgress(canvas, mProgressList.get(i), mProgressPaint);
                drawSeparator(canvas, mProgressList.get(i));
            }
        }
    }

    private void drawProgress(Canvas canvas, ProgressHolder progressHolder, Paint paint) {
        if (progressHolder == null) return;
        RectF rectF = calculateProgressRect(progressHolder);
        if (rectF == null) return;
        canvas.drawRect(rectF, paint);
    }

    private void drawSeparator(Canvas canvas, ProgressHolder progressHolder) {
        if (progressHolder == null) return;
        RectF rectF = calculateSeparatorRect(progressHolder);
        if (rectF == null) return;
        canvas.drawRect(rectF, mSeparatorPaint);
    }

    /**
     * 绘制背景
     */
    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, mTotalProgressWidth, mProgressHeight, mBackgroundPaint);
    }

    private void drawLimitSeparator(Canvas canvas) {
        RectF rectF = calculateLimitSeparatorRect();
        if (rectF == null) return;
        canvas.drawRect(rectF, mLimitSeparatorPaint);
    }

    private RectF calculateProgressRect(ProgressHolder progressHolder) {
        if (progressHolder == null) return null;
        RectF rect = new RectF();
        rect.left = progressHolder.start / mMaxProgress * mTotalProgressWidth;
        rect.top = mCenterAxisY - mProgressHeight / 2;
        float end = progressHolder.start + progressHolder.distance;
        if (end > mMaxProgress) {
            end = mMaxProgress;
        }
        rect.right = end / mMaxProgress * mTotalProgressWidth;
        rect.bottom = mCenterAxisY + mProgressHeight / 2;
        return rect;
    }

    private RectF calculateSeparatorRect(ProgressHolder progressHolder) {
        if (progressHolder == null) return null;
        float end = progressHolder.start + progressHolder.distance;
        if (end > mMaxProgress) {
            end = mMaxProgress;
        }
        RectF rect = new RectF();
        rect.top = mCenterAxisY - mSeparatorHeight / 2;
        rect.right = end / mMaxProgress * mTotalProgressWidth;
        rect.bottom = mCenterAxisY + mSeparatorHeight / 2;
        float width = mSeparatorWidth;  //理论宽度
        float maxWidth = (end - progressHolder.start) / mMaxProgress * mTotalProgressWidth;
        if (width > maxWidth) {
            width = maxWidth;
        }
        rect.left = rect.right - width;
        return rect;
    }

    private RectF calculateLimitSeparatorRect() {
        RectF rect = new RectF();
        rect.top = mCenterAxisY - mProgressHeight / 2;
        rect.bottom = mCenterAxisY + mProgressHeight / 2;
        float tempLeft = mTotalProgressWidth * mLimitProgress / mMaxProgress - mLimitSeparatorWidth / 2;
        float tempRight = mTotalProgressWidth * mLimitProgress / mMaxProgress + mLimitSeparatorWidth / 2;
        if (tempLeft < 0) tempLeft = 0;
        if (tempRight > mTotalProgressWidth) tempRight = mTotalProgressWidth;
        rect.left = tempLeft;
        rect.right = tempRight;
        return rect;
    }


    public void newSegmentProgress() {
        if (getRestPercent() <= 0.01) return;
        ProgressHolder holder = new ProgressHolder();
        if (mProgressList.size() == 0) {
            holder.start = 0f;
            holder.distance = 0f;
        } else {
            ProgressHolder lastHolder = mProgressList.get(mProgressList.size() - 1);
            holder.start = lastHolder.start + lastHolder.distance;
            if (holder.start > mMaxProgress) {
                holder.start = mMaxProgress;
            }
            holder.distance = 0;
        }
        mProgressList.add(holder);
        isDone = false;
        invalidate();
    }

    public void done() {
        isDone = true;
        invalidate();
    }

    public void update(float newDistance) {
        if (getRestPercent() <= 0f) return;
        if (mProgressList.size() == 0) {
            return;
        }
        ProgressHolder holder = mProgressList.get(mProgressList.size() - 1);
        holder.distance = newDistance;
        invalidate();
    }

    public void clear() {
        mProgressList.clear();
        invalidate();
    }

    public void remove() {
        if (mProgressList.size() != 0) {
            mProgressList.remove(mProgressList.size() - 1);
        }
        isDone = true;
        mWillDelete = false;
        postInvalidate();
    }

    /**
     * 进入即将删除状态
     */
    public void willRemove() {
        mWillDelete = true;
        invalidate();
    }

    /**
     * 取消删除
     */
    public void cancelRemove() {
        mWillDelete = false;
        invalidate();
    }

    public void cancel() {
        if (!isDone) {  //刚刚结束 Update 阶段
            if (mProgressList.size() != 0) {
                mProgressList.remove(mProgressList.size() - 1);
            }
            isDone = true;
            invalidate();
        }
    }

    public boolean canPop() {
        return !mProgressList.isEmpty();
    }

    /**
     * 百分制
     *
     * @return
     */
    public float getRestPercent() {
        if (mProgressList.size() == 0) {
            return 1f;
        } else {
            ProgressHolder holder = mProgressList.get(mProgressList.size() - 1);
            float restPercent = mMaxProgress - (holder.start + holder.distance);
            if (restPercent < 0) {
                restPercent = 0;
            }
            float result = restPercent / mMaxProgress;
            if (result <= 0.0001f) {
                result = 0f;
            }
            return result;
        }
    }

    /**
     * 是否处于即将删除状态
     *
     * @return
     */
    public boolean isWillDelete() {
        return mWillDelete;
    }

    //-------------------------------------get & set-----------------------------------

    //-------------------------------------Inner Color---------------------------------
    static class ProgressHolder {
        float start;
        float distance;

        @Override
        public String toString() {
            return "ProgressHolder{" +
                    "start=" + start +
                    ", distance=" + distance +
                    '}';
        }
    }
}
