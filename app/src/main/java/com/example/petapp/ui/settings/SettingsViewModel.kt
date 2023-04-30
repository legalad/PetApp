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
import com.example.petapp.model.util.toSettingsUiState
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

    val successUiState: StateFlow<SettingsUiState.Success> =  userSettingsDataRepository.getSettings()
        .map {
            it.toSettingsUiState()
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
        viewModelScope.launch(Dispatchers.IO) {
            setLanguage(language = language)
        }
    }

    fun onDropdownMenuUnitClicked(unit: UserPreferences.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            setUnit(unit = unit)
        }
    }


}