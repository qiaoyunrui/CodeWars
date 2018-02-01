package me.juhezi.module.base.widget.refactor

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import me.juhezi.module.base.BaseApplication
import me.juhezi.module.base.extensions.dp2px

/**
 *
 * Waitting
 *
 * [Refactor]
 *
 * 重构后的拍照按钮，和 微信 & 微博故事 的交互一样
 *
 * CameraButton 状态有：
 * 1. IDLE 静止状态
 * 2. PRESSED 按压状态，即短按状态、拍照状态
 * 3. LONG_PRESSED 长按状态，即摄像状态
 * 4. INVALID 不可按压状态
 *
 * 以上四种状态都是内部状态，外部无法影响
 *
 * Created by Juhezi on 2018/1/20.
 */
class CameraButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : View(context, attrs, defStyleAttr) {

    private var mInnerCircleRadius = DEFAULT_INNER_CIRCLE_RADIUS   //内圆半径(初始)
    private var mOuterCircleRadius = DEFAULT_OUTER_CIRCLE_RADIUS   //外圆半径(初始)

    private var mInnerCircleScale = DEFAULT_INNER_CIRCLE_SCALE    //内圆缩放倍数 < 1
    private var mOuterCircleScale = DEFAULT_OUTER_CIRCLE_SCALE    //外圆缩放倍数 > 1

    //正常状态下的 Drawable
    private var mInnerCircleNormalDrawable = createColorDrawable(DEFAULT_INNER_NORMAL_COLOR)    //内圆未按压状态下的 Drawable
    private var mOuterCircleNormalDrawable = createColorDrawable(DEFAULT_OUTER_NORMAL_COLOR)    //外圆未按压状态下的 Drawable

    //按压状态下的 Drawable
    private var mInnerCirclePressedDrawable = createColorDrawable(DEFAULT_INNER_PRESSED_COLOR)   //内圆按压状态下的 Drawable
    private var mOuterCirclePressedDrawable = createColorDrawable(DEFAULT_OUTER_PRESSED_COLOR)   //外圆按压状态下的 Drawable

    //长按状态下的 Drawable


    private var mHeight: Float = 0.toFloat()
    private var mWidth: Float = 0.toFloat()
    private val mCircleCenter = Point()  //圆心

    private var mInnerCirclePaint: Paint? = null
    private var mOuterCirclePaint: Paint? = null

    private var mExpendAnim: ValueAnimator? = null
    private var mUnexpendAnim: ValueAnimator? = null

    private var mCurrentPercent = 0f
    private var mLastDistance: Long = 0

    private var mCanClick = true    //是否可以点击

    //Record Touch-Event Time
    private var mStartTime: Long = 0     //开始点击时间
    private var mCurrentTime: Long = 0  //当前时间
    private var mEndTime: Long = 0  //结束点击时间

    private var mCurrentZoom = 1.0f

    companion object {

        private val STATE_IDLE = 0x10 //初始状态
        private val STATE_EXPANDING = 0x11    //扩大中
        private val STATE_EXPANDED = 0x12  //扩大
        private val STATE_UNEXPANDING = 0x13  //缩小中

        private val INIT_STATE = STATE_IDLE   //初始状态

        private val INVALID_DRAWABLE = 0x00
        private val COLOR_DRAWABLE = 0x01 //Color
        private val BITMAP_DRAWABLE = 0x02    //Bitmap

        private val DEFAULT_ANIM_DURATION: Long = 100  //动画持续时间

        private val DEFAULT_CLICK_LIMIT_TIME: Long = 1500

        private val DEFAULT_CLICK_MAX_TIME = (15 * 1000).toLong()   //最长时间为 15 秒

        private val DEFAULT_ZOOM_LOAD_FACTOR = 0.75f

        private val DEFAULT_INNER_CIRCLE_RADIUS = BaseApplication.getContext().dp2px(27.5f).toFloat()    //55dp
        private val DEFAULT_OUTER_CIRCLE_RADIUS = BaseApplication.getContext().dp2px(40f).toFloat()    //80dp

        private val DEFAULT_INNER_CIRCLE_SCALE = 0.87273f
        private val DEFAULT_OUTER_CIRCLE_SCALE = 1.5f

        private val DEFAULT_INNER_NORMAL_COLOR = Color.WHITE
        private val DEFAULT_OUTER_NORMAL_COLOR = 0x3DFFFFFF

        private val DEFAULT_INNER_PRESSED_COLOR = 0x383839
        private val DEFAULT_OUTER_PRESSED_COLOR = 0x3DFFFFFF

        private val DEFAULT_INNER_LONG_PRESSED_COLOR = Color.RED
        private val DEFAULT_OUTER_LONG_PRESSED_COLOR = Color.BLUE

        private fun createColorDrawable(color: Int): Drawable = ColorDrawable(color)

    }

}
