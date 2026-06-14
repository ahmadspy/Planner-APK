package com.ladystoneco.plangym.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destination(val route: String) {
    data object Home : Destination("home")
    data object Planner : Destination("planner")
    data object Habit : Destination("habit")
    data object Workout : Destination("workout")
    data object WorkoutHistory : Destination("workout_history")
    data object ProjectDetail : Destination("project/{projectId}") {
        const val ARG_PROJECT_ID = "projectId"
        fun createRoute(projectId: Long) = "project/$projectId"
    }
    data object RoutineDetail : Destination("routine_detail/{routineId}") {
        const val ARG_ROUTINE_ID = "routineId"
        fun createRoute(routineId: Long) = "routine_detail/$routineId"
    }
}

sealed class TopDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : TopDestination(Destination.Home.route, "خانه", Icons.Outlined.Home)
    data object Planner : TopDestination(Destination.Planner.route, "برنامه", Icons.Outlined.ListAlt)
    data object Habit : TopDestination(Destination.Habit.route, "عادت‌ها", Icons.Outlined.CheckCircle)
    data object Workout : TopDestination(Destination.Workout.route, "تمرین", Icons.Outlined.FitnessCenter)

    companion object {
        val all = listOf(Home, Planner, Habit, Workout)
    }
}
