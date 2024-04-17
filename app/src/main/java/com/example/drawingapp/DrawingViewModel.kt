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

    fun getDrawingById(id : Long) : Bitmap {
        dbCurrentDrawing = repository.getCurrentDrawing(id)
        dbCurrentId = id
        return dbCurrentDrawing
    }
    fun getCurrentDrawing() : Bitmap {
        dbCurrentDrawing = repository.getCurrentDrawing(dbCurrentId)
        return dbCurrentDrawing
    }

    fun updateDrawing(bitmap: Bitmap, id: Long) {
        repository.updateDrawing(bitmap, id)
    }
    fun getFileName(id: Long) : String{
        return repository.getCurrentFileName(id)
    }

    fun addDrawing(filename: String) : String {
        val id = repository.addDrawing(filename)
        dbCurrentDrawing = repository.getCurrentDrawing(id)
        dbCurrentId = id
        return filename
    }



    // The current drawing
     lateinit var  brushShape : Paint.Cap


    // The color and size of the pen
    val penColor = MutableLiveData<Int>()
    val penSize = MutableLiveData<Float>()
    val penShape = MutableLiveData<Paint.Cap>()


    init {
        // Default values for pen color and size and shape
        penShape.postValue(Paint.Cap.ROUND)
        penColor.postValue(Color.BLACK)
        penSize.postValue(5.0f)
    }

    // Select a drawing if possible

    // The currently logged in user
    val loggedIn = MutableLiveData<Boolean>(false)
    val loggedInEmail = MutableLiveData<String>("")

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