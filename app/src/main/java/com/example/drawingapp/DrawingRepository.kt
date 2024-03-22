package com.example.drawingapp
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class DrawingRepository(private val scope : CoroutineScope, private val dao: DrawingDAO) {
    val allDrawings = dao.allDrawings()
    suspend fun addDrawing(): Long {
        return dao.addDrawing(Drawing())

    }
    fun getCurrentDrawing(Id : Int): Long {
        return dao.getCurrentDrawing(Id)
    }
}