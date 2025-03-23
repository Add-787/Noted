package com.psyluckco.noted.data.repository

import com.psyluckco.noted.data.local.TaskDao
import com.psyluckco.noted.data.model.Task
import com.psyluckco.noted.data.model.toExternal
import com.psyluckco.noted.data.model.toLocal
import com.psyluckco.noted.data.network.NetworkDataSource
import com.psyluckco.noted.di.ApplicationScope
import com.psyluckco.noted.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.uuid.Uuid

@Singleton
class CombinedTaskRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: TaskDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : TaskRepository {

    override fun getTasksStream(): Flow<List<Task>> {
        return localDataSource.observeAll().map { tasks ->
            withContext(dispatcher) {
                tasks.toExternal()
            }
        }
    }

    override suspend fun getTasks(forceUpdate: Boolean): List<Task> {
        if(forceUpdate) {
            refresh()
        }

        return withContext(dispatcher) {
            localDataSource.getAll().toExternal()
        }
    }

    override suspend fun refresh() {
        withContext(dispatcher) {
            val remoteTasks = networkDataSource.loadTasks().take(4)
            localDataSource.deleteAll()
            localDataSource.upsertAll(remoteTasks.toLocal())
        }
    }

    override fun getTaskStream(taskId: String): Flow<Task?> {
        return localDataSource.observeById(taskId).map { it.toExternal() }

    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Task? {
        if(forceUpdate) {
            refresh()
        }

        return localDataSource.getById(taskId)?.toExternal()
    }

    override suspend fun refreshTask(taskId: String) {
        refresh()
    }

    override suspend fun createTask(title: String, description: String): String {
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description
        )
        localDataSource.upsert(task.toLocal())
        return task.id;
    }

    override suspend fun updateTask(taskId: String, title: String, description: String) {
        val task = getTask(taskId)?.copy(
            title = title,
            description = description
        ) ?: throw Exception("Task (id $taskId) not found")
    }

    override suspend fun completeTask(taskId: String) {
        localDataSource.updateCompleted(taskId, completed = true)
    }

    override suspend fun activateTask(taskId: String) {
        localDataSource.updateCompleted(taskId, completed = false)
    }

    override suspend fun clearCompletedTasks() {
        localDataSource.deleteCompleted()
    }

    override suspend fun deleteAllTasks() {
        localDataSource.deleteAll()
    }

    override suspend fun deleteTask(taskId: String) {
        localDataSource.deleteById(taskId)
    }
}