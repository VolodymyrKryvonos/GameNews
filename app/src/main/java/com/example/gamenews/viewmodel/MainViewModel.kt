package com.example.gamenews.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamenews.model.News
import com.example.gamenews.repository.Repository
import kotlinx.coroutines.launch
import kotlin.Exception

class MainViewModel(private val repository: Repository) : ViewModel() {

    var isResponseReceived: Boolean = false
        private set

    val searchText: MutableLiveData<String> = MutableLiveData()
    private val stories: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val videos: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val favorites: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val topNews: MutableLiveData<ArrayList<News>> = MutableLiveData()

    init {
        stories.value = arrayListOf()
        videos.value = arrayListOf()
        favorites.value = arrayListOf()
        topNews.value = arrayListOf()
    }

    fun getStories(): MutableLiveData<ArrayList<News>> {
        return stories
    }

    fun getVideo(): MutableLiveData<ArrayList<News>> {
        return videos
    }

    fun getFavorites(): MutableLiveData<ArrayList<News>> {
        return favorites
    }

    fun getTopNews(): MutableLiveData<ArrayList<News>> {
        return topNews
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    private fun distributeNews(news: List<News>) {
        topNews.value?.clear()
        stories.value?.clear()
        videos.value?.clear()
        favorites.value?.clear()
        for (i in news) {
            if (i.top.equals("0")){
                topNews.value?.add(i)
            }
            when (i.type) {
                "strories" -> stories.value?.add(i)
                "video" -> videos.value?.add(i)
                "favourites" -> favorites.value?.add(i)
            }
        }

        topNews.value = topNews.value
        stories.value = stories.value
        videos.value = videos.value
        favorites.value = favorites.value
    }

    fun makeRequest() {
        viewModelScope.launch {
            try {
                val response = repository.getNews()
                if (response.isSuccessful) {
                    isResponseReceived = true
                    distributeNews(response.body()!!)
                } else {
                    Log.e("request", response.code().toString())
                }
            }catch (e: Exception){
                Log.e("makeRequest", e.toString())
            }

        }
    }

}