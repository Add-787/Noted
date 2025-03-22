/**
 * Created by developer on 21-03-2025.
 * Tismo Technology Solutions (P) Ltd.
 * developers@tismotech.net
 */

package com.psyluckco.noted.data.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.psyluckco.noted.data.local.LocalTask
import com.psyluckco.noted.data.network.NetworkTask

// External to Local
fun Task.toLocal() = LocalTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

fun List<Task>.toLocal() = map(Task::toLocal)

// Local to External
fun LocalTask.toExternal() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("localToExternal")
fun List<LocalTask>.toExternal() = map(LocalTask::toExternal)

// Network to Local
fun NetworkTask.toLocal() = LocalTask(
    id = id.toString(),
    title = title,
    description = "",
    isCompleted = completed
)