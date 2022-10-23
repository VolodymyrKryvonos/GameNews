package com.example.data.remote.dto

data class GeneralResponse(
    val articles: List<ArticleDto>,
    val status: String,
    val totalResults: Int
)