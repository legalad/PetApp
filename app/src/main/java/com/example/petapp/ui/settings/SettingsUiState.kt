package com.example.petapp.ui.settings

import com.example.android.datastore.UserPreferences.Language
import com.example.android.datastore.UserPreferences.Unit

sealed interface SettingsUiState {
    data class Success(
        val language: Language = Language.ENGLISH,
        val unit: Unit = Unit.IMPERIAL,
        val languageMenuExpanded: Boolean = false,
        val unitMenuExpanded: Boolean = false

    ) : SettingsUiState
    object Loading : SettingsUiState
    data class Error (val errorMessage: String) : SettingsUiState
}
