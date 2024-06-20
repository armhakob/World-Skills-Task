package com.example.jsonparsing

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation

class DetailFragment : Fragment(R.layout.event_details) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id") ?: -1
        val title = arguments?.getString("title") ?: ""
        val text = arguments?.getString("text") ?: ""
        //var isOpened = arguments?.getBoolean("isOpened") ?: false
        var viewCount = arguments?.getInt("viewCount") ?: -1
        val moreImages = arguments?.getStringArrayList("imageArray")
        Log.d("array", moreImages.toString())
        if (id != -1) {
            view.findViewById<TextView>(R.id.ed_text).text = text
            view.findViewById<TextView>(R.id.ed_title).text = title
            view.findViewById<TextView>(R.id.ed_vcount).text = viewCount.toString()
            view.findViewById<ImageView>(R.id.ed_img1).load(moreImages?.getOrNull(0)){
                Log.d("B:", "image")
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
            view.findViewById<ImageView>(R.id.ed_img2).load(moreImages?.getOrNull(1)){
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
            view.findViewById<ImageView>(R.id.ed_img3).load(moreImages?.getOrNull(2)){
                Log.d("img", moreImages?.getOrNull(2).toString())
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
        }
        view.findViewById<ImageView>(R.id.ed_img1).setOnClickListener{
            openZoomImage(moreImages?.getOrNull(0).toString())
        }
        view.findViewById<ImageView>(R.id.ed_img2).setOnClickListener{
            openZoomImage(moreImages?.getOrNull(1).toString())
        }
        view.findViewById<ImageView>(R.id.ed_img3).setOnClickListener{
            openZoomImage(moreImages?.getOrNull(2).toString())
        }
    }

    companion object {
        fun newInstance(resultModel: ResultModelClass): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle().apply {
                putInt("id", resultModel.id)
                putString("title", resultModel.title)
                putString("text", resultModel.text)
                putInt("viewCount", resultModel.viewCount)
                putBoolean("isOpened", resultModel.isOpened)
                putStringArrayList("imageArray", resultModel.imageArray)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private fun openZoomImage(url: String) {
        val fragment = ZoomImageFragment.newInstanceForZoom(url)
        parentFragmentManager.beginTransaction()
            .add(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

}
