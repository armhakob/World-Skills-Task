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

class ResultAdapter(val context: Context, val items: ArrayList<ResultModelClass>) :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>(){

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

        val item = items.get(position)

        holder.tvId.text = item.id.toString()
        holder.tvTitle.text = item.title
        holder.tvText.text = item.text
        holder.tvImage.load(item.imgUrl){
//            Log.d("A:", "image")
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val tvId = view.findViewById<TextView>(R.id.tv_id)
        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val tvText = view.findViewById<TextView>(R.id.tv_text)
        val tvImage = view.findViewById<ImageView>(R.id.tv_img)
    }
}