package com.example.drawingapp

import android.graphics.Paint
import androidx.compose.ui.test.assert
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.action.ViewActions.*
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.testing.TestLifecycleOwner
import io.ktor.util.Identity.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ToolBarTests {
    @get: Rule
    val ran_composeTest = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNewFileButtonDisplayed() {
        ran_composeTest.onNodeWithText("New File").assertExists()
        ran_composeTest.onNodeWithText("New File").assertIsDisplayed()
    }

    @Test
    fun testNewFileButtonPopupDisplayed() {
        ran_composeTest.onNodeWithText("Name File").assertIsNotDisplayed()
        ran_composeTest.onNodeWithText("New File").performClick()
        ran_composeTest.onNodeWithText("Name File").assertIsDisplayed()
    }

    @Test
    fun testNewFileTransition() {
        ran_composeTest.onNodeWithText("New File").performClick()
        ran_composeTest.onNodeWithTag("TextField").performTextInput("abcdefg")
        ran_composeTest.onNodeWithTag("ConfirmButton").performClick()



    }

    fun clickButton(){
        ran_composeTest.onNodeWithText("New Drawing").performClick().assert(hasText("New Drawing"))
    }


    private lateinit var repo: DrawingRepository
    @Test
    fun penSize_test() {

        val vm = DrawingViewModel(repo)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val before = vm.penSize.value!!
            var callbackFired = false

            // Run with the given lifecycle
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    // Note any changes
                    vm.penSize.observe(lifecycleOwner) {
                        callbackFired = true
                    }

                    // Update the pen color
                    vm.penSize.postValue(10f)

                    // Ensure it was changed
                    assertTrue(callbackFired)
                    assertNotSame(before, vm.penSize.value)
                }
            }

        }
    }

    @Test
    fun tool_test() {
        val vm = DrawingViewModel(repo)
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val before = vm.penSize.value!!
            var callbackFired = false
            var num = 0
            lifecycleOwner.run { withContext(Dispatchers.Main){
                vm.penShape.observe(lifecycleOwner){
                    callbackFired = true
                    num++
                }
                assertEquals(1, Paint.Cap.ROUND)
                assertEquals(2, Paint.Cap.SQUARE)

            } }
        }
    }
}