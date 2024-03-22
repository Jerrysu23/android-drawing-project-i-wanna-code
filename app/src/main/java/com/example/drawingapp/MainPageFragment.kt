package com.example.drawingapp

import android.graphics.Bitmap
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class MainPageFragment : Fragment() {
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
                MainPageContent()
            }
        }
    }

    @Composable
    fun MainPageContent() {
        Column {
            Button(
                onClick = {
                    lifecycleScope.launch{
                        viewModel.addDrawing()
                        val dir = File(context?.filesDir.toString() + "/" + viewModel.dbCurrentDrawing + ".png")
                        Log.i("context?.filesDir.toString() + viewModel.dbCurrentDrawing", "file path is: " + context?.filesDir.toString() + viewModel.dbCurrentDrawing)
                        val fOut = FileOutputStream(dir)
                        val bmp = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
                        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut)
                        fOut.flush()
                        fOut.close()
                        findNavController().navigate(R.id.makeNewDrawing)
                }},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("New Drawing")
            }
            val list by viewModel.drawings.collectAsState(listOf())
            DrawingList(list, context) { id ->
                Log.i("id", "id is: $id")
                lifecycleScope.launch {
                    viewModel.getCurrentDrawing(id)
                    findNavController().navigate(R.id.makeNewDrawing)
                }

            }
        }
    }
}