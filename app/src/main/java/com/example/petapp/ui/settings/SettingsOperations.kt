package com.example.petapp.ui.settings

import com.example.android.datastore.UserPreferences.Language
import com.example.android.datastore.UserPreferences.Unit


interface SettingsOperations {
    suspend fun setLanguage(language: Language)
    suspend fun setUnit(unit: Unit)
}