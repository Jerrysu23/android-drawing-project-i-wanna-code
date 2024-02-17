package com.example.drawingapp

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DrawingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(bitmap: Bitmap, index: Int) {
        itemView.findViewById<ImageView>(R.id.image_drawing_preview).setImageBitmap(bitmap)
        itemView.findViewById<TextView>(R.id.text_drawing_name).text = "Drawing $index"
    }
}