package com.example.drawingapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainPageFragment : Fragment() {
    private val viewModel : DrawingViewModel by activityViewModels()
    private lateinit var adapter: DrawingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_page, container, false)

        // Add listener for the drawing button
        view.findViewById<Button>(R.id.newDrawingButton).setOnClickListener {
            viewModel.addBitmap()
            findNavController().navigate(R.id.makeNewDrawing)
        }

        // Create and set up the RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapter = DrawingAdapter(viewModel.bitmapList.value.orEmpty()) { id ->
            Log.d("MainPageFragment", "Clicked a bitmap! ${id})")
            viewModel.selectDrawing(id)
            findNavController().navigate(R.id.makeNewDrawing)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the ViewModel and update the adapter
        viewModel.bitmapList.observe(viewLifecycleOwner, Observer { bitmaps ->
            adapter.setBitmaps(bitmaps)
        })

        return view
    }
}