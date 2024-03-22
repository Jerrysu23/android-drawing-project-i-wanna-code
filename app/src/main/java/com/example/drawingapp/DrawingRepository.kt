package com.example.drawingapp
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class DrawingRepository(private val scope : CoroutineScope, private val dao: DrawingDAO) {
    val allDrawings = dao.allDrawings()
    fun addDrawing(){
        scope.launch {
            dao.addDrawing(Drawing(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)))
        }

    }
    fun getCurrentDrawing(Id : Int): Drawing {
        return dao.getCurrentDrawing(Id)
    }
    fun updateCurrentDrawing(bitmap: Bitmap, Id: Int){
        dao.updateBitmap(bitmap, Id)
    }
}