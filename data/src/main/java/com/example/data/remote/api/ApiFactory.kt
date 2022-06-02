package com.example.data.remote.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class ApiFactory {

    companion object {

        val moshi: Moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }


    fun buildRetrofit(apiUrl: String): Retrofit {
        return Retrofit.Builder().apply {
            this.addConverterFactory(ScalarsConverterFactory.create())
            this.addConverterFactory(MoshiConverterFactory.create(moshi))
        }
            .baseUrl(apiUrl)
            .client(createOkHttpClient())
            .build()
    }


    private fun createOkHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}
