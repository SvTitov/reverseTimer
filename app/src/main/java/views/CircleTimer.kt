package views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.watchtimer.R

class CircleTimer : View {

    private var cHeight: Int = 0
    private var cWeight: Int = 0
    private var emptyText: String = "0:00"
    private var paint: Paint = Paint()

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
        drawCircle(canvas)
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        paint.color = Color.WHITE
        paint.textSize = 100f

        var size = getTextSize(text!!, paint)

        setCustomFont()

        canvas?.drawText(text!!, (width /2).toFloat() - size.width() / 2,
            (height/2).toFloat(),
            paint)
    }

    private fun setCustomFont() {
        var font = ResourcesCompat.getFont(context, R.font.crystal_italic)
        paint.typeface = font
    }

    private fun getTextSize(text: String, paint: Paint): Rect {
        var rect = Rect()

        paint.getTextBounds(text,0, text.length, rect)

        return rect
    }

    private fun drawCircle(canvas: Canvas?) {

        var centerX = width / 2
        var centerY = height / 2

        var radius = width / 2

        paint.color = Color.BLACK

        canvas?.drawPaint(paint)

        paint.color = Color.RED

        canvas?.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
    }
}