package com.ladystoneco.plangym.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ladystoneco.plangym.R

@Composable
fun PlangymBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets.navigationBars
    ) {
        TopDestination.all.forEach { dest ->
            val selected = currentRoute?.hierarchy?.any { it.route == dest.route } == true
            val label = when (dest) {
                TopDestination.Home -> stringResource(R.string.home)
                TopDestination.Planner -> stringResource(R.string.planner)
                TopDestination.Habit -> stringResource(R.string.habits)
                TopDestination.Workout -> stringResource(R.string.workout)
            }
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { 
                    Icon(
                        dest.icon, 
                        contentDescription = label,
                        tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    ) 
                },
                label = { 
                    Text(
                        label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    }
}
