package com.example.jsonparsing

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import java.io.OutputStream
import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.File
import java.io.FileOutputStream

class TicketDetailsFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ticket_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString("type") ?: ""
        val name = arguments?.getString("name") ?: ""
        val imgUri = arguments?.getString("imageUri") ?: ""
        val currentDate = arguments?.getString("currentDate") ?: ""
        val seat = arguments?.getString("seat") ?: ""

        view.findViewById<TextView>(R.id.ticketTypeTextView).text = type
        view.findViewById<TextView>(R.id.ticketNameTextView).text = name
        view.findViewById<TextView>(R.id.ticketTimeTextView).text = currentDate
        view.findViewById<TextView>(R.id.ticketSeatTextView).text = type
        view.findViewById<ImageView>(R.id.ticketImageView).load(imgUri){
            Log.d("B:", "image")
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }

        val viewSS = view.findViewById<ConstraintLayout>(R.id.viewSS)
        view.findViewById<Button>(R.id.downloadButton).setOnClickListener{
            val bitmap = getScreenShotFromView(viewSS)
            if (bitmap != null) {
                saveMediaToStorage(bitmap)
            }
        }
    }

    companion object {
        fun newInstance(resultModel: Ticket): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle().apply {
                putString("type", resultModel.type)
                putString("name", resultModel.name)
                putString("imageUri", resultModel.imageUri)
                putString("currentDate", resultModel.currentDate)
                putString("seat", resultModel.seatNumber)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private fun getScreenShotFromView(v: View): Bitmap? {
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GFG", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireContext().contentResolver?.also { resolver ->

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext() , "Captured View and saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }
}