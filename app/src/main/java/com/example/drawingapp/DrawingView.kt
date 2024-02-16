package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private val paint = Paint()
    private var path = Path()

    init {
        // TODO: Temporary initialization values, we will use the view model later
        bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)

        // Set up the paint
        paint.style = Paint.Style.STROKE
    }

    fun startTouch(x: Float, y: Float) {
        path.reset()
        path.moveTo(x, y)
        invalidate()
    }

    fun continueTouch(x: Float, y: Float) {
        path.lineTo(x, y)
        invalidate()
    }

    fun stopTouch() {
        // Put the path onto the actual bitmap
        canvas.drawPath(path, paint)
        path.reset()
    }

    fun setPenColor(color: Int) {
        paint.color = color
    }

    fun setPenSize(size: Float) {
        paint.strokeWidth = size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw everything onto the bitmap, clipping the pen stroke to the bounds
        bitmap.let { it ->
            canvas.apply {
                save()
                clipRect(0, 0, it.width, it.height)
                drawBitmap(it, 0f, 0f, paint)
                drawPath(path, paint)
                restore()
            }
        }
    }
}
