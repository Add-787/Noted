package com.psyluckco.noted.data.network

import retrofit2.Call
import retrofit2.http.GET

interface TaskApi {

    @GET("todos")
    fun getTasks(): Call<List<NetworkTask>>
}