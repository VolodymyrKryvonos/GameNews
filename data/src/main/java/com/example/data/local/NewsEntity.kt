package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.News

@Entity
data class NewsEntity(
    val publishedAt: Long,
    @PrimaryKey
    val title: String,
    val url: String,
    val urlToImage: String,
    var isFavorite: Int
) {
    fun toNews(): News = News(
        publishedAt, title, url, urlToImage, isFavorite == 1
    )

    constructor(news: News) : this(
        news.publishedAt,
        news.title,
        news.url,
        news.urlToImage ?: "",
        if (news.isFavorite) 1 else 0
    )
}
