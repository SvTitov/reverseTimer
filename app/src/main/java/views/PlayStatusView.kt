package views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator


class PlayStatusView : View {

    private lateinit var callback: () -> Unit
    private var backgroundPaint: Paint = Paint()
    private var path: Path = Path()
    private var animator: ValueAnimator? = null

    var isClicked: Boolean = false
        private set

    private var coefficient = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        coefficient = (h / 2).toFloat()

        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        colorBackground(canvas)

        backgroundPaint.color = Color.GREEN

        var halfHeight = height / 2

        if (coefficient < 0)
            coefficient = 0f
        if (coefficient > halfHeight)
            coefficient = halfHeight.toFloat()

        path.reset()

        path.moveTo(0f,0f)
        path.lineTo(width.toFloat(), 0f + coefficient)

        path.lineTo(width.toFloat(), (height - coefficient).toFloat())

        path.lineTo(0f, height.toFloat())

        path.lineTo(0f,0f)

        canvas!!.drawPath(path, backgroundPaint)
    }

    private fun colorBackground(canvas: Canvas?) {
        backgroundPaint.color = Color.TRANSPARENT
        canvas?.drawPaint(backgroundPaint)
    }

    fun onClick(callback: ()-> Unit){
        this.callback = callback
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        var halfHeight = (height / 2).toFloat()

        if (isClicked)
            transformationAnimation(0f, halfHeight)
        else
            transformationAnimation(halfHeight, 0f)

        isClicked = !isClicked

        callback()

        return super.onTouchEvent(event)
    }

    private fun transformationAnimation(startPosition: Float, endPosition: Float) {
        animator = ValueAnimator.ofFloat(startPosition, endPosition)
            .apply {
                addUpdateListener {
                    coefficient = it.animatedValue as Float
                }
                duration = 500L
                repeatCount = 0
                interpolator = LinearOutSlowInInterpolator()
                start()
            }
    }
}