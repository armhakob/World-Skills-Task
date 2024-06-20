package com.example.jsonparsing

import android.Manifest
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException


class RecordsFragment : Fragment(R.layout.fragment_records){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var btnRecord: Button
    private lateinit var btnStopRecording: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    private val audioList = mutableListOf<String>()
    private var outputFile: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRecord = view.findViewById(R.id.btnRecord)
        btnStopRecording = view.findViewById(R.id.btnStopRecording)
        recyclerView = view.findViewById(R.id.recyclerView)

        audioAdapter = AudioAdapter(audioList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = audioAdapter

        btnRecord.setOnClickListener {
            startRecording()
        }

        btnStopRecording.setOnClickListener {
            stopRecording()
            addRecordingToList(outputFile)
        }

//        ActivityCompat.requestPermissions(requireContext(), arrayOf(
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ), 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecording() {
        //outputFile = "${externalCharDir?.absolutePath}/audiorecord_${System.currentTimeMillis()}.3gp"
        val externalStorageState = Environment.getExternalStorageState()
        if (externalStorageState == Environment.MEDIA_MOUNTED) {
            val externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            outputFile = File(externalDir, "audiorecord_${System.currentTimeMillis()}.3gp").toString()
            Log.d("Audio: ", outputFile)
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(outputFile)

                try {
                    prepare()
                    start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            btnRecord.isEnabled = false
            btnStopRecording.isEnabled = true
        } else {
            Log.d("Audio File:" ,"external storage is not available")
        }
    }

    private fun stopRecording() {
        mediaRecorder.apply {
            Log.d("Stop: ", "??????")
            try {
                stop()
                release()
            }catch (e: IllegalStateException){
                Log.e("StopMediaRecorder", "Error while stopping MediaRecorder: ${e.message}")
            }

        }
        btnRecord.isEnabled = true
        btnStopRecording.isEnabled = false
    }

    private fun addRecordingToList(filePath: String) {
        audioList.add(filePath)
        audioAdapter.notifyItemInserted(audioList.size - 1)
    }
}