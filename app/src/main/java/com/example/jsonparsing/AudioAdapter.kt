package com.example.jsonparsing

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException

class AudioAdapter(private val audioList: List<String>) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val audioTitle: TextView = itemView.findViewById(R.id.audioTitle)
        val btnPlay: Button = itemView.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.audio_item, parent, false)
        return AudioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFilePath = audioList[position]
        holder.audioTitle.text = "Recording ${position + 1}"
        holder.btnPlay.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(audioFilePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount() = audioList.size
}