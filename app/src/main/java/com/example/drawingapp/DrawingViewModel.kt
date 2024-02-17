package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.max

class DrawingViewModel : ViewModel() {
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
        // Add 1 empty bitmap by default
        _bitmapList.postValue(mutableListOf(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)))
        _currentDrawing.postValue(0)

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
        if (_bitmapList.value != null)
            _bitmapList.value!!.add(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888))
        else
            _bitmapList.postValue(mutableListOf(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)))

        _currentDrawing.postValue(_bitmapList.value!!.count() - 1)
    }

    // Remove a bitmap
    fun removeBitmap(id: Int) {
        _bitmapList.value?.let {
            if (id >= 0 && id < it.count())
                it.removeAt(id)

            val tempIndex = (_currentDrawing.value!! - 1) ?: 0
            _currentDrawing.postValue(max(tempIndex, 0))
        }
    }

    // Update the current bitmap
    fun updateCurrentBitmap(bitmap: Bitmap) {
        _bitmapList.value?.set(_currentDrawing.value!!, bitmap)
    }
}