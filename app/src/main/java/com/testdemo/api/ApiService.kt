package com.testdemo.api

import com.testdemo.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("users")
    suspend fun fetchData(@Query("since") since: Int): List<UserModel>
}