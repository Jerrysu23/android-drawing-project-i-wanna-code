package com.example.drawingapp

import android.graphics.Color
import androidx.lifecycle.Observer
import org.junit.Test
import androidx.lifecycle.testing.TestLifecycleOwner
import org.junit.Assert.*

class ViewModelTests {

    val vm = DrawingViewModel()
    val lifecycleOwner = TestLifecycleOwner()

    @Test
    fun testObservePenColorChange() {
        var callbackFired = false
        vm.penColor.observe(lifecycleOwner) { callbackFired = true }

        vm.penColor.postValue(Color.WHITE)

        assertTrue(callbackFired)
    }

    @Test
    fun testObservePenSizeChange() {
        var callbackFired = false
        vm.penSize.observe(lifecycleOwner) { callbackFired = true }

        vm.penSize.postValue(10.0f)

        assertTrue(callbackFired)
    }
}