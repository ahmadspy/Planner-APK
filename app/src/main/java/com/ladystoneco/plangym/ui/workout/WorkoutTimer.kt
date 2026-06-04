package com.ladystoneco.plangym.ui.workout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ladystoneco.plangym.R
import com.ladystoneco.plangym.ui.home.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.ladystoneco.plangym.util.LocalizationManager
import kotlinx.coroutines.delay

@Composable
fun WorkoutTimer(modifier: Modifier = Modifier) {
    var seconds by remember { mutableLongStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    
    // Using HomeViewModel just to get the current locale for digit formatting
    val homeViewModel: HomeViewModel = hiltViewModel()
    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000)
                seconds++
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(stringResource(R.string.workout_timer), style = MaterialTheme.typography.labelMedium)
                Text(
                    text = LocalizationManager.formatNumbers(formatTime(seconds), uiState.locale),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            Row {
                IconButton(onClick = { seconds = 0; isRunning = false }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                }
                FilledIconButton(onClick = { isRunning = !isRunning }) {
                    Icon(
                        if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Start/Pause"
                    )
                }
            }
        }
    }
}

private fun formatTime(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return "%02d:%02d:%02d".format(h, m, s)
}
