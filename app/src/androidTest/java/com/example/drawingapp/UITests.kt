package com.example.drawingapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.ViewActions.*
import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.After


import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UITests {
    private lateinit var testContext: Context
    private lateinit var testScope: CoroutineScope
    private lateinit var drawingDao: DrawingDAO
    private lateinit var db: DrawingDatabase
    private lateinit var repository: DrawingRepository

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
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkIfButtonExists() {
        composeTestRule.onNodeWithText("New Drawing").assertIsDisplayed()
    }
    @Test
    //It does go to the next page but crashes because bitmap is null
    fun clickButton(){
        composeTestRule.onNodeWithText("New Drawing").performClick().assert(hasText("New Drawing"))
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.drawingapp", appContext.packageName)
    }
}