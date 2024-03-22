package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.set
import androidx.lifecycle.Observer
import org.junit.Test
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.junit.Assert.*
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@RunWith(AndroidJUnit4::class)
class ViewModelTests {
    /*
    @Test
    fun testObservePenColor() {
        val vm = DrawingViewModel()
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()//Lifecycle.State.CREATED,this.coroutineContext)
            val before = vm.penColor.value!!
            var callbackFired = false

            //We'll cover this later in the course
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {

                    // Actual test code stuff happening here!

                    vm.penColor.observe(lifecycleOwner) {
                        callbackFired = true
                    }
                    vm.penColor.postValue(Color.WHITE)
                    assertTrue(callbackFired)

                    assertNotSame(before, vm.penColor.value!!)

                    // End actual test stuff
                }
            }
        }
    }

    @Test
    fun testObservePenSize() {
        val vm = DrawingViewModel()
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()//Lifecycle.State.CREATED,this.coroutineContext)
            val before = vm.penSize.value!!
            var callbackFired = false

            //We'll cover this later in the course
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {

                    // Actual test code stuff happening here!

                    vm.penSize.observe(lifecycleOwner) {
                        callbackFired = true
                    }
                    vm.penSize.postValue(10.0f)
                    assertTrue(callbackFired)

                    assertNotSame(before, vm.penSize.value!!)

                    // End actual test stuff
                }
            }
        }
    }

    @Test
    fun testIndexAutomaticSetting() {
        val vm = DrawingViewModel()
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    // Ensure it starts at 0 because we start with an empty drawing
                    assertEquals(0, vm.currentDrawing.value)

                    // Add two drawings and ensure it is 2
                    vm.addBitmap()
                    vm.addBitmap()
                    delay(100) // runBlocking doesn't seem to work unless we manually pop in a delay
                    assertEquals(2, vm.currentDrawing.value)

                    // Select drawing #1 and test
                    vm.selectDrawing(1)
                    delay(100)
                    assertEquals(1, vm.currentDrawing.value)

                    // Delete drawing #2, ensure drawing #1 is still selected
                    vm.removeBitmap(2)
                    delay(100)
                    assertEquals(1, vm.currentDrawing.value)

                    // Delete drawing #1, ensure we select #0
                    vm.removeBitmap(1)
                    delay(100)
                    assertEquals(0, vm.currentDrawing.value)

                    // Delete drawing #0, ensure it is now null
                    vm.removeBitmap(0)
                    delay(100)
                    assertEquals(null, vm.currentDrawing.value)
                }
            }
        }
    }

    @Test
    fun testBitmapUpdates() {
        val vm = DrawingViewModel()
        runBlocking {
            val lifecycleOwner = TestLifecycleOwner()
            val before = vm.getCurrentBitmap()

            lifecycleOwner.run {
                withContext(Dispatchers.Main) {
                    // First do no change and make sure they're still the same
                    val newBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                    vm.updateCurrentBitmap(newBitmap)

                    delay(100)
                    assertTrue(before!!.sameAs(vm.getCurrentBitmap())) // We can't use .equals or == for bitmaps

                    // Now update one pixel and make sure they're different
                    newBitmap[400, 400] = Color.BLACK
                    vm.updateCurrentBitmap(newBitmap)

                    delay(100)
                    assertFalse(before.sameAs(vm.getCurrentBitmap()))
                }
            }
        }
    }
    */
}
