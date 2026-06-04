package com.ladystoneco.plangym.ui.habit

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ladystoneco.plangym.R
import com.ladystoneco.plangym.domain.model.Recurrence
import com.ladystoneco.plangym.ui.util.ModernCard
import com.ladystoneco.plangym.ui.util.SectionHeader
import com.ladystoneco.plangym.util.LocalizationManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits: List<HabitUi> by viewModel.habits.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.habits), 
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black
                    ) 
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAdd = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_habit))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SectionHeader(title = stringResource(R.string.habit_list))
                Spacer(Modifier.height(4.dp))
            }
            items(habits, key = { it.habit.id }) { ui ->
                ModernHabitCard(
                    ui = ui,
                    onToggle = { viewModel.toggleToday(ui) },
                    onDelete = { viewModel.deleteHabit(ui.habit) }
                )
            }
        }
    }

    if (showAdd) {
        AddHabitDialog(
            onDismiss = { showAdd = false },
            onConfirm = { name, recurrence ->
                viewModel.addHabit(name, recurrence)
                showAdd = false
            }
        )
    }
}

@Composable
private fun ModernHabitCard(
    ui: HabitUi,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val containerColor by animateColorAsState(
        if (ui.doneToday) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.surface
    )

    ModernCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = containerColor
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (ui.doneToday) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant
                    )
                    .clickable { onToggle() },
                contentAlignment = Alignment.Center
            ) {
                if (ui.doneToday) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    ui.habit.name, 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (ui.doneToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.streak_days, ui.streak),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            if (ui.streak > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = CircleShape,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = ui.streak.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = stringResource(R.string.delete), tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Recurrence) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var recurrence by remember { mutableStateOf(Recurrence.DAILY) }
    val options = listOf(Recurrence.DAILY, Recurrence.WEEKLY, Recurrence.MONTHLY)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.new_habit), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.habit_name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )
                Text(stringResource(R.string.priority) + ":", style = MaterialTheme.typography.labelLarge)
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    options.forEachIndexed { index, r ->
                        SegmentedButton(
                            selected = recurrence == r,
                            onClick = { recurrence = r },
                            shape = SegmentedButtonDefaults.itemShape(index, options.size)
                        ) { Text(r.label()) }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, recurrence) },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) { Text(stringResource(R.string.add)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}

@Composable
private fun Recurrence.label() = when (this) {
    Recurrence.DAILY -> stringResource(R.string.daily)
    Recurrence.WEEKLY -> stringResource(R.string.weekly)
    Recurrence.MONTHLY -> stringResource(R.string.monthly)
    else -> name
}
