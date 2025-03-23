package com.psyluckco.noted

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.psyluckco.noted.NotedDestinationsArgs.TASK_ID_ARG
import com.psyluckco.noted.NotedDestinationsArgs.USER_MESSAGE_ARG
import com.psyluckco.noted.NotedScreens.CREATE_SCREEN
import com.psyluckco.noted.NotedScreens.TASKS_SCREEN

/**
 * Screens used in [NotedDestinations]
 */
private object NotedScreens {
    const val TASKS_SCREEN = "tasks"
    const val CREATE_SCREEN = "create"
}

/**
 * Arguments used in [NotedDestinations] routes
 */
object NotedDestinationsArgs {
    const val USER_MESSAGE_ARG = "userMessage"
    const val TASK_ID_ARG = "taskId"
}

/**
 * Destinations used in the [MainActivity]
 */
object NotedDestinations {
    const val TASKS_ROUTE = "$TASKS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
    const val CREATE_ROUTE = "$CREATE_SCREEN?$TASK_ID_ARG={$TASK_ID_ARG}"
}

/**
 * Models the navigation actions in the app.
 */
class NotedNavigationActions(private val navController: NavHostController) {

    fun navigateToCreateTask(taskId: String?) {
        navController.navigate(
            CREATE_SCREEN.let {
                if (taskId != null) "$it?$TASK_ID_ARG=$taskId" else it
            }
        )
    }


    fun navigateToTasks(userMessage: Int = 0) {
        navController.navigate(
            TASKS_SCREEN.let {
                if(userMessage != 0) "$it?$USER_MESSAGE_ARG=$userMessage" else it
            }
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
                saveState = false
            }
            launchSingleTop = true
            restoreState = false
        }
    }
}