/**
 * Created by developer on 21-03-2025.
 * Tismo Technology Solutions (P) Ltd.
 * developers@tismotech.net
 */

package com.psyluckco.noted.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psyluckco.noted.R
import com.psyluckco.noted.data.model.Task
import com.psyluckco.noted.data.repository.TaskRepository
import com.psyluckco.noted.utils.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


/**
 * UiState for the tasks screen.
 */
data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isRefreshing: Boolean = false,
    val userMessage: Int? = null
)

/**
 * ViewModel for the task list screen.
 */
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isRefreshing = MutableStateFlow(false)
    private val _tasks = taskRepository.getTasksStream()
        .map { Async.Success(it) }
        .catch<Async<List<Task>>> { emit(Async.Error(R.string.placeholder)) }

    val uiState: StateFlow<TasksUiState> = combine(
        _isRefreshing,_userMessage,_tasks
    ) {
        isRefreshing,userMessage,tasks ->
            when(tasks) {
                Async.Loading -> {
                    TasksUiState(isRefreshing = true)
                }
                is Async.Error -> {
                    TasksUiState(userMessage = tasks.errorMessage)
                }
                is Async.Success -> {
                    TasksUiState(
                        tasks = tasks.data,
                        isRefreshing = isRefreshing,
                        userMessage = userMessage
                    )
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TasksUiState(isRefreshing = true)
    )
}