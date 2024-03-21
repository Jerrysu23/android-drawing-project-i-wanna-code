package com.example.drawingapp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun DrawingList(bitmaps: List<Bitmap>, onItemClick: (Int) -> Unit) {
    Column {
        bitmaps.forEachIndexed { index, bitmap ->
            Image(
                bitmap.asImageBitmap(),
                "Drawing $index",
                modifier = Modifier.clickable { onItemClick(index) }.padding(8.dp)
            )
            Text("Drawing $index", Modifier.padding(8.dp))
        }
    }
}