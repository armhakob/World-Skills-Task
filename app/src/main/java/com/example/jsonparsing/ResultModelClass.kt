package com.example.jsonparsing

data class Result(
    val result : ArrayList<ResultModelClass>
)

data class ResultModelClass (
    val id: Int,
    val title: String,
    val text: String,
    val imgUrl: String
)