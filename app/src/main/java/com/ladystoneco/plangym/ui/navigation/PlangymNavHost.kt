package com.ladystoneco.plangym.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ladystoneco.plangym.ui.habit.HabitScreen
import com.ladystoneco.plangym.ui.home.HomeScreen
import com.ladystoneco.plangym.ui.planner.PlannerScreen
import com.ladystoneco.plangym.ui.planner.ProjectDetailScreen
import com.ladystoneco.plangym.ui.workout.RoutineDetailScreen
import com.ladystoneco.plangym.ui.workout.WorkoutHistoryScreen
import com.ladystoneco.plangym.ui.workout.WorkoutListScreen

@Composable
fun PlangymRoot() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { PlangymBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destination.Home.route) {
                HomeScreen()
            }

            composable(Destination.Planner.route) {
                PlannerScreen(
                    onProjectClick = { projectId ->
                        navController.navigate(Destination.ProjectDetail.createRoute(projectId))
                    }
                )
            }
            
            composable(
                route = Destination.ProjectDetail.route,
                arguments = listOf(
                    navArgument(Destination.ProjectDetail.ARG_PROJECT_ID) {
                        type = NavType.LongType
                    }
                )
            ) {
                ProjectDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Destination.Habit.route) { HabitScreen() }
            
            composable(Destination.Workout.route) {
                WorkoutListScreen(
                    onOpenRoutine = { routineId ->
                        navController.navigate(Destination.RoutineDetail.createRoute(routineId))
                    },
                    onOpenHistory = {
                        navController.navigate(Destination.WorkoutHistory.route)
                    }
                )
            }

            composable(Destination.WorkoutHistory.route) {
                WorkoutHistoryScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Destination.RoutineDetail.route,
                arguments = listOf(
                    navArgument(Destination.RoutineDetail.ARG_ROUTINE_ID) {
                        type = NavType.LongType
                    }
                )
            ) {
                RoutineDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
