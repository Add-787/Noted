package com.psyluckco.noted

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.psyluckco.noted.NotedDestinationsArgs.TASK_ID_ARG
import com.psyluckco.noted.NotedDestinationsArgs.USER_MESSAGE_ARG
import com.psyluckco.noted.create.CreateScreen
import com.psyluckco.noted.tasks.TasksScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NotedDestinations.TASKS_ROUTE,
    navActions: NotedNavigationActions = remember(navController) {
        NotedNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(
            NotedDestinations.TASKS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) {
            TasksScreen(
                onTaskAdded = { navActions.navigateToCreateTask(taskId = null) },
                onTaskClicked = { navActions.navigateToCreateTask(taskId = it.id) },
                onUserMessageDisplayed = { /*TODO*/ })
        }

        composable(
            NotedDestinations.CREATE_ROUTE,
            arguments = listOf(
                navArgument(TASK_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) {
            entry ->
                val taskId = entry.arguments?.getString(TASK_ID_ARG)

            CreateScreen(
                onTaskUpdated = {
                                navActions.navigateToTasks(
                                    if(taskId == null) R.string.placeholder else R.string.placeholder
                                )
                },
                onTaskDeleted = {
                                navActions.navigateToTasks(
                                    if(taskId == null) R.string.placeholder else R.string.placeholder
                                )
                },
                onBack = { navController.popBackStack() }
            )
        }


        
    }

}
