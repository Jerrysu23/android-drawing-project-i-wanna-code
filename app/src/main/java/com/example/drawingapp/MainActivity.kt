package com.example.drawingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private val viewModel: DrawingViewModel by viewModels(){
        DrawingViewModelFactory((application as DrawingApplication).drawingRepository)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Firebase authentication
        auth = Firebase.auth

        // Draw splash screen and start
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_main)


        // Set up ViewModel
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            viewModel.loggedIn.postValue(true)
            viewModel.loggedInEmail.postValue(currentUser.email)
        }
    }
}