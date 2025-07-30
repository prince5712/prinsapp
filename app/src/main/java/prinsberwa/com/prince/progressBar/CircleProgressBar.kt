package prinsberwa.com.prince.progressBar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import prinsberwa.com.R
import prinsberwa.com.prince.IGRefreshLayout

class CircleProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseProgressBar(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var backColor = 0
    private var frontColor = 0
    private var borderWidth = dp2px(4).toFloat()
    private var size = dp2px(40)
    private var mIndeterminateSweep = 85f
    private var mStartAngle = 0f
    private val mRect = RectF()

    private var progressAnimator: ValueAnimator? = null

    init {
        // Get colors from theme attributes
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CircleProgressBar,
            defStyleAttr,
            0
        )

        try {
            // Get colors from attributes with fallback to default theme colors
            backColor = typedArray.getColor(
                R.styleable.CircleProgressBar_progressBackgroundColor,
                getThemeColor(android.R.attr.colorControlHighlight)
            )

            frontColor = typedArray.getColor(
                R.styleable.CircleProgressBar_progressForegroundColor,
                getThemeColor(android.R.attr.colorAccent)
            )

            borderWidth = typedArray.getDimension(
                R.styleable.CircleProgressBar_progressBorderWidth,
                dp2px(4).toFloat()
            )

            size = typedArray.getDimensionPixelSize(
                R.styleable.CircleProgressBar_progressSize,
                dp2px(40)
            )
        } finally {
            typedArray.recycle()
        }

        paint.apply {
            color = frontColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }

        backPaint.apply {
            color = backColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }
    }

    // Helper method to get color from theme attribute
    private fun getThemeColor(attr: Int): Int {
        val typedValue = android.util.TypedValue()
        context.theme.resolveAttribute(attr, typedValue, true)
        return if (typedValue.resourceId != 0) {
            ContextCompat.getColor(context, typedValue.resourceId)
        } else {
            typedValue.data
        }
    }

    fun setBorderWidth(width: Int) {
        borderWidth = dp2px(width).toFloat()
        paint.strokeWidth = borderWidth
        backPaint.strokeWidth = borderWidth
        invalidate()
    }

    fun setColors(backColor: Int, frontColor: Int) {
        this.backColor = backColor
        this.frontColor = frontColor
        paint.color = frontColor
        backPaint.color = backColor
        invalidate()
    }

    fun setSize(px: Int) {
        size = dp2px(px)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Define the bounds for the arc
        val left = ((width - size) / 2).toFloat()
        val top = mParent.DRAG_MAX_DISTANCE / 3f
        val right = left + size
        val bottom = top + size
        mRect.set(left, top, right, bottom)

        // Draw background arc
        canvas.drawArc(mRect, 270f, 360f, false, backPaint)

        if (isLoading) {
            // Draw indeterminate arc
            canvas.drawArc(mRect, mStartAngle, mIndeterminateSweep, false, paint)
        } else {
            // Draw progress arc
            drawProgress(canvas)
        }
    }

    private fun drawProgress(canvas: Canvas) {
        val sweepAngle = mPercent * 3.6f
        canvas.drawArc(mRect, 270f, sweepAngle, false, paint)
    }

    override fun setParent(parent: IGRefreshLayout) {
        mParent = parent
    }

    override fun setPercent(percent: Float) {
        mPercent = percent.coerceIn(0f, 100f) // Ensures percent is between 0 and 100
        invalidate()
    }

    override fun start() {
        isLoading = true
        resetAnimation()
    }

    override fun stop() {
        stopAnimation()
    }

    private fun resetAnimation() {
        progressAnimator?.cancel()

        progressAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener {
                mStartAngle = it.animatedValue as Float
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    resetAnimation()
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationStart(animation: Animator) {}
            })
            start()
        }
    }

    private fun stopAnimation() {
        isLoading = false
        progressAnimator?.cancel()
        progressAnimator?.removeAllListeners()
        progressAnimator = null
    }
}