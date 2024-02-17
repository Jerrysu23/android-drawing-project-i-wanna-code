package com.example.drawingapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.drawingapp.databinding.FragmentDrawingPageBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DrawingPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DrawingPageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var drawingView: DrawingView
    private val viewModel: DrawingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility") // The app cannot really support this accessibility feature
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDrawingPageBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drawing_page, container, false)
        drawingView = binding.drawingView

        // Default values for the drawing view
        drawingView.setPenColor(Color.BLACK)
        drawingView.setPenSize(5.0f)
        val penSizeEditor = binding.penSize
        penSizeEditor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawingView.setPenSize(progress.toFloat())
                binding.penSizeText.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        binding.homeButton.setOnClickListener{
            findNavController().popBackStack()
        }

        // Set up drawing view touch listener
        drawingView.setOnTouchListener { _, event -> handleTouchEvent(event) }
        // Observe changes in the ViewModel and pass to the drawing view
        viewModel.penColor.observe(viewLifecycleOwner, Observer { drawingView.setPenColor(it) })
        viewModel.penSize.observe(viewLifecycleOwner, Observer { drawingView.setPenSize(it) })
        viewModel.currentDrawing.observe(viewLifecycleOwner, Observer { resetBitmap() })

        // Set the DrawingView's bitmap
        resetBitmap()

        return binding.root
    }

    // Handle touch events for drawing
    private fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawingView.startTouch(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                drawingView.continueTouch(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                drawingView.stopTouch()
            }
        }
        return true
    }

    // Reset the DrawingView's bitmap
    private fun resetBitmap() {
        drawingView.setBitmap(viewModel.getCurrentBitmap()) {
            // Update the ViewModel when something is drawn
            viewModel.updateCurrentBitmap(it ?: Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888))
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DrawingPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DrawingPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}