package com.reviwh.githubuser.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getThemeSetting(): Flow<Boolean> = dataStore.data.map { it[THEME_KEY] ?: false }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { it[THEME_KEY] = isDarkMode }
    }

    companion object {
        private const val THEME_SETTING = "theme_setting"
        private val THEME_KEY = booleanPreferencesKey(THEME_SETTING)
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}