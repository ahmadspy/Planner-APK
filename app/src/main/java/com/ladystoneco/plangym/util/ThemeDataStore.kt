package com.ladystoneco.plangym.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

enum class AppTheme {
    CALM_MINIMAL, GLASS_VIOLET, FITNESS_DARK
}

@Singleton
class ThemeDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
    }

    val theme: Flow<AppTheme> = context.themeDataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppTheme.GLASS_VIOLET.name
        AppTheme.valueOf(themeName)
    }

    suspend fun setTheme(theme: AppTheme) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }
}
