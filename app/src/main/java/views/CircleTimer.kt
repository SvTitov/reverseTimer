package views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.watchtimer.R
import kotlin.math.cos
import kotlin.math.sin


class CircleTimer : View {

    private var cHeight: Int = 0
    private var cWeight: Int = 0
    private var emptyText: String = "0:00"
    private var paint: Paint = Paint()
    private var angle: Float = 270f

    var text: String? = null
        get(){
            if (field == null)
                return emptyText
            return field
        }
        set(value) {
            field = value
            postInvalidate()
        }


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        cWeight = w
        cHeight = h 
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawShadow(canvas)
        drawCircle(canvas)
        drawMovingCircle(canvas)
        drawText(canvas)
    }

    private fun drawShadow(canvas: Canvas?) {
        var uWidth = (width / 2).toFloat()
        var uHeight = (height / 2).toFloat()
        var radius = uWidth + 15

        val gradient: RadialGradient = RadialGradient(
            uWidth, uHeight, radius , -0x1,
            -0x1000000, Shader.TileMode.CLAMP
        )

        val p = Paint()
        p.isDither = true
        p.shader = gradient
        canvas!!.drawCircle(uWidth, uHeight, radius, p)
    }

    private fun drawMovingCircle(canvas: Canvas?) {
        //https://www.khanacademy.org/math/algebra2/x2ec2f6f830c9fb89:trig/x2ec2f6f830c9fb89:unit-circle/a/trig-unit-circle-review

        var radius = 20f

        var ofRadius = width / 2 - 20

        var pos = getPosition(
            PointF((width / 2).toFloat(), (height / 2).toFloat()),
            ofRadius.toFloat(),
            angle
        )

        paint.color = Color.GREEN

        canvas?.drawCircle(pos!!.x, pos!!.y, radius, paint)
    }

    private fun getPosition(center: PointF, radius: Float, angle: Float): PointF? {
        return PointF(
            (center.x + radius * cos(Math.toRadians(angle.toDouble()))).toFloat(),
            (center.y + radius * sin(Math.toRadians(angle.toDouble()))).toFloat()
        )
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = Color.WHITE
        paint.textSize = 100f

        var size = getTextSize(text!!, paint)

        setCustomFont()

        canvas?.drawText(
            text!!, (width / 2).toFloat() - size.width() / 2,
            (height / 2).toFloat(),
            paint
        )
    }

    private fun setCustomFont() {
        var font = ResourcesCompat.getFont(context, R.font.crystal_italic)
        paint.typeface = font
    }

    private fun getTextSize(text: String, paint: Paint): Rect {
        var rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect
    }

    private fun drawCircle(canvas: Canvas?) {
        var centerX = width / 2
        var centerY = height / 2

        var radius = (width / 2) - 20

        paint.color = Color.RED

        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
    }
}