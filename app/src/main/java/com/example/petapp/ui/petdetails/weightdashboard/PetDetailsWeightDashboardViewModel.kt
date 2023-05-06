package com.example.petapp.ui.petdetails.weightdashboard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsWeightDashboardViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle["petId"])

    var uiState: PetDetailsWeightDashboardUiState by mutableStateOf(PetDetailsWeightDashboardUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsWeightDashboardUiState.Success())

    val successUiState: StateFlow<PetDetailsWeightDashboardUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value)

    init {
        viewModelScope.launch {
            petsDashboardRepository.getPetWeightHistory(petId = petId).collect { petHistory ->
                _successUiState.update { it.copy(weightHistoryList = petHistory) }
            }
        }
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {it.copy(unit = unit)
                }
            }
        }
        uiState = PetDetailsWeightDashboardUiState.Success()
    }

    fun getPetId(): String {
        return petId
    }
}