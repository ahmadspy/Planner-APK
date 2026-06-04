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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class LocaleDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val LOCALE_KEY = stringPreferencesKey("locale")
    }

    val locale: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LOCALE_KEY] ?: "fa" // Default to Persian
    }

    suspend fun setLocale(locale: String) {
        context.dataStore.edit { preferences ->
            preferences[LOCALE_KEY] = locale
        }
    }
}
