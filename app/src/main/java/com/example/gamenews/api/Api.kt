package com.example.gamenews.api

import com.example.gamenews.model.News
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET(".")
    suspend fun getNews() : Response<List<News>>
}