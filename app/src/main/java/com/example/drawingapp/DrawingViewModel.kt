package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


class DrawingViewModel(private val repository: DrawingRepository): ViewModel() {
    val drawings = repository.allDrawings()
    var dbCurrentId: Long = 0
    var dbCurrentDrawing: Bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)

    fun getDrawingById(id: Long) : Bitmap {
        dbCurrentDrawing = repository.getCurrentDrawing(id)
        dbCurrentId = id
        return dbCurrentDrawing
    }
    fun getCurrentDrawing() : Bitmap {
        dbCurrentDrawing = repository.getCurrentDrawing(dbCurrentId)
        return dbCurrentDrawing
    }

    fun updateDrawing(bitmap: Bitmap, Id: Long) {
        repository.updateDrawing(bitmap, Id)
    }

    fun addDrawing() : Long {
        val id = repository.addDrawing()
        dbCurrentDrawing = repository.getCurrentDrawing(id)
        dbCurrentId = id

        Log.i("drawing id", "Drawing id is: $dbCurrentDrawing")
        return id
    }



    // The current drawing

     lateinit var  BrushShape : Paint.Cap

 


    // The color and size of the pen
    val penColor = MutableLiveData<Int>()
    val penSize = MutableLiveData<Float>()
    val penShape = MutableLiveData<Paint.Cap>()


    init {
        // Default values for pen color and size
        penColor.postValue(Color.BLACK)
        penSize.postValue(5.0f)
    }

    // Select a drawing if possible

}
class DrawingViewModelFactory(private val repository: DrawingRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(DrawingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DrawingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}