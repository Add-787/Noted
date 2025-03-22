package com.psyluckco.noted.data.network

interface NetworkDataSource {
    suspend fun loadTasks(): List<NetworkTask>
}