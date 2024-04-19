package com.example.drawingapp

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "drawing")
data class Drawing(var filename: String,  @PrimaryKey(autoGenerate = true) var id: Int = 0) {

}

data class FileAndBitmap(var filename: String, var bitmap: Bitmap, var owner: String = ""){
    var name = filename
    var drawing = bitmap
    var creator = owner
}