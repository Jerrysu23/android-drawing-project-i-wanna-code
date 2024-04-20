package com.example.drawingapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentDrawingPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog
import java.io.ByteArrayOutputStream

class Wrappers {
    external fun blur(bitmap : Bitmap)
    external fun invertColors(bitmap: Bitmap)
}

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
    private val viewModel : DrawingViewModel by activityViewModels(){
        DrawingViewModelFactory((activity?.application as DrawingApplication).drawingRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility") // The app cannot really support this accessibility feature
    override fun onResume() {
        super.onResume()
        resetBitmap()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDrawingPageBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drawing_page, container, false)
        drawingView = binding.drawingView
        var user = Firebase.auth.currentUser
        val wrapper = Wrappers()

        binding.blurButt.setOnClickListener{
            wrapper.blur(viewModel.dbCurrentDrawing)
            drawingView.stopTouch()
            resetBitmap()
        }
        binding.invertButt.setOnClickListener{
            wrapper.invertColors(viewModel.dbCurrentDrawing)
            drawingView.stopTouch()
            resetBitmap()
        }
        binding.saveButt.setOnClickListener{

            if(user != null){
                Toast.makeText(this@DrawingPageFragment.context, "Uploading drawing to gallery", Toast.LENGTH_LONG).show()
                val baos = ByteArrayOutputStream()
                viewModel.dbCurrentDrawing.compress(Bitmap.CompressFormat.PNG, 0, baos)
                val data = baos.toByteArray()
                val ref = Firebase.storage.reference
                val fileRef = ref.child("${user!!.uid}/${viewModel.getFileName(viewModel.dbCurrentId)}")
                var uploadTask = fileRef.putBytes(data)
                uploadTask.addOnFailureListener{e -> Log.e("Pic upload", "Failed !$e")}
                    .addOnSuccessListener { Log.e("Pic upload", "Success!") }
                val db = Firebase.firestore
                var drawinglist = ArrayList<String>()
                val collection = db.collection("users").document("${user!!.uid}")
                collection.get().addOnSuccessListener { result ->
                    if(result.data?.get("drawings") != null){

                        drawinglist = result.data?.get("drawings") as ArrayList<String>
                        Log.e("drawinglist", drawinglist.toString())


                    }
                    var image = "gs://cs4530-drawing-app.appspot.com/${user!!.uid}/${viewModel.getFileName(viewModel.dbCurrentId)}"
                    if(!drawinglist.contains(image)){
                        drawinglist.add(image)
                    }
                    Log.e("drawinglist", drawinglist.toString())
                    val document = mapOf("drawings" to drawinglist, "email" to user!!.email, "name" to user!!.displayName)
                    db.collection("users/").document("${user!!.uid}")
                        .set(document).addOnSuccessListener {
                            Log.e("Upload", "Success")
                        }.addOnFailureListener{
                            Log.e("Upload", "Failed")
                        }

                }
                    .addOnFailureListener{
                        Log.e("Error", "error getting file")
                    }

            }
            else{
                Toast.makeText(this@DrawingPageFragment.context, "You need to be signed in to upload a drawing", Toast.LENGTH_LONG).show()
            }

        }
        // Default values for the drawing view
        drawingView.setPenColor(Color.BLACK)
        drawingView.setPenSize(5.0f)

        binding.colorButt.setOnClickListener{
            val colorPicker: ColorPickerDialog = ColorPickerDialog.Builder()

                // Set initial (default) color
                .setInitialColor(Color.BLACK)

                // Set Color Model, can be ARGB, RGB, AHSV or HSV
                .setColorModel(ColorModel.RGB)

                // Set is user be able to switch color model
                .setColorModelSwitchEnabled(true)

                // Set your localized string resource for OK button
                .setButtonOkText(android.R.string.ok)

                // Set your localized string resource for Cancel button
                .setButtonCancelText(android.R.string.cancel)

                // Callback for picked color (required)
                .onColorSelected { color: Int ->
                    viewModel.penColor.postValue(color)
                }

                // Create dialog
                .create()
            colorPicker.show(childFragmentManager, "color_picker")
        }
        binding.eraserButt.setOnClickListener{
            viewModel.penColor.postValue(Color.WHITE)
        }
        binding.penShapeButt.setOnClickListener{
            viewModel.penShape.postValue(Paint.Cap.ROUND)
        }
        binding.squareShapeButt.setOnClickListener{
            viewModel.penShape.postValue(Paint.Cap.SQUARE)
        }

        val penSizeEditor = binding.penSize
        penSizeEditor.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.penSize.postValue(progress.toFloat())
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
        viewModel.penShape.observe(viewLifecycleOwner, Observer { drawingView.setPenShape(it) })
        viewModel.getDrawingById(viewModel.dbCurrentId)


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
        drawingView.setBitmap(viewModel.dbCurrentDrawing) {
            // Update the ViewModel when something is drawn
            it?.let{ viewModel.updateDrawing(it, viewModel.dbCurrentId) }
        }
    }

    companion object {
        init{
            System.loadLibrary("drawingapp")
        }
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