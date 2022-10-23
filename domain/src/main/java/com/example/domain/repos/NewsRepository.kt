package com.example.domain.repos

import com.example.domain.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getTopNews(): List<News>

    fun getNews(): Flow<List<News>>

    suspend fun updateNews()

    suspend fun addToFavorites(news: News)

    suspend fun removeFromFavorites(news: News)

    fun getFavorites(): Flow<List<News>>

}