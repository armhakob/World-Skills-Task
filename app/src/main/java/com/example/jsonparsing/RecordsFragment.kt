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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException


@RequiresApi(Build.VERSION_CODES.S)
class RecordsFragment : Fragment(R.layout.fragment_records){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_records, container, false)
    }

    private lateinit var btnRecord: Button
    private lateinit var btnStopRecording: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var audioAdapter: AudioAdapter
    private val audioList = mutableListOf<String>()
    private lateinit var output: String
    private var mediaRecorder: MediaRecorder? = null

//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnRecord = view.findViewById(R.id.btnRecord)
        btnStopRecording = view.findViewById(R.id.btnStopRecording)
        recyclerView = view.findViewById(R.id.recyclerView)

        audioAdapter = AudioAdapter(audioList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = audioAdapter

//        output = Environment.getExternalStorageDirectory().absolutePath + "/recording.3gp"
        output = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/recording.mp3"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        mediaRecorder = MediaRecorder(view.context)
    } else {
        mediaRecorder = MediaRecorder()
    }
    mediaRecorder?.let {
        it.setAudioSource(MediaRecorder.AudioSource.MIC)
        it.setAudioSamplingRate(44100)
        it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        it.setOutputFile(output)
        it.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }
//        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        mediaRecorder?.setOutputFile(output)


        btnRecord.setOnClickListener {
            startRecording()
            Log.d("MediaRecorder", "Gtel es!!!!")
        }

        btnStopRecording.setOnClickListener {
            stopRecording()
            addRecordingToList(output)
        }

//        ActivityCompat.requestPermissions(requireContext(), arrayOf(
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ), 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecording() {
        Log.d("MediaRecorder", "Mtnumaaa!!!!")
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            Toast.makeText(requireContext(), "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            Log.d("MediaRecorder", "mtnuma? ${e.message}")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.d("MediaRecorder", "mtnuma?IO: ${e.message}")
            e.printStackTrace()
        }
        btnRecord.isEnabled = true
        btnStopRecording.isEnabled = true
    }

    private fun stopRecording() {
        mediaRecorder.apply {
            Log.d("Stop: ", "??????")
            try {
                mediaRecorder?.stop()
                mediaRecorder?.reset()
//                release()
            }catch (e: IllegalStateException){
                Log.e("StopMediaRecorder","error on stop", e)
            }

        }
    }

    private fun addRecordingToList(filePath: String) {
        audioList.add(filePath)
        audioAdapter.notifyItemInserted(audioList.size - 1)
    }
}