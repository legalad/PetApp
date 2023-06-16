package com.example.petapp.ui.petdetails.fooddashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.Async
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PetFoodDashboardViewModel @Inject constructor(
    settingsDataRepository: UserSettingsDataRepository,
    petsDashboardRepository: PetsDashboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _successUiState = MutableStateFlow(PetFoodDashboardUiState.Success())

    private val _asyncData = combine(
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
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetFoodDashboardUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetFoodDashboardUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetFoodDashboardUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetFoodDashboardUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetFoodDashboardUiState.Loading
        )

    fun getPetId(): String {
        return petId
    }
}