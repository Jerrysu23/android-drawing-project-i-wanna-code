package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentMainPageBinding

class MainPageFragment : Fragment() {
    private val myViewModel : DrawingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMainPageBinding.inflate(layoutInflater, container, false)
        binding.newDrawingButton.setOnClickListener{
            myViewModel.addBitmap()
            findNavController().navigate(R.id.makeNewDrawing)
        }
        return binding.root
    }


}