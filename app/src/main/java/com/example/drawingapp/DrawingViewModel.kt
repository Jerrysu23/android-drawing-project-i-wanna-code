package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
        // Initialize the bitmap list with a basic bitmap
        _bitmapList.value = mutableListOf(
            Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
        )

        // Select this first drawing (later will be selected on splash screen probably)
        _currentDrawing.value = 0

        // Default values for pen color and size
        penColor.value = Color.BLACK
        penSize.value = 5.0f
    }

    // Select a drawing if possible
    fun selectDrawing(id: Int) {
        _bitmapList.value?.let {
            if (id >= 0 && id < it.count()) {
                _currentDrawing.value = id
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
        _bitmapList.value?.add(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888))
    }

    // Remove a bitmap
    fun removeBitmap(id: Int) {
        _bitmapList.value?.let {
            if (id >= 0 && id < it.count())
                it.removeAt(id)
        }
    }
}