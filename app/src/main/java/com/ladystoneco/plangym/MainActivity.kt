package com.ladystoneco.plangym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ladystoneco.plangym.ui.navigation.PlangymRoot
import com.ladystoneco.plangym.ui.theme.PlanGymTheme
import com.ladystoneco.plangym.util.AppTheme
import com.ladystoneco.plangym.util.LocaleDataStore
import com.ladystoneco.plangym.util.ThemeDataStore
import com.ladystoneco.plangym.worker.ReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var localeDataStore: LocaleDataStore
    
    @Inject
    lateinit var themeDataStore: ThemeDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        scheduleReminders()
        
        setContent {
            val locale by localeDataStore.locale.collectAsState(initial = "fa")
            val appTheme by themeDataStore.theme.collectAsState(initial = AppTheme.GLASS_VIOLET)
            val layoutDirection = if (locale == "fa") LayoutDirection.Rtl else LayoutDirection.Ltr
            
            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                PlanGymTheme(appTheme = appTheme) {
                    PlangymRoot()
                }
            }
        }
    }

    private fun scheduleReminders() {
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(request)
    }
}
