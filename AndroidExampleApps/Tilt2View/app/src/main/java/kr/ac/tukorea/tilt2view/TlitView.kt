import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.util.Log
import android.view.View

class TlitView(context: Context?) : View(context) {
    private val greenPaint : Paint = Paint()
    private val blackPaint : Paint = Paint()
    private var cX : Float = 0f
    private var cY : Float = 0f
    private var xCoord  = 0f
    private var yCoord  = 0f

    init {
        greenPaint.color = Color.GREEN
        blackPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cX = w/2f
        cY = h/2f

    }
    fun onSensorEvent(event: SensorEvent){

       // Log.d("event:","$event")
        yCoord = event.values[0]*40
        xCoord = event.values[1]*40

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(cX +100+xCoord,cY+100+yCoord,cX-100+xCoord,cY-100+yCoord,greenPaint)
        canvas.drawLine(cX-100,cY-100,cX+100,cY-100,blackPaint.apply { strokeWidth =5f })
        canvas.drawLine(cX-100,cY+100,cX+100,cY+100,blackPaint)
        canvas.drawLine(cX-100,cY+100,cX-100,cY-100,blackPaint)
        canvas.drawLine(cX +100,cY+100,cX+100,cY-100,blackPaint)
       // canvas.drawCircle(cX+xCoord,cY+yCoord,100f,greenPaint)
        canvas.drawLine(cX-20,cY,cX+20,cY,blackPaint)
        canvas.drawLine(cX,cY-20,cX,cY+20,blackPaint)
    }
}