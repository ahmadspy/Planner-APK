package com.ladystoneco.plangym.ui.planner

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ladystoneco.plangym.R
import com.ladystoneco.plangym.data.local.entity.SubtaskEntity
import com.ladystoneco.plangym.data.local.relation.TaskWithSubtasks
import com.ladystoneco.plangym.domain.model.Priority
import com.ladystoneco.plangym.ui.util.ModernCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    onBack: () -> Unit,
    viewModel: ProjectDetailViewModel = hiltViewModel()
) {
    val uiState: ProjectDetailUiState by viewModel.uiState.collectAsState()
    var showAddTask by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.project?.name ?: stringResource(R.string.tasks),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
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
                onClick = { showAddTask = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_habit))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (uiState.tasks.isNotEmpty()) {
                    LinearProgressIndicator(
                        progress = { uiState.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        color = MaterialTheme.colorScheme.primary,
                        strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                }

                if (uiState.tasks.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            stringResource(R.string.no_tasks),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.tasks, key = { it.task.id }) { item: TaskWithSubtasks ->
                            ModernTaskCard(
                                item = item,
                                onToggleTask = { viewModel.toggleTask(item.task) },
                                onDeleteTask = { viewModel.deleteTask(item.task) },
                                onAddSubtask = { title -> viewModel.addSubtask(item.task.id, title) },
                                onToggleSubtask = { viewModel.toggleSubtask(it) },
                                onDeleteSubtask = { viewModel.deleteSubtask(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddTask) {
        AddTaskDialog(
            onDismiss = { showAddTask = false },
            onConfirm = { title, priority ->
                viewModel.addTask(title, priority)
                showAddTask = false
            }
        )
    }
}

@Composable
private fun ModernTaskCard(
    item: TaskWithSubtasks,
    onToggleTask: () -> Unit,
    onDeleteTask: () -> Unit,
    onAddSubtask: (String) -> Unit,
    onToggleSubtask: (SubtaskEntity) -> Unit,
    onDeleteSubtask: (SubtaskEntity) -> Unit
) {
    var expanded by rememberSaveable(item.task.id) { mutableStateOf(false) }
    var subtaskInput by rememberSaveable(item.task.id) { mutableStateOf("") }
    val task = item.task

    ModernCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isDone,
                    onCheckedChange = { onToggleTask() }
                )
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (task.isDone) FontWeight.Normal else FontWeight.Bold,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                PriorityDot(task.priority)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = stringResource(R.string.subtasks)
                    )
                }
                IconButton(onClick = onDeleteTask) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (expanded) {
                item.subtasks.forEach { sub ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 32.dp)
                    ) {
                        Checkbox(
                            checked = sub.isDone,
                            onCheckedChange = { onToggleSubtask(sub) }
                        )
                        Text(
                            text = sub.title,
                            style = MaterialTheme.typography.bodyMedium,
                            textDecoration = if (sub.isDone) TextDecoration.LineThrough else null,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onDeleteSubtask(sub) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 32.dp, top = 8.dp)
                ) {
                    OutlinedTextField(
                        value = subtaskInput,
                        onValueChange = { subtaskInput = it },
                        placeholder = { Text(stringResource(R.string.new_subtask)) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                    IconButton(
                        onClick = {
                            if (subtaskInput.isNotBlank()) {
                                onAddSubtask(subtaskInput)
                                subtaskInput = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = stringResource(R.string.add),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriorityDot(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> Color(0xFFFF5252)
        Priority.MEDIUM -> Color(0xFFFFAB40)
        Priority.LOW -> Color(0xFF448AFF)
        Priority.URGENT -> Color.Red
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .size(8.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Priority) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_task), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.task_title)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Text(stringResource(R.string.priority), style = MaterialTheme.typography.labelLarge)

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    val priorities = listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
                    priorities.forEachIndexed { index, priority ->
                        SegmentedButton(
                            selected = selectedPriority == priority,
                            onClick = { selectedPriority = priority },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = priorities.size
                            )
                        ) {
                            Text(priority.label())
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank()) onConfirm(title, selectedPriority) },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun Priority.label() = when (this) {
    Priority.LOW -> stringResource(R.string.low)
    Priority.MEDIUM -> stringResource(R.string.medium)
    Priority.HIGH -> stringResource(R.string.high)
    Priority.URGENT -> stringResource(R.string.urgent)
}
