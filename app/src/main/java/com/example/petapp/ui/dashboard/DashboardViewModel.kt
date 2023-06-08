package com.example.petapp.ui.dashboard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.PetWaterEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.PetDashboardUiState
import com.example.petapp.model.PetStatsFormatter
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import com.example.petapp.model.util.toPetDashboardUiState
import com.example.petapp.ui.components.PetStatsEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    private val settingsDataRepository: UserSettingsDataRepository,
    private val application: Application
) : ViewModel(), PetStatsFormatter {


    var uiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(DashboardUiState.Success())
    val successUiState: StateFlow<DashboardUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value)

    init {
        viewModelScope.launch {
            petsDashboardRepository.getDashboard().collect { pets ->
                _successUiState.update { it.copy(pets = pets.toPetDashboardUiState()) }
            }
        }
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {it.copy(unit = unit)
                }
            }
        }
        uiState = DashboardUiState.Success()


    }

    override fun getPetAgeFormattedString(instant: Instant): String {
        return Formatters.getFormattedAgeString(instant = instant,context = application.applicationContext)
    }

    override fun getPetWeightFormattedString(weight: Double?): String {
        return  Formatters.getFormattedWeightString(weight = weight, unit = _successUiState.value.unit, context = application.applicationContext)
    }

    override fun getPetDimensionsFormattedString(value: Double?): String {
        return getPetDimensionsFormattedString(value = value)
    }

    fun waterIconOnClicked(pet: PetDashboardUiState) {
        val pets = _successUiState.value.pets.toMutableList()
        val index = pets.indexOf(pet)
        if (pets[index].petStat != PetStatsEnum.THIRST) pets[index] = pets[index].copy(petStat = PetStatsEnum.THIRST)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1){
            _successUiState.update {
                it.copy(
                    pets = pets
                )
            }
        }

    }

    fun onWaterChangedIconClicked(pet: PetDashboardUiState) {
        viewModelScope.launch {
            petsDashboardRepository.addPetWaterChangeData(
                PetWaterEntity(
                    id = UUID.randomUUID(),
                    pet_id = pet.petDashboard.petId,
                    measurementDate = Instant.now()
                )
            )
        }
    }

    fun foodIconOnClicked(pet: PetDashboardUiState) {
        val pets = _successUiState.value.pets.toMutableList()
        val index = pets.indexOf(pet)
        if (pets[index].petStat != PetStatsEnum.HUNGER) pets[index] = pets[index].copy(petStat = PetStatsEnum.HUNGER)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1){
            _successUiState.update {
                it.copy(
                    pets = pets
                )
            }
        }
    }

    fun activityIconOnClicked(pet: PetDashboardUiState) {
        val pets = _successUiState.value.pets.toMutableList()
        val index = pets.indexOf(pet)
        if (pets[index].petStat != PetStatsEnum.ACTIVITY) pets[index] = pets[index].copy(petStat = PetStatsEnum.ACTIVITY)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1){
            _successUiState.update {
                it.copy(
                    pets = pets
                )
            }
        }
    }
}