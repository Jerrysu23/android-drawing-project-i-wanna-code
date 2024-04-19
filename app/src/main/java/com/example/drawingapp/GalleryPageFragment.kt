package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class GalleryPageFragment : Fragment() {
    private val viewModel : DrawingViewModel by activityViewModels(){
        DrawingViewModelFactory((activity?.application as DrawingApplication).drawingRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                GalleryPageContent()
            }
        }
    }

    @Composable
    fun GalleryPageContent() {
        Column {
            // View Gallery button
            Button(
                onClick = {
                    findNavController().navigate(R.id.closeGallery)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }

            // List
            GalleryList(context)
        }
    }
}