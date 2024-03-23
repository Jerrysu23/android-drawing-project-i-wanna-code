package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
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
}
