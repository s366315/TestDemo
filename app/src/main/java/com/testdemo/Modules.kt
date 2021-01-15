package com.testdemo

import com.testdemo.api.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun appModule() = module {
    single { provideRetrofit() }
}

fun provideRetrofit(): ApiService {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(App.okHttpClient)
        .build().create(ApiService::class.java)
}