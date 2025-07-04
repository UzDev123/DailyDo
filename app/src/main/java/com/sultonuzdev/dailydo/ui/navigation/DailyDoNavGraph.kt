package com.sultonuzdev.dailydo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sultonuzdev.dailydo.ui.screens.reports.ReportsScreen
import com.sultonuzdev.dailydo.ui.screens.settings.SettingsScreen
import com.sultonuzdev.dailydo.ui.screens.stories.StoryDetailScreen
import com.sultonuzdev.dailydo.ui.screens.stories.StoryListScreen
import com.sultonuzdev.dailydo.ui.screens.tasks.TaskDetailScreen
import com.sultonuzdev.dailydo.ui.screens.tasks.TasksScreen

@Composable
fun DailyDoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = BottomDestinations.Tasks.route,
        modifier = modifier
    ) {
        // Tasks section
        composable(BottomDestinations.Tasks.route) {
            TasksScreen(
                onTaskClick = { taskId ->
                    navController.navigate(TaskDetail(taskId))
                }, onAddTaskClick = {
                    navController.navigate(TaskDetail())
                },
                modifier = modifier
            )
        }

        composable<TaskDetail> { backStackEntry ->
            val arguments = backStackEntry.toRoute<TaskDetail>()
            TaskDetailScreen(
                taskId = arguments.taskId, onBackClick = { navController.popBackStack() }
                ,
                modifier = modifier
            )

        }

        // Reports section
        composable(BottomDestinations.Reports.route) {
            ReportsScreen(
                modifier = modifier
            )
        }

        // Stories section
        composable(BottomDestinations.Stories.route) {
            StoryListScreen(onStoryClick = { storyId ->
                navController.navigate(StoryDetail(storyId))
            }, onAddStoryClick = {
                navController.navigate(StoryDetail())
            }, modifier = modifier)
        }

        composable<StoryDetail> { backStackEntry ->
            val arguments = backStackEntry.toRoute<StoryDetail>()
            StoryDetailScreen(
                modifier = modifier,
                storyId = arguments.storyId, onBackClick = { navController.popBackStack() })
        }


        // Settings section
        composable(BottomDestinations.Settings.route) {
            SettingsScreen(
                modifier=modifier,
            )
        }
    }
}