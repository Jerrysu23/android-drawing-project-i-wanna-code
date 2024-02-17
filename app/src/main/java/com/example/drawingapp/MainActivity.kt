package com.example.drawingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: DrawingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw splash screen and start
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        // Set up ViewModel
        viewModel = ViewModelProvider(this)[DrawingViewModel::class.java]
    }
}