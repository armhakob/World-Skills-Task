package com.example.jsonparsing

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class EventsFragment(context: Context) : Fragment(R.layout.fragment_events) {
    var resultList: ArrayList<ResultModelClass>? = null

    init {
        val jsonString = loadJSONFromAsset(context, "example.json")
        jsonString?.let {
            resultList = parseJsonToResultList(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultList?.let { list ->
            val rvResultList = view.findViewById<RecyclerView>(R.id.rvResultList)
            rvResultList.layoutManager = LinearLayoutManager(requireContext())
            val itemAdapter = ResultAdapter(requireContext(), list) { item ->
                item.isOpened = true
                item.viewCount++
                rvResultList.adapter?.notifyItemChanged(item.id)
                openDetailFragment(item)
            }
            rvResultList.adapter = itemAdapter

            val searchView = view.findViewById<SearchView>(R.id.searchView)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    itemAdapter.filter(newText.orEmpty())
                    rvResultList.adapter?.notifyDataSetChanged()
                    return true
                }
            })
        }

    }

    private fun openDetailFragment(item: ResultModelClass) {
        val fragment = DetailFragment.newInstance(item)
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var jsonString: String? = null
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            jsonString = stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return jsonString
    }

    private fun parseJsonToResultList(jsonString: String?): ArrayList<ResultModelClass>? {
        return try {
            if (jsonString.isNullOrEmpty()) {
                Log.e("EventsFragment", "JSON string is null or empty")
                return null
            }
            val wrapper = Gson().fromJson<Result>(jsonString, Result::class.java)
            val resultList = wrapper.result
            Log.d("EventsFragment", "Parsed Result List: $resultList") // Log the parsed result list
            resultList
        } catch (e: JsonSyntaxException) {
            Log.e("EventsFragment", "Error parsing JSON", e)
            e.printStackTrace()
            null
        }
    }

}