package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.data.NewsRepositoryImpl
import com.example.data.local.AppDatabase
import com.example.data.local.NewsDao
import com.example.data.remote.api.Api
import com.example.data.remote.api.AppRest
import com.example.domain.repos.NewsRepository
import com.example.domain.use_case.TopNewsUseCase
import com.example.news.viewmodel.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { TopNewsUseCase(get()) }
    single { provideDatabase(androidContext()) }
    single { provideNewsDao(get()) }
    single { provideApi() }
    single<NewsRepository> { NewsRepositoryImpl(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
}

private fun provideApi(): Api {
    return AppRest(Api::class.java, "https://newsapi.org").api()
}

fun provideDatabase(application: Context): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "news.db")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideNewsDao(database: AppDatabase): NewsDao {
    return database.newsDao()
}

