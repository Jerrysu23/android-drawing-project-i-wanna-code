package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentMainPageBinding

class MainPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentMainPageBinding.inflate(layoutInflater, container, false)
        binding.newDrawingButton.setOnClickListener{
            findNavController().navigate(R.id.makeNewDrawing)
        }
        return binding.root
    }


}