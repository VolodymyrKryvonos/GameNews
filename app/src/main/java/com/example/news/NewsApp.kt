package com.example.news

import android.app.Application
import com.example.news.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class NewsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // init DI
        startKoin {
            androidContext(this@NewsApp)
            modules(AppModule)
        }
    }
}