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

/**
 * Created by Juhezi[juhezix@163.com] on 2017/10/26.
 */

public class MultiSegmentProgressBar extends View {

    private static final String TAG = "MultiSegmentProgressBar";

    private static final float DEFAULT_SEPARATOR_HEIGHT = 20.0f;
    private static final float DEFAULT_SEPARATOR_WIDTH = 20.0f;
    private static final float DEFAULT_PROGRESS_HEIGHT = 15.0f;

    private static final int DEFAULT_PROGRESS_COLOR = Color.RED;
    private static final int DEFAULT_SEPARATOR_COLOR = Color.BLACK;

    private static final float DEFAULT_MAX_PROGRESS = 1.0f;

    private float mProgressHeight = DEFAULT_PROGRESS_HEIGHT;
    private float mSeparatorHeight = DEFAULT_SEPARATOR_HEIGHT;
    private float mSeparatorWidth = DEFAULT_SEPARATOR_WIDTH;

    private int mProgressColor = DEFAULT_PROGRESS_COLOR;
    private int mSeparatorColor = DEFAULT_SEPARATOR_COLOR;

    private float mMaxProgress = DEFAULT_MAX_PROGRESS;

    private float mHeight;
    private float mWidth;
    private float mTotalProdgressWidth;
    private float mCenterAxisY;

    private Paint mProgressPaint;
    private Paint mSeparatorPaint;

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
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mTotalProdgressWidth = mWidth;
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
        for (int i = 0; i < mProgressList.size(); i++) {
            drawProgress(canvas, mProgressList.get(i));
            if (i != mProgressList.size() - 1 || isDone) {
                drawSeparator(canvas, mProgressList.get(i));
            }
        }
    }

    private void drawProgress(Canvas canvas, ProgressHolder progressHolder) {
        if (progressHolder == null) return;
        RectF rectF = calculateProgressRect(progressHolder);
        if (rectF == null) return;
        canvas.drawRect(rectF, mProgressPaint);
    }

    private void drawSeparator(Canvas canvas, ProgressHolder progressHolder) {
        if (progressHolder == null) return;
        RectF rectF = calculateSeparatorRect(progressHolder);
        if (rectF == null) return;
        canvas.drawRect(rectF, mSeparatorPaint);
    }

    private RectF calculateProgressRect(ProgressHolder progressHolder) {
        if (progressHolder == null) return null;
        RectF rect = new RectF();
        rect.left = progressHolder.start / mMaxProgress * mTotalProdgressWidth;
        rect.top = mCenterAxisY - mProgressHeight / 2;
        float end = progressHolder.start + progressHolder.distance;
        if (end > mMaxProgress) {
            end = mMaxProgress;
        }
        rect.right = end / mMaxProgress * mTotalProdgressWidth;
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
        rect.right = end / mMaxProgress * mTotalProdgressWidth;
        rect.bottom = mCenterAxisY + mSeparatorHeight / 2;
        float width = mSeparatorWidth;  //理论宽度
        float maxWidth = (end - progressHolder.start) / mMaxProgress * mTotalProdgressWidth;
        if (width > maxWidth) {
            width = maxWidth;
        }
        rect.left = rect.right - width;
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
        if (getRestPercent() <= 0.01) return;
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
        if (isDone) {
            if (mProgressList.size() != 0) {
                mProgressList.remove(mProgressList.size() - 1);
            }
            isDone = true;
            invalidate();
        }
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
            if (result <= 0.01f) {
                result = 0f;
            }
            return result;
        }
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
