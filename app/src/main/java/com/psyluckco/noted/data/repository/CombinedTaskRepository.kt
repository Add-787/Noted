package com.psyluckco.noted.data.repository

import com.psyluckco.noted.data.local.TaskDao
import com.psyluckco.noted.data.model.Task
import com.psyluckco.noted.di.ApplicationScope
import com.psyluckco.noted.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CombinedTaskRepository @Inject constructor(
    private val localDataSource: TaskDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : TaskRepository {

    override fun getTasksStream(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun createTask(title: String, description: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
}