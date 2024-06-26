package com.example.jsonparsing

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class CreateTicketFragment : Fragment() {
    private val REQUEST_CODE = 100
    private lateinit var ticketViewModel: TicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_ticket, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chooseImageButton = view.findViewById<Button>(R.id.chooseImageButton)
        chooseImageButton.setOnClickListener {
            pickImageFromGallery()
        }
        ticketViewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)
        val typeSpinner = view.findViewById<Spinner>(R.id.typeSpinner)
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)

//        val selectedType = typeSpinner.selectedItem.toString()
//        val enteredName: String = nameEditText.text.toString()
//
//        val imageUri = view.findViewById<ImageView>(R.id.previewImageView)?.tag?.toString() ?: ""

        val createButton = view.findViewById<Button>(R.id.createButton)

        createButton.setOnClickListener {
            val selectedType = typeSpinner.selectedItem.toString()
            val enteredName: String = nameEditText.text.toString()
            val imageUri =
                view.findViewById<ImageView>(R.id.previewImageView)?.tag?.toString() ?: ""

            val currentDateTime: LocalDateTime = LocalDateTime.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formattedDateTime: String = currentDateTime.format(formatter)

            val seatFormat = generateSeatFormat()

            nameEditText.clearFocus()
            if (selectedType.isNotBlank() && selectedType.isNotBlank() && imageUri.isNotBlank()) {
                val ticket = Ticket(
                    "",
                    enteredName,
                    imageUri,
                    formattedDateTime,
                    seatFormat
                )
                if(selectedType == "Closing") {
                    ticketViewModel.addCloseTicket(ticket)
                } else {
                    ticketViewModel.addOpenTicket(ticket)
                }
                 //change id
//                ticketViewModel.ticketList.add(ticket)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fill all fields and select an image",
                    Toast.LENGTH_SHORT
                ).show()
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, TicketsFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data

            view?.findViewById<ImageView>(R.id.previewImageView)?.setImageURI(imageUri)
            view?.findViewById<ImageView>(R.id.previewImageView)?.tag = imageUri
        }
    }

    fun generateSeatFormat(): String {
        val randomChar = ('A'..'C').random()  // Random character A, B, or C
        val randomNumber = Random.nextInt(1, 11)  // Random number between 1 and 10

        return "$randomChar$randomNumber Row$randomNumber Column$randomNumber"
    }

}
