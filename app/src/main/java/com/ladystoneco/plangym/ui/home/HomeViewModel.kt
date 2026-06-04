package com.ladystoneco.plangym.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ladystoneco.plangym.data.local.entity.HabitEntity
import com.ladystoneco.plangym.data.local.entity.HabitLogEntity
import com.ladystoneco.plangym.data.local.entity.TaskEntity
import com.ladystoneco.plangym.domain.repository.HabitRepository
import com.ladystoneco.plangym.domain.repository.PlannerRepository
import com.ladystoneco.plangym.util.AppTheme
import com.ladystoneco.plangym.util.LocaleDataStore
import com.ladystoneco.plangym.util.LocalizationManager
import com.ladystoneco.plangym.util.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeUiState(
    val quote: String = "",
    val today: LocalDate = LocalDate.now(),
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val totalHabits: Int = 0,
    val completedHabits: Int = 0,
    val locale: String = "fa",
    val theme: AppTheme = AppTheme.GLASS_VIOLET,
    val greeting: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plannerRepository: PlannerRepository,
    private val habitRepository: HabitRepository,
    private val localeDataStore: LocaleDataStore,
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    private val _quoteIndex = MutableStateFlow(0)
    
    @Suppress("UNCHECKED_CAST")
    val uiState: StateFlow<HomeUiState> = combine(
        _quoteIndex,
        plannerRepository.observeAllTasks(),
        habitRepository.observeActiveHabits(),
        habitRepository.observeAllLogs(),
        localeDataStore.locale,
        themeDataStore.theme
    ) { args: Array<Any> ->
        val quoteIdx = args[0] as Int
        val tasks = args[1] as List<TaskEntity>
        val habits = args[2] as List<HabitEntity>
        val logs = args[3] as List<HabitLogEntity>
        val locale = args[4] as String
        val theme = args[5] as AppTheme

        val todayEpoch = LocalDate.now().toEpochDay()
        val completedHabitsCount = logs.count { it.dateEpochDay == todayEpoch && it.isCompleted }
        
        val quotes = if (locale == "fa") quotesFa else quotesEn
        
        val hour = java.time.LocalTime.now().hour
        val greeting = when {
            hour < 12 -> if (locale == "fa") "صبح بخیر" else "Good Morning"
            hour < 18 -> if (locale == "fa") "عصر بخیر" else "Good Afternoon"
            else -> if (locale == "fa") "شب بخیر" else "Good Evening"
        }
        
        HomeUiState(
            quote = quotes[quoteIdx % quotes.size],
            today = LocalDate.now(),
            totalTasks = tasks.size,
            completedTasks = tasks.count { it.isDone },
            totalHabits = habits.size,
            completedHabits = completedHabitsCount,
            locale = locale,
            theme = theme,
            greeting = greeting
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    private val quotesFa = listOf(
        "موفقیت مجموع تلاش‌های کوچکی است که هر روز تکرار می‌شوند.",
        "نظم پل بین اهداف و دستاوردهاست.",
        "امروز کاری انجام بده که در آینده از خودت تشکر کنی.",
        "سخت‌کوشی هیچ جایگزینی ندارد.",
        "توانایی شما برای یادگیری سریع‌تر از رقبایتان، تنها مزیت رقابتی پایدار شماست."
    )

    private val quotesEn = listOf(
        "Success is the sum of small efforts repeated day in and day out.",
        "Discipline is the bridge between goals and accomplishment.",
        "Do something today that your future self will thank you for.",
        "There is no substitute for hard work.",
        "Your ability to learn faster than your competitors is your only sustainable competitive advantage."
    )

    fun refreshQuote() {
        _quoteIndex.value = (0..4).random()
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val current = uiState.value.locale
            val next = if (current == "fa") "en" else "fa"
            localeDataStore.setLocale(next)
            LocalizationManager.setLocale(next)
        }
    }

    fun nextTheme() {
        viewModelScope.launch {
            val current = uiState.value.theme
            val next = when (current) {
                AppTheme.CALM_MINIMAL -> AppTheme.GLASS_VIOLET
                AppTheme.GLASS_VIOLET -> AppTheme.FITNESS_DARK
                AppTheme.FITNESS_DARK -> AppTheme.CALM_MINIMAL
            }
            themeDataStore.setTheme(next)
        }
    }
}
