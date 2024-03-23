package com.example.drawingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private val viewModel: DrawingViewModel by viewModels(){
        DrawingViewModelFactory((application as DrawingApplication).drawingRepository)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw splash screen and start
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_main)


        // Set up ViewModel
    }
}