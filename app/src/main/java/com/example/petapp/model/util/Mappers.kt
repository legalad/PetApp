package com.example.petapp.model.util

import com.example.android.datastore.UserPreferences
import com.example.petapp.ui.settings.SettingsUiState

fun UserPreferences.toSettingsUiState(): SettingsUiState.Success {
    return SettingsUiState.Success(
        language = language,
        unit = unit
    )
}