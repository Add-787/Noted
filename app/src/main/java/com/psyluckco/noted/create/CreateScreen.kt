package com.psyluckco.noted.create

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CreateScreen(
    onTaskUpdated: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CreateContent(
                isRefreshing = uiState.isRefreshing,
                title = uiState.title,
                onTitleChanged = viewModel::updateTitle,
                onDescriptionChanged = viewModel::updateDescription,
                modifier = modifier.padding(paddingValues)
            )


    }
}

@Composable
fun CreateContent(
    isRefreshing: Boolean,
    title: String = "New Task",
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

}

