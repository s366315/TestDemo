package com.testdemo

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {

        val okHttpClient = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                addInterceptor(logging)
            }
        }.build()
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(appModule()))
        }
    }
}