package com.example.drawingapp

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DrawingAdapter(private var bitmaps: MutableList<Bitmap>, private val onItemClick: (Bitmap) -> Unit) : RecyclerView.Adapter<DrawingViewHolder>() {
    // Adaptively set the drawings
    fun setBitmaps(newBitmaps: MutableList<Bitmap>) {
        bitmaps = newBitmaps
    }

    // Create items from the template
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.drawing_item, parent, false)
        return DrawingViewHolder(view)
    }

    // Bind the contact names to the items in the RecyclerView
    override fun onBindViewHolder(holder: DrawingViewHolder, position: Int) {
        val bitmap = bitmaps[position]
        holder.bind(bitmap, position + 1)
        holder.itemView.setOnClickListener{ onItemClick(bitmap) }
    }

    override fun getItemCount(): Int = bitmaps.size
}