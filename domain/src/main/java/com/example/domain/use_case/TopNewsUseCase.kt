package com.example.domain.use_case

import android.util.Log
import com.example.domain.model.News
import com.example.domain.model.Resource
import com.example.domain.repos.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TopNewsUseCase(private val repository: NewsRepository) {
    operator fun invoke(): Flow<Resource<List<News>>> = flow{
        try {
            emit(Resource.Loading<List<News>>())
            Log.d("TopNewsUseCase", "invoke() called")
            val news = repository.getTopNews()
            emit(Resource.Success<List<News>>(news))
        }catch (e: Exception){
            emit(Resource.Error<List<News>>(e.localizedMessage.toString()))
        }
    }
}