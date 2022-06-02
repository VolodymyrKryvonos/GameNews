package com.example.data.remote.dto

import android.util.Log
import com.example.data.local.NewsEntity
import com.example.domain.model.News
import com.example.utils.toDate

data class ArticleDto(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String?
) {
    fun toNews() = News(
        publishedAt.toDate("yyyy-MM-dd'T'HH:mm:ss'Z'")?.time ?: 0L, title, url, urlToImage
    ).also { Log.e("toNews", title) }

    fun toNewsEntity() = NewsEntity(
        publishedAt.toDate("yyyy-MM-dd'T'HH:mm:ss'Z'")?.time ?: 0L, title, url, urlToImage ?: "", 0
    ).also { Log.e("toNews", title) }
}