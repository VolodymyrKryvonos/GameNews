package com.example.data

import android.util.Log
import com.example.data.local.NewsDao
import com.example.data.local.NewsEntity
import com.example.data.remote.api.Api
import com.example.domain.model.News
import com.example.domain.repos.NewsRepository
import com.example.utils.parseDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import java.util.*

class NewsRepositoryImpl(private val api: Api, private val newsDao: NewsDao) :
    NewsRepository {
    override suspend fun getTopNews(): List<News> = api.getTopNews().articles.map { it.toNews() }

    override fun getNews(): Flow<List<News>> {
        try {
            return newsDao.getNews().map { it.map { entity->entity.toNews() } }
        }catch (e:Exception){
            throw e
        }
    }

    override suspend fun updateNews() {
        try {
            val c = Calendar.getInstance()
            c.add(Calendar.DATE,-1)
            Log.e("NewsUseCase", "invoke() called")
            val strDt = c.time.parseDate("yyyy-MM-dd")
            newsDao.insert(api.getNews(from = strDt, to = strDt).articles.map { it.toNewsEntity() })
        }catch (e:Exception){
            throw e
        }
    }


    override suspend fun addToFavorites(news: News) {
        newsDao.update(NewsEntity(news).also { it.isFavorite = 1 })
    }

    override suspend fun removeFromFavorites(news: News) {
        newsDao.update(NewsEntity(news).also { it.isFavorite = 0 })
    }

    override fun getFavorites(): Flow<List<News>> = newsDao.getFavorites().map {
        it.map { entity -> entity.toNews() }
    }
}