package com.psyluckco.noted.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.psyluckco.noted.R
import com.psyluckco.noted.ui.theme.NotedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    onTaskUpdated: () -> Unit,
    onTaskDeleted: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
                 TopAppBar(
                     title = { },
                     navigationIcon = { IconButton(onClick = onBack) {
                         Icon(
                             imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                             contentDescription = "go back"
                         )
                     }},
                     actions = {
                        if(uiState.isTaskLoaded) {
                            IconButton(onClick = viewModel::deleteTask) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "delete task"
                                )
                            }
                        }

                     }
                 )
        },
        floatingActionButton = { FloatingActionButton(
            onClick = viewModel::saveTask,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "done")
        }},
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

            CreateContent(
                isRefreshing = uiState.isRefreshing,
                title = uiState.title,
                description = uiState.description,
                onTitleChanged = viewModel::updateTitle,
                onDescriptionChanged = viewModel::updateDescription,
                modifier = modifier.padding(paddingValues)
            )

        LaunchedEffect(uiState.isTaskSaved) {
            if (uiState.isTaskSaved) {
                onTaskUpdated()
            }
        }

        LaunchedEffect(uiState.isTaskDeleted) {
            if (uiState.isTaskDeleted) {
                onTaskDeleted()
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateContent(
    isRefreshing: Boolean,
    title: String = "New Task",
    description: String = "New Description",
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var initialRefresh by remember { mutableStateOf(false) }
    val refreshState = rememberPullToRefreshState()

    if(isRefreshing) {
        PullToRefreshBox(
            isRefreshing = initialRefresh,
            state = refreshState,
            onRefresh = { /*TODO*/ }
        ) {
            
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {

            val textFieldColors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onSecondary
            )

            OutlinedTextField(
                value = title,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall
                    .copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                colors = textFieldColors
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text(stringResource(id = R.string.enter_description)) },
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth(),
                colors = textFieldColors
            )

        }
    }

}

@Preview
@Composable
private fun CreateContentUpdatePreview() {
    NotedTheme {
        Surface {
            CreateContent(
                isRefreshing = false,
                title = "Run",
                description = "Go for a run in the morning",
                onTitleChanged = { _ -> },
                onDescriptionChanged = { _ -> },
            )
        }
    }
}

@Preview
@Composable
private fun CreateContentNewPreview() {
    NotedTheme {
        Surface {
            CreateContent(
                isRefreshing = false,
                title = "",
                description = "",
                onTitleChanged = { _ -> },
                onDescriptionChanged = { _ -> },
            )
        }
    }
}

