package com.example.jsonparsing

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import java.util.Locale

class ResultAdapter(private val context: Context, private val items: ArrayList<ResultModelClass>, val onClick: (item: ResultModelClass) -> Unit) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>(){

        private var filteredItems: List<ResultModelClass> = items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
//                R.layout.item_layout,
                R.layout.layout_tem,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = filteredItems[position]
        println("VR: item: ${item.viewCount}")
        println("VR: item: ${item}")
        holder.tvId.text = item.id.toString()
        holder.tvTitle.text = item.title
        holder.tvText.text = item.text
        holder.tvImage.load(item.imageUrl){
            Log.d("A:", "image")
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }
        holder.itemView.setOnClickListener {
            onClick(item)
        }
        holder.tvRead.text = if (item.isOpened) {
            "Read"
        } else {
            "Unread"
        }
    }

    fun mapping(q: String): Boolean {
        if (q.toLowerCase(Locale.US).equals("read")) {
            return true
        } else {
            return false
        }
    }
    fun filter(query: String) {
        val read = mapping(query)
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter {
                it.isOpened == read
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredItems.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvId = view.findViewById<TextView>(R.id.tv_id)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvText = view.findViewById<TextView>(R.id.tv_text)
        val tvImage = view.findViewById<ImageView>(R.id.tv_img)
        val tvRead = view.findViewById<TextView>(R.id.tv_isOpened)
    }
}