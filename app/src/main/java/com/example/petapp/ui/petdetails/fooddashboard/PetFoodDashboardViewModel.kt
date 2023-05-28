package com.example.petapp.ui.petdetails.fooddashboard

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
class PetFoodDashboardViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    var uiState: PetFoodDashboardUiState by mutableStateOf(
        PetFoodDashboardUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetFoodDashboardUiState.Success())

    val successUiState: StateFlow<PetFoodDashboardUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value
        )

    init {
        viewModelScope.launch {
            combine(
                petsDashboardRepository.getPetMeals(petId = petId),
                petsDashboardRepository.getPetDetails(petId = petId),
                settingsDataRepository.getUnit()
            ) {
                meals, pet, unit ->
                _successUiState.update {
                    it.copy(
                        petName = pet.name,
                        petMeals = meals
                    )
                }
            }.collect()
        }
    }

    fun getPetId(): String {
        return petId
    }
}