package com.example.domain.model

data class News(
    val publishedAt: Long,
    val title: String,
    val url: String,
    val urlToImage: String?,
    var isFavorite: Boolean = false
)
