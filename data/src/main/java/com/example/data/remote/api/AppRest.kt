package com.example.data.remote.api


class AppRest<T>(private val service: Class<T>, private val apiURL: String) {

    private var apiFactory: ApiFactory = ApiFactory()

    //@Throws(IOException::class)
    fun api(): T {
        return apiFactory
            .buildRetrofit(apiURL)
            .create(service)
    }
}
