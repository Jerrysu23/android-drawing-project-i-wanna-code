package com.example.drawingapp
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

class DrawingRepository(private val scope : CoroutineScope, private val dao: DrawingDAO, private val context: Context) {
    fun allDrawings() : Flow<List<Bitmap>> {

       var bitmaps: Flow<List<Bitmap>> = dao.allDrawings().map {
           var bitmapList : ArrayList<Bitmap> = ArrayList()
           for(x in it){
               var bitmap = BitmapFactory.decodeFile(context?.filesDir.toString() + "/" + (x-1) + ".png").copy(Bitmap.Config.ARGB_8888, true)
               Log.i("drawing", "$bitmap")
               bitmapList.add(bitmap)
           }
           return@map bitmapList.toList()

       }
        return bitmaps
    }
    fun addDrawing() : Long = runBlocking {

            val id = dao.addDrawing(Drawing())
            Log.i("addDrawing", "$id path: ${context.filesDir.toString() + "/" + (id-1) + ".png"}")
            val dir = File(context.filesDir.toString() + "/" + (id-1) + ".png")
            val fOut = FileOutputStream(dir)
            val bmp = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
            bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut)
            fOut.flush()
            fOut.close()
        id-1



    }
    fun updateDrawing(bitmap: Bitmap, id: Long){
    scope.launch {
        val dir = File(context.filesDir.toString() + "/" + dao.getCurrentDrawing(id) + ".png")
        val fOut = FileOutputStream(dir)
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
    }

    }
     fun getCurrentDrawing(Id: Long): Bitmap = runBlocking{
        Log.i("getDrawing", "${dao.getCurrentDrawing(Id)} ")
         BitmapFactory.decodeFile(context?.filesDir.toString() + "/" + dao.getCurrentDrawing(Id) + ".png").copy(Bitmap.Config.ARGB_8888, true)
    }
}