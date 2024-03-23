package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.get
import androidx.core.graphics.set
import androidx.lifecycle.Observer
import org.junit.Test
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ViewModelTests {
    private lateinit var testContext: Context
    private lateinit var testScope: CoroutineScope
    private lateinit var drawingDao: DrawingDAO
    private lateinit var db: DrawingDatabase
    private lateinit var repository: DrawingRepository

    // Set up these required variables
    @Before
    fun createDb() {
        // Get the context and coroutine scope
        testContext = ApplicationProvider.getApplicationContext()
        testScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        // Set up the database and repository
        db = Room.inMemoryDatabaseBuilder(testContext, DrawingDatabase::class.java).build()
        drawingDao = db.drawingDao()
        repository = DrawingRepository(testScope, drawingDao, testContext)
    }

    // Close the database after the tests
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // Ensure pen size is stored correctly
    @Test
    fun observePenSizeChange() {
        val vm = DrawingViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val before = vm.penSize.value!!
            var callbackFired = false

            // Run with the given lifecycle
            lifecycleOwner.run { withContext(Dispatchers.Main) {
                // Note any changes
                vm.penSize.observe(lifecycleOwner) {
                    callbackFired = true
                }

                // Update the pen color
                vm.penSize.postValue(10f)

                // Ensure it was changed
                assertTrue(callbackFired)
                assertNotSame(before, vm.penSize.value)
            }}
        }
    }

    // Ensure pen color is stored correctly
    @Test
    fun observePenColorChange() {
        val vm = DrawingViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val before = vm.penColor.value!!
            var callbackFired = false

            // Run with the given lifecycle
            lifecycleOwner.run { withContext(Dispatchers.Main) {
                // Note any changes
                vm.penColor.observe(lifecycleOwner) {
                    callbackFired = true
                }

                // Update the pen color
                vm.penColor.postValue(Color.WHITE)

                // Ensure it was changed
                assertTrue(callbackFired)
                assertNotSame(before, vm.penColor.value)
            }}
        }
    }

    // Ensure we can add drawings
    @Test
    fun testAddDrawings() {
        val vm = DrawingViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run{ withContext(Dispatchers.Main) {
                /* Begin Test */

                // Add two drawings and get their IDs
                val firstId: Long = vm.addDrawing()
                val secondId: Long = vm.addDrawing()

                // Ensure the IDs are separate
                assertFalse(firstId == secondId)

                /* End Test */
            }}
        }
    }

    // Ensure we can update drawings
    // TODO: Find out why this test is failing but works in production.
    @Test
    fun testUpdateDrawings() {
        val vm = DrawingViewModel(repository)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run{ withContext(Dispatchers.Main) {
                /* Begin Test */

                // Add a drawing
                val firstId = vm.addDrawing()

                // Add a red pixel to the top left and update it
                val firstBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                firstBitmap[0, 0] = Color.RED
                Log.d("TestDrawing 1a", "${checkImageEmpty(firstBitmap)}")
                vm.updateDrawing(firstBitmap, firstId)

                // Add another drawing
                val secondId = vm.addDrawing()

                // Add a blue pixel to the top left and update it
                val secondBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                secondBitmap[0, 0] = Color.BLUE
                Log.d("TestDrawing 2a", "${checkImageEmpty(secondBitmap)}")
                vm.updateDrawing(secondBitmap, firstId)

                Log.d("TestDrawing 2b", "${checkImageEmpty(vm.getCurrentDrawing())}")
                Log.d("TestDrawing 1b", "${checkImageEmpty(vm.getDrawingById(firstId))}")
                Log.d("TestDrawing 2b", "${checkImageEmpty(vm.getDrawingById(secondId))}")

                // Make sure it updated our second drawing
                assertEquals(Color.BLUE, vm.getCurrentDrawing()[0, 0])

                // Make sure it updated our first drawing
                assertEquals(Color.RED, vm.getDrawingById(firstId)[0, 0])

                /* End Test */
            }}
        }
    }

    private fun checkImageEmpty(bitmap: Bitmap) : Boolean {
        for (x in 0..<bitmap.width) {
            for (y in 0..<bitmap.height) {
                if (bitmap[x, y] != 0) {
                    return false
                }
            }
        }
        return true
    }
}
