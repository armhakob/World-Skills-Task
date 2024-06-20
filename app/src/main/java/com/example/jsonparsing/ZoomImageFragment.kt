package com.example.jsonparsing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation

class ZoomImageFragment : Fragment(R.layout.zoom_img) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.zoom_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.getString("url") ?: ""
        view.findViewById<ImageView>(R.id.image).load(url){
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            transformations(CircleCropTransformation())
        }

    }

    companion object {
        fun newInstanceForZoom(url: String): ZoomImageFragment{
            val fragment = ZoomImageFragment()
            val args = Bundle()
            args.putString("url", url)
            fragment.arguments = args
            return fragment
        }
    }
}