package com.ladystoneco.plangym.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ladystoneco.plangym.R
import com.ladystoneco.plangym.data.local.entity.WorkoutSetEntity
import com.ladystoneco.plangym.data.local.relation.ExerciseWithSets
import com.ladystoneco.plangym.ui.util.ModernCard
import com.ladystoneco.plangym.util.LocalizationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailScreen(
    onBack: () -> Unit,
    viewModel: RoutineDetailViewModel = hiltViewModel()
) {
    val exercises: List<ExerciseWithSets> by viewModel.exercises.collectAsState()
    val restTimer by viewModel.restTimerState.collectAsState()
    val totalVolume by viewModel.totalVolume.collectAsState()
    val homeViewModel: com.ladystoneco.plangym.ui.home.HomeViewModel = hiltViewModel()
    val homeState by homeViewModel.uiState.collectAsState()
    var showAddExercise by remember { mutableStateOf(false) }
    var setTargetExerciseId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.workout_details),
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddExercise = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_exercise))
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                com.ladystoneco.plangym.ui.util.MetricCard(
                    label = stringResource(R.string.total_volume),
                    value = LocalizationManager.formatNumbers(totalVolume.toInt().toString(), homeState.locale),
                    modifier = Modifier.weight(1f)
                )
                if (restTimer.isRunning) {
                    com.ladystoneco.plangym.ui.util.RestTimerCard(
                        remainingSeconds = restTimer.remainingSeconds,
                        totalSeconds = restTimer.totalSeconds,
                        modifier = Modifier.weight(1.5f)
                    )
                } else {
                    WorkoutTimer(modifier = Modifier.weight(1.5f))
                }
            }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(exercises, key = { it.exercise.id }) { item: ExerciseWithSets ->
                    ModernExerciseCard(
                        item = item,
                        onAddSet = { setTargetExerciseId = item.exercise.id },
                        onToggleSet = { set -> viewModel.toggleSet(set, item.exercise.restSec) },
                        onDeleteSet = viewModel::deleteSet,
                        onDeleteExercise = { viewModel.deleteExercise(item.exercise) }
                    )
                }
            }
        }
    }

    if (showAddExercise) {
        AddExerciseDialog(
            onDismiss = { showAddExercise = false },
            onConfirm = { name, restSec ->
                viewModel.addExercise(name, restSec, exercises.size)
                showAddExercise = false
            }
        )
    }

    setTargetExerciseId?.let { exId ->
        AddSetDialog(
            onDismiss = { setTargetExerciseId = null },
            onConfirm = { reps, weight ->
                val nextSetNum = exercises.find { it.exercise.id == exId }?.sets?.size?.plus(1) ?: 1
                viewModel.addSet(exId, reps, weight, nextSetNum)
                setTargetExerciseId = null
            }
        )
    }
}

@Composable
private fun ModernExerciseCard(
    item: ExerciseWithSets,
    onAddSet: () -> Unit,
    onToggleSet: (WorkoutSetEntity) -> Unit,
    onDeleteSet: (WorkoutSetEntity) -> Unit,
    onDeleteExercise: () -> Unit
) {
    var expanded by rememberSaveable(item.exercise.id) { mutableStateOf(true) }

    ModernCard(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
            ) {
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    item.exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CircleShape
                ) {
                    Text(
                        stringResource(R.string.sets_ratio, item.sets.count { it.isCompleted }, item.sets.size),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                IconButton(onClick = onDeleteExercise) {
                    Icon(
                        Icons.Default.DeleteOutline, 
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    item.sets.forEachIndexed { index, set ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                        ) {
                            Checkbox(checked = set.isCompleted, onCheckedChange = { onToggleSet(set) })
                            Text(
                                stringResource(R.string.set_format, index + 1, set.reps ?: 0, set.weight ?: 0f),
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { onDeleteSet(set) }) {
                                Icon(
                                    Icons.Default.Close, 
                                    contentDescription = stringResource(R.string.delete),
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = onAddSet, 
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.add_set), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
