package com.example.drawingapp

import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Color
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
    fun updateDrawing(Id: Long){
        repository.updateDrawing(repository.getCurrentDrawing(Id), Id)
    }

    fun addDrawing() {
        var Id = repository.addDrawing()
        dbCurrentDrawing = repository.getCurrentDrawing(Id)
        dbCurrentId = Id

        Log.i("drawing id", "Drawing id is: $dbCurrentDrawing")
    }



    // The current drawings
    private val _bitmapList = MutableLiveData<MutableList<Bitmap>>()
    val bitmapList: LiveData<MutableList<Bitmap>> get() = _bitmapList

    // The currently selected drawing
    private val _currentDrawing = MutableLiveData<Int>()
    val currentDrawing: LiveData<Int> get() = _currentDrawing

    // The color and size of the pen
    val penColor = MutableLiveData<Int>()
    val penSize = MutableLiveData<Float>()

    init {
        // Default values for pen color and size
        penColor.postValue(Color.BLACK)
        penSize.postValue(5.0f)
    }

    // Select a drawing if possible
    fun selectDrawing(id: Int) {
        _bitmapList.value?.let {
            if (id >= 0 && id < it.count()) {
                _currentDrawing.postValue(id)
            }
        }
    }

    // Return the currently active bitmap
    fun getCurrentBitmap(): Bitmap? {
        _bitmapList.value?.let {
            return it[_currentDrawing.value!!]
        }
        // If for some reason we couldn't get the bitmap
        return null
    }

    // Add a bitmap
    fun addBitmap() {
        val lastCount: Int = _bitmapList.value?.size ?: 0

        if (_bitmapList.value != null && _bitmapList.value!!.isNotEmpty())
            _bitmapList.value!!.add(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888))
        else
            _bitmapList.postValue(mutableListOf(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)))

        _currentDrawing.postValue(lastCount)
    }

    // Remove a bitmap
    fun removeBitmap(id: Int) {
        _bitmapList.value?.let {
            if (id >= 0 && id < it.count())
                it.removeAt(id)

            if (_currentDrawing.value!! >= id) {
                val tempIndex = (_currentDrawing.value!! - 1) ?: 0
                _currentDrawing.postValue(if (tempIndex >= 0) tempIndex else null)
            }
        }
    }

    // Update the current bitmap
    fun updateCurrentBitmap(bitmap: Bitmap) {
        _bitmapList.value?.set(_currentDrawing.value!!, bitmap)
    }

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