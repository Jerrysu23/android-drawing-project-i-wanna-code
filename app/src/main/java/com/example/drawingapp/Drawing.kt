package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Base64.DEFAULT
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.nio.ByteBuffer


@Entity(tableName = "drawing")
data class Drawing( @PrimaryKey(autoGenerate = true) var id: Int = 0) {

}