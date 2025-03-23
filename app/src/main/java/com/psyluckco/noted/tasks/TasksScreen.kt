/**
 * Created by developer on 21-03-2025.
 * Tismo Technology Solutions (P) Ltd.
 * developers@tismotech.net
 */

package com.psyluckco.noted.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.psyluckco.noted.R
import com.psyluckco.noted.data.model.Task
import com.psyluckco.noted.ui.component.CircularCheckbox
import com.psyluckco.noted.ui.theme.NotedTheme
import java.util.Collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    onTaskAdded: () -> Unit,
    onTaskClicked: (Task) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    viewModel: TasksViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
//        topBar = {
//                 TopAppBar(
//                     title = { },
//                     actions = {
//                         IconButton(onClick = viewModel::forceCrash) {
//                             Icon(imageVector = Icons.Outlined.Build, contentDescription = "force crash")
//                         }
//                     }
//                 )
//        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onTaskAdded,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "add task")
            }
        }
    ) {
        paddingValues ->

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            TasksContent(
                isRefreshing = uiState.isRefreshing,
                tasks = uiState.tasks,
                onRefresh = viewModel::refresh,
                onAppCrashed = viewModel::forceCrash,
                onTaskCheckedChange = viewModel::completedTask,
                onTaskClick = onTaskClicked,
                modifier = Modifier.padding(paddingValues)
            )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksContent(
    isRefreshing: Boolean,
    tasks: List<Task>,
    onRefresh: () -> Unit,
    onAppCrashed: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        if(tasks.isEmpty()) {
            NoTasksContent()
        } else {
            Column(
                modifier = modifier
                    .padding(horizontal = 18.dp, vertical = 4.dp)
            ) {
                StatusBox(noOfCompletedTasks = tasks.filter { t -> t.isCompleted }.size, onAppCrashed = onAppCrashed,totalNoOfTasks = tasks.size)
                Spacer(modifier = Modifier.height(24.dp))
                TasksList(isRefreshing = isRefreshing, onRefresh = onRefresh,tasks = tasks, onTaskClick = onTaskClick, onTaskCheckedChange = onTaskCheckedChange)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun EmptyTasksPreview() {
    NotedTheme {
        Surface {
            TasksContent(
                isRefreshing = false,
                tasks = emptyList(),
                onRefresh = {  },
                onTaskClick = { },
                onTaskCheckedChange = { _,_ -> },
                onAppCrashed = { },
                modifier = Modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SomeTasksPreview() {
    NotedTheme {
        Surface {
            TasksContent(
                isRefreshing = false,
                tasks = listOf(
                    Task(
                        id = "jwbnejkfn",
                        title = "Eat",
                        isCompleted = true
                    ),
                    Task(
                        id = "jwbnmldem",
                        title = "Sleep",
                        isCompleted = false
                    ),
                    Task(
                        id = "jwbnnkld",
                        title = "Repeat",
                        isCompleted = false
                    )
                ),
                onRefresh = { },
                onAppCrashed = { },
                onTaskClick = { },
                onTaskCheckedChange = { _,_ -> }
            )

        }
    }
}

@Composable
fun NoTasksContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.notasks),
                contentDescription = "My PNG Image",
                modifier = Modifier
                    .size(96.dp)  // Adjust size
            )
            Text(
                text = "No tasks created",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun StatusBox(
    noOfCompletedTasks: Int,
    totalNoOfTasks: Int,
    onAppCrashed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Status",
                modifier = Modifier.padding(vertical = 10.dp),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = onAppCrashed) {
                Icon(imageVector = Icons.Filled.Build, contentDescription = "force crash")
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "$totalNoOfTasks tasks"
                )
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(7.dp))
                LinearProgressIndicator(
                    progress = { (noOfCompletedTasks.toFloat()/totalNoOfTasks) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

            }
            
        }
    }
}

@Preview
@Composable
private fun StatusBoxPreview() {
    NotedTheme {
        Surface {
            StatusBox(noOfCompletedTasks = 5, onAppCrashed = { }, totalNoOfTasks = 10)
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { onTaskClick(task) }
        ) {
            CircularCheckbox(checked = task.isCompleted, onCheckedChange = onCheckedChange)
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(
                    start = 16.dp
                ),
                textDecoration = if(task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    null
                }
            )
        }
    }

}

@Preview
@Composable
private fun TaskItemPreview() {
    NotedTheme {
        TaskItem(
            task = Task(
                id = "hbvhefbvuef",
                title = "Eat ice cream",
                isCompleted = false
            ),
            onTaskClick = { },
            onCheckedChange = { }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksList(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task,Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "Tasks",
            modifier = Modifier.padding(vertical = 10.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = onRefresh) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onTaskClick = onTaskClick,
                        onCheckedChange = { onTaskCheckedChange(task,it) },
                    )
                }

            }
        }
    }

}
