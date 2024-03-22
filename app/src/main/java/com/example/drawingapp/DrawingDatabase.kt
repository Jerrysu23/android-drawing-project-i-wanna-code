package com.example.drawingapp
import android.content.Context
import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow

@TypeConverters(Converters::class)
@Database(entities=[Drawing::class], version=1, exportSchema = false)
abstract class DrawingDatabase : RoomDatabase() {
    abstract fun drawingDao() : DrawingDAO

    companion object {
        @Volatile
        private var INSTANCE: DrawingDatabase? = null
        fun getDatabase(context: Context): DrawingDatabase{
            return INSTANCE ?: synchronized(this){
                if(INSTANCE != null) return INSTANCE!!
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawingDatabase::class.java,
                    "drawing_database"
                ).build()
                INSTANCE = instance
                return instance
                instance
            }
        }
    }

}
@Dao
interface DrawingDAO{
    @Insert
    suspend fun addDrawing(data : Drawing)

    @Query("SELECT bitmap from drawing")
    fun allDrawings() : Flow<List<Bitmap>>

    @Query("SELECT * from drawing where id = :id")
    fun getCurrentDrawing(id: Int) : Drawing

    @Query("UPDATE drawing SET bitmap = :bitmap WHERE id=:id")
    fun updateBitmap(bitmap : Bitmap, id : Int)
}
