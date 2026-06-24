package com.ladystoneco.plangym.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ladystoneco.plangym.R
import com.ladystoneco.plangym.ui.util.GlassCard
import com.ladystoneco.plangym.ui.util.ModernCard
import com.ladystoneco.plangym.ui.util.SectionHeader
import com.ladystoneco.plangym.util.LocalizationManager
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPlanner: () -> Unit,
    onNavigateToWorkout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val quotes = stringArrayResource(R.array.quotes)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.app_name), 
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    ) 
                },
                actions = {
                    IconButton(onClick = { viewModel.nextTheme() }) {
                        Icon(Icons.Default.ColorLens, contentDescription = stringResource(R.string.change_theme))
                    }
                    IconButton(onClick = { viewModel.toggleLanguage() }) {
                        Icon(Icons.Default.Language, contentDescription = stringResource(R.string.language))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                if (uiState.greetingResId != 0) {
                    Text(
                        text = stringResource(uiState.greetingResId),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.height(8.dp))
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = LocalizationManager.formatDate(uiState.today, uiState.locale),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = quotes.getOrElse(uiState.quoteIndex) { "" },
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 26.sp
                            )
                        }
                        IconButton(
                            onClick = { viewModel.refreshQuote() },
                            modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.new_quote), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }

            item {
                SectionHeader(title = stringResource(R.string.calendar))
                Spacer(Modifier.height(12.dp))
                HorizontalCalendar(selectedDate = uiState.today, locale = uiState.locale)
            }

            item {
                SectionHeader(title = stringResource(R.string.today_status))
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ModernSummaryCard(
                        title = stringResource(R.string.tasks),
                        value = LocalizationManager.formatNumbers(uiState.completedTasks.toString(), uiState.locale),
                        total = LocalizationManager.formatNumbers(uiState.totalTasks.toString(), uiState.locale),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    ModernSummaryCard(
                        title = stringResource(R.string.habits),
                        value = LocalizationManager.formatNumbers(uiState.completedHabits.toString(), uiState.locale),
                        total = LocalizationManager.formatNumbers(uiState.totalHabits.toString(), uiState.locale),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SectionHeader(title = stringResource(R.string.quick_actions))
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuickActionCard(
                        title = stringResource(R.string.tasks),
                        icon = Icons.Default.Add,
                        onClick = onNavigateToPlanner,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionCard(
                        title = stringResource(R.string.start_workout),
                        icon = Icons.Default.PlayArrow,
                        onClick = onNavigateToWorkout,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModernCard(
        modifier = modifier.clickable { onClick() },
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HorizontalCalendar(selectedDate: LocalDate, locale: String) {
    val days = remember {
        (-3..3).map { selectedDate.plusDays(it.toLong()) }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        userScrollEnabled = false
    ) {
        items(days) { date ->
            val isSelected = date == selectedDate
            val backgroundColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
            val textColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onPrimary 
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(
                modifier = Modifier
                    .width(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale(locale)),
                    style = MaterialTheme.typography.labelSmall,
                    color = textColor.copy(alpha = 0.8f)
                )
                Text(
                    text = LocalizationManager.formatNumbers(date.dayOfMonth.toString(), locale),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun ModernSummaryCard(
    title: String,
    value: String,
    total: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    ModernCard(
        modifier = modifier,
        containerColor = color.copy(alpha = 0.1f)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Text(
                    text = "/$total",
                    style = MaterialTheme.typography.titleMedium,
                    color = color.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )
            }
        }
    }
}
