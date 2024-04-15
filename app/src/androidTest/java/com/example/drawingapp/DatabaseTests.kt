package com.example.drawingapp

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTests {
    private lateinit var dao: DrawingDAO;
    private lateinit var db: DrawingDatabase;

    @Before
    fun setUpDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DrawingDatabase::class.java).build()
        dao = db.drawingDao()
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    @Test
    fun testAddDrawing() {
        runBlocking {
            // Add a drawing to the database
            val id = dao.addDrawing(Drawing())

            // Make sure it's in the list of drawings
            val drawings = dao.allDrawings().first()
            assert(drawings.contains(id))
        }
    }
}