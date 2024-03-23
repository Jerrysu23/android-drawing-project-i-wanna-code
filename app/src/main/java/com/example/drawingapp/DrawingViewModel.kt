package com.example.drawingapp

import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.IllegalArgumentException
import kotlin.math.max


class DrawingViewModel(private val repository: DrawingRepository): ViewModel() {
    val drawings = repository.allDrawings()
    var dbCurrentId: Long = 0
    var dbCurrentDrawing: Bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
    fun getCurrentDrawing(Id: Long) {

        dbCurrentDrawing = repository.getCurrentDrawing(Id)
        dbCurrentId = Id
    }
    fun updateDrawing(bitmap: Bitmap, Id: Long){
        repository.updateDrawing(bitmap, Id)
    }

    fun addDrawing() {
        var Id = repository.addDrawing()
        dbCurrentDrawing = repository.getCurrentDrawing(Id)
        dbCurrentId = Id

        Log.i("drawing id", "Drawing id is: $dbCurrentDrawing")
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