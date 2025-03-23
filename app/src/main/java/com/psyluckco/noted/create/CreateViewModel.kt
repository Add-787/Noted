package com.psyluckco.noted.create

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psyluckco.noted.NotedDestinationsArgs
import com.psyluckco.noted.R
import com.psyluckco.noted.data.model.Task
import com.psyluckco.noted.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * UiState for the create screen.
 */
data class CreateUiState(
    val title: String = "",
    val description: String = "",
    val isTaskLoaded: Boolean = false,
    val isTaskCompleted: Boolean = false,
    val isTaskDeleted: Boolean = false,
    val isRefreshing: Boolean = false,
    val userMessage: Int? = null,
    val isTaskSaved: Boolean = false
)

/**
 * ViewModel for the Create screen.
 */
@HiltViewModel
class CreateViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: String? = savedStateHandle[NotedDestinationsArgs.TASK_ID_ARG]

    private val _uiState = MutableStateFlow(CreateUiState())
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    init {
        if(taskId != null) {
            loadTask(taskId)
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateDescription(newDescription: String) {
        _uiState.update {
            it.copy(description = newDescription)
        }
    }

    fun saveTask() {
        if (uiState.value.title.isEmpty() || uiState.value.description.isEmpty()) {
            _uiState.update {
                it.copy(userMessage = R.string.placeholder)
            }
            return
        }

        if(taskId == null) {
            createNewTask()
        } else {
            updateTask()
        }
        
    }

    private fun createNewTask() {
        viewModelScope.launch {
            taskRepository.createTask(uiState.value.title, uiState.value.description)
            _uiState.update {
                it.copy(isTaskSaved = true)
            }
        }
    }

    fun deleteTask() {
        if(taskId == null) {
            throw RuntimeException("deleteTask() was called but task is new.")
        }
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)

            _uiState.update {
                it.copy(isTaskDeleted = true)
            }
        }
    }

    private fun updateTask() {
        if (taskId == null) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        viewModelScope.launch {
            taskRepository.updateTask(
                taskId,
                title = uiState.value.title,
                description = uiState.value.description,
            )
            _uiState.update {
                it.copy(isTaskSaved = true)
            }
        }
    }



    private fun loadTask(taskId: String) {
        _uiState.update {
            it.copy(isRefreshing = true)
        }

        viewModelScope.launch {
            taskRepository.getTask(taskId).let { task ->
                if(task != null) {
                    _uiState.update {
                        it.copy(
                            title = task.title,
                            description = task.description,
                            isTaskLoaded = true,
                            isTaskCompleted = task.isCompleted,
                            isRefreshing = false
                        )
                    }

                } else {
                    _uiState.update {
                        it.copy(isRefreshing = false)
                    }
                }
            }
        }

    }

}

