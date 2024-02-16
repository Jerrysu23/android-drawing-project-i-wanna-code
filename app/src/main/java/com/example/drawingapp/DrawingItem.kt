package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class DrawingItem(var bitmap: Bitmap, var pen: Paint, var pensize: Int)

class DrawingViewModel: ViewModel(){
    private val actualList = MutableLiveData(
        mutableListOf(DrawingItem(Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888), Paint(), pensize = 5)))
    //create a public list that other files can look at
    val observableList = actualList as LiveData<List<DrawingItem>>
    //store the current item to be displayed in the details fragment
    lateinit var currentItem : DrawingItem

    //add new items to the list
    fun newItem(bitmap: Bitmap, pen: Paint, pensize: Int){
        actualList.value!!.add(DrawingItem(bitmap, pen, pensize))
        actualList.value = actualList.value
    }
    fun setCurrentItem(bitmap: Bitmap, pen: Paint, pensize: Int){
        currentItem = DrawingItem(bitmap, pen, pensize)
    }
    fun changePenSize(pensize: Int){
        currentItem.pensize = pensize
    }
    //not sure how to get int value of color yet
    //fun changePenColor(color: Color){
      //  val x = R.color.black
        //currentItem.pen.setColor(100)
    //}
    fun removeItem(item: DrawingItem){
        actualList.value!!.remove(item)
        actualList.value = actualList.value
    }
}