package com.example.drawingapp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

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
            var text by remember { mutableStateOf("") }
            Row(modifier = Modifier.fillMaxWidth()){
                TextField(
                    modifier = Modifier.weight(1f),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    label = {Text("File Name")}
                )
            }

            Button(
                onClick = {
                    if(text != "") {
                        Log.i("filename:", text)
                        viewModel.addDrawing(text)
                        findNavController().navigate(R.id.makeNewDrawing)
                    }
                    else{
                        Toast.makeText(this@MainPageFragment.context, "Please enter a file name", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("New Drawing")
            }
            val list by viewModel.drawings.collectAsState(listOf())
            DrawingList(list, context) { id ->
                Log.i("id", "id is: $id")

                    viewModel.getDrawingById(id.toLong())
                    findNavController().navigate(R.id.makeNewDrawing)


            }
        }
    }
}