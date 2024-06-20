package com.example.jsonparsing

import com.google.gson.annotations.SerializedName

data class Result(
    val result : ArrayList<ResultModelClass>
)

data class ResultModelClass (
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("image_url")
    val imageUrl: String,
    var isOpened: Boolean = false,
    var viewCount: Int = 0,
    @SerializedName("more_images")
    val imageArray: ArrayList<String> = arrayListOf()
)