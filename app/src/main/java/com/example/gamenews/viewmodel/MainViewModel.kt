package com.example.gamenews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
    private val _stories: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val _videos: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val _favorites: MutableLiveData<ArrayList<News>> = MutableLiveData()
    private val _topNews: MutableLiveData<ArrayList<News>> = MutableLiveData()

    val stories: LiveData<ArrayList<News>> get() = _stories
    val videos: LiveData<ArrayList<News>> get() = _videos
    val favorites: LiveData<ArrayList<News>> get() = _favorites
    val topNews: LiveData<ArrayList<News>> get() = _topNews

    init {
        _stories.value = arrayListOf()
        _videos.value = arrayListOf()
        _favorites.value = arrayListOf()
        _topNews.value = arrayListOf()
    }

    private fun distributeNews(news: List<News>) {
        _topNews.value?.clear()
        _stories.value?.clear()
        _videos.value?.clear()
        _favorites.value?.clear()
        for (i in news) {
            if (i.top.equals("0")){
                _topNews.value?.add(i)
            }
            when (i.type) {
                "strories" -> _stories.value?.add(i)
                "video" -> _videos.value?.add(i)
                "favourites" -> _favorites.value?.add(i)
            }
        }

        _topNews.value = _topNews.value
        _stories.value = _stories.value
        _videos.value = _videos.value
        _favorites.value = _favorites.value
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