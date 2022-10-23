package com.example.data.remote.api

import com.example.data.remote.dto.GeneralResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/v2/top-headlines")
    suspend fun getTopNews(@Query("q") q: String = "Ukraine",
                        @Query("apiKey") apiKey: String = "926711897ea54723ab4aaf266a755b25") : GeneralResponse

    @GET("/v2/everything")
    suspend fun getNews(@Query("q") q: String = "Ukraine",
                        @Query("q") from: String,
                        @Query("q") to: String,
                        @Query("apiKey") apiKey: String = "926711897ea54723ab4aaf266a755b25") : GeneralResponse
}