package com.psyluckco.noted.data.model

data class Task(
    val id: String,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
) {
    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}


