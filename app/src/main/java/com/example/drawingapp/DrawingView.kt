package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.shapes.Shape
import android.util.AttributeSet
import android.view.View
import androidx.compose.foundation.shape.CircleShape

typealias BitmapCallback = (bitmap: Bitmap?) -> Unit

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

//    private var curr_shape = Paint.Cap.ROUND;
    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
    private val paint = Paint()
    private var path = Path()

    // Callback for updating the ViewModel
    private var updateViewModelCallback: BitmapCallback? = null

    init {
        // Temporary bitmap, it should contain and do nothing until we get one from the ViewModel
        setBitmap(null, null)

        // Set up the paint
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
    }

    fun startTouch(x: Float, y: Float) {

//        if(curr_shape == DrawingViewModel.BrushShape.CIRCLE){
//            path.reset()
//            path.moveTo(x, y)
//
//        }
//        else if(curr_shape == DrawingViewModel.BrushShape.SQUARE){
//            val translation = DrawingViewModel. / 2
//            path.reset()
//            path.addRect(x - translation, y - translation,
//                x + translation, y + translation, Path.Direction.CW)
//        }
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
        updateViewModelCallback?.let { it(bitmap) }

        path.reset()
    }

    fun setPenColor(color: Int) {
        paint.color = color
    }

    fun setPenSize(size: Float) {
        paint.strokeWidth = size
    }

    fun setPenShape(brushShape: Paint.Cap){

        paint.strokeCap = brushShape;

    }



    // Sets the bitmap
    fun setBitmap(newBitmap: Bitmap?, callback: BitmapCallback?) {
        updateViewModelCallback = callback
        bitmap = newBitmap ?: Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        invalidate()
    }



    // Set the view size to the bitmap size
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Measure the bitmap size and set it as the measured dimensions
        val width = bitmap.width
        val height = bitmap.height
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap.let { it ->
            // Draw the bitmap and current pen stroke on the screen, scaling everything to fit
            canvas.apply {
                // Save the canvas before we change it for drawing
                save()

                // Ensure the path isn't drawn outside the bounds of the bitmap
                clipRect(0, 0, it.width, it.height)

                // Draw the canvas
                drawBitmap(it, 0f, 0f, paint)

                //canvas?.drawCircle(x, y, 10f, paint)
                drawPath(path, paint)



                // Restore the old canvas
                restore()
            }
        }
    }
}
