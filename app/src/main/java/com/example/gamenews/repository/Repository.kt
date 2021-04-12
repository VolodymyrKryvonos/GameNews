package com.example.gamenews.repository

import com.example.gamenews.api.RetrofitInstance
import com.example.gamenews.model.News
import retrofit2.Response

class Repository {

    suspend fun getNews(): Response<List<News>> {
        return RetrofitInstance.api.getNews()
    }

}