package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class MainPageFragment : Fragment() {
    private val viewModel : DrawingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MainPageContent()
            }
        }
    }

    @Composable
    fun MainPageContent() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Button(
                onClick = {
                    viewModel.addBitmap()
                    findNavController().navigate(R.id.makeNewDrawing)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("New Drawing")
            }
            DrawingList(viewModel.bitmapList.value.orEmpty()) { id ->
                viewModel.selectDrawing(id)
                findNavController().navigate(R.id.makeNewDrawing)
            }
        }
    }
}