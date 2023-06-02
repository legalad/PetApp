package com.example.petapp.data

import com.example.android.datastore.UserPreferences
import com.example.android.datastore.UserPreferences.Language
import kotlinx.coroutines.flow.Flow

interface UserSettingsDataRepository {
    fun getSettings() : Flow<UserPreferences>
    fun getLanguage() : Flow<Language>
    suspend fun setLanguage(language: Language)
    fun getUnit(): Flow<UserPreferences.Unit>
    suspend fun setUnit(unit: UserPreferences.Unit)
}