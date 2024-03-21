package com.example.drawingapp

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DrawingList(bitmaps: List<Bitmap>, onItemClick: (Int) -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        bitmaps.forEachIndexed { index, bitmap ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onItemClick(index) }
            ) {
                Image(
                    bitmap.asImageBitmap(),
                    "Drawing $index",
                    modifier = Modifier
                        .size(100.dp)
                        .border(BorderStroke(1.dp, Color.Black))
                )
                Text("Drawing $index", textAlign = TextAlign.Center, modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
                )
            }
        }
    }
}