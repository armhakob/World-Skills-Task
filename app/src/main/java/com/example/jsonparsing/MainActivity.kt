package com.example.jsonparsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jsonparsing.Result
import okhttp3.internal.notify
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resultList: ArrayList<ResultModelClass> = ArrayList()

        try {
            val obj = JSONObject(getJSONFromAssets()!!)
            // fetch JSONArray named users by using getJSONArray
            val resultArray = obj.getJSONArray("result")
            // Get the users data using for loop i.e. id, name, email and so on

            for (i in 0 until resultArray.length()) {
                // Create a JSONObject for fetching single User's Data
                val result = resultArray.getJSONObject(i)
                // Fetch id store it in variable
                val id = result.getInt("id")
                val title = result.getString("title")
                val text = result.getString("text")
                val imageUrl = result.getString("image_url")

                val resultDetails =
                    ResultModelClass(id, title, text, imageUrl, isOpened = false)
                resultList.add(resultDetails)
            }
        } catch (e: JSONException) {
            //exception
            e.printStackTrace()
        }

        // Set the LayoutManager that this RecyclerView will use.
        val rvResultList = findViewById<RecyclerView>(R.id.rvResultList)
        rvResultList.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = ResultAdapter(this, resultList) { position ->
            resultList[position].isOpened = true
            rvResultList.adapter?.notifyItemChanged(position)
        }
        // adapter instance is set to the recyclerview to inflate the items.
        rvResultList.adapter = itemAdapter

    }

    private fun getJSONFromAssets(): String? {

        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myResultJSONFile = assets.open("example.json")
            val size = myResultJSONFile.available()
            val buffer = ByteArray(size)
            myResultJSONFile.read(buffer)
            myResultJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}