package com.sultonuzdev.dailydo.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations for the Daily Do app.
 */


// Main screen destinations
@Serializable
sealed class BottomDestinations(
    val route: String
) {
    @Serializable
    data object Tasks : BottomDestinations(
        route = "tasks"
    )

    @Serializable
    data object Reports : BottomDestinations(
        route = "reports"
    )

    @Serializable
    data object Stories : BottomDestinations(
        route = "stories"
    )

    @Serializable
    data object Settings : BottomDestinations(
        route = "settings"
    )
}


// Detail screen destinations with arguments
@Serializable
data class TaskDetail(val taskId: Long = -1L)

@Serializable
data class StoryDetail(val storyId: Long = -1L)