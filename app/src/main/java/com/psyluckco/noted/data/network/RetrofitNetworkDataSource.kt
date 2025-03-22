package com.psyluckco.noted.data.network

import retrofit2.Call
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitNetworkDataSource @Inject constructor(
    private val taskApi: TaskApi
) : NetworkDataSource {

    override suspend fun loadTasks(): List<NetworkTask> {
        val response = taskApi.getTasks().execute()
        return response.body() ?: emptyList()
    }


}