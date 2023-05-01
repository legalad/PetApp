package com.example.petapp.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.android.datastore.UserPreferences.Language
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsDataRepository: UserSettingsDataRepository
) : ViewModel(), SettingsOperations {
    var uiState: SettingsUiState by mutableStateOf(SettingsUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(SettingsUiState.Success())

    val successUiState: StateFlow<SettingsUiState.Success> = userSettingsDataRepository.getSettings().combine(_successUiState)
    {
        userPreferences, uiState ->
        SettingsUiState.Success(
            language = userPreferences.language,
            unit = userPreferences.unit,
            languageMenuExpanded = uiState.languageMenuExpanded,
            unitMenuExpanded = uiState.unitMenuExpanded
        )
    }
        .also { uiState = SettingsUiState.Success() }
        .catch { uiState = SettingsUiState.Error(it.message?: "Error") }
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
        initialValue = SettingsUiState.Success()
    )

    override suspend fun setLanguage(language: Language) {
        userSettingsDataRepository.setLanguage(language)
    }

    override suspend fun setUnit(unit: UserPreferences.Unit) {
        userSettingsDataRepository.setUnit(unit)
    }

    fun onDropdownMenuLanguageClicked(language: Language) {
        _successUiState.update {
            it.copy(
                languageMenuExpanded = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            setLanguage(language = language)
        }
    }

    fun onDropdownMenuUnitClicked(unit: UserPreferences.Unit) {
        _successUiState.update {
            it.copy(
                unitMenuExpanded = false
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            setUnit(unit = unit)
        }
    }

    fun languageMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                languageMenuExpanded = !it.languageMenuExpanded
            )
        }
    }

    fun unitMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                unitMenuExpanded = !it.unitMenuExpanded
            )
        }
    }

    fun languageMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                languageMenuExpanded = false
            )
        }
    }

    fun unitMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                unitMenuExpanded = false
            )
        }
    }

}