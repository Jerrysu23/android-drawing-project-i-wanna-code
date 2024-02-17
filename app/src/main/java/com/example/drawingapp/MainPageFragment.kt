package com.example.drawingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class MainPageFragment : Fragment() {
    private val viewModel : DrawingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val binding = FragmentMainPageBinding.inflate(layoutInflater, container, false)
//        binding.newDrawingButton.setOnClickListener{
//            myViewModel.addBitmap()
//            findNavController().navigate(R.id.makeNewDrawing)
//        }
//        return binding.root

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)

        // Add listener for the drawing button
        view.findViewById<Button>(R.id.newDrawingButton).setOnClickListener {
            viewModel.addBitmap()
            findNavController().navigate(R.id.makeNewDrawing)
        }

        return view
    }


}