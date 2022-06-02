package com.example.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.News
import com.example.domain.model.Resource
import com.example.domain.repos.NewsRepository
import com.example.domain.use_case.TopNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val topNewsUseCase: TopNewsUseCase,
    private val repository: NewsRepository
) : ViewModel() {

    val searchText = MutableSharedFlow<String>(0)
    private val _topNews = MutableStateFlow<List<News>>(arrayListOf())
    private val _newsUpdated = MutableStateFlow(false)

    val newsUpdated: StateFlow<Boolean> get() = _newsUpdated
    val topNews: StateFlow<List<News>> get() = _topNews

    init {
        _topNews.value = arrayListOf()
        getTopNews()
        updateNews()
    }

    fun getFavorites() = repository.getFavorites()

    fun addFavoriteNews(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorites(news)
        }
    }

    fun removeFavoriteNews(news: News) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromFavorites(news)
        }
    }

    private fun getTopNews() {
        topNewsUseCase().onEach {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { newsList -> _topNews.emit(newsList.filter { filterable -> filterable.urlToImage != null }) }
                    _newsUpdated.emit(true)
                }
                is Resource.Loading -> {
                    _newsUpdated.emit(false)
                }
                is Resource.Error -> {
                    _newsUpdated.emit(true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getNews() = repository.getNews()

    fun updateNews() {
        viewModelScope.launch {
            _newsUpdated.emit(false)
            repository.updateNews()
            _newsUpdated.emit(true)
        }
    }


}