package com.example.petapp.ui.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.Async
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
    private val application: Application,
    settingsDataRepository: UserSettingsDataRepository
) : ViewModel(), PetStatsFormatter {

    private val _successUiState = MutableStateFlow(DashboardUiState.Success())

    private val _asyncData = combine(
        petsDashboardRepository.getDashboard(),
        settingsDataRepository.getUnit()
    ) { dashboard, unit ->
        _successUiState.update {
            it.copy(
                pets = dashboard.toPetDashboardUiState(),
                unit = unit
            )
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<DashboardUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<DashboardUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> DashboardUiState.Loading
                is Async.Success -> success
                is Async.Error -> DashboardUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = DashboardUiState.Loading
        )

    override fun getPetAgeFormattedString(instant: Instant): String {
        return Formatters.getFormattedAgeString(
            instant = instant,
            context = application.applicationContext
        )
    }

    override fun getPetWeightFormattedString(weight: Double?): String {
        return Formatters.getFormattedWeightString(
            weight = weight,
            unit = _successUiState.value.unit,
            context = application.applicationContext
        )
    }

    override fun getPetDimensionsFormattedString(value: Double?): String {
        return getPetDimensionsFormattedString(value = value)
    }

    fun waterIconOnClicked(pet: PetDashboardUiState) {
        val pets = _successUiState.value.pets.toMutableList()
        val index = pets.indexOf(pet)
        if (pets[index].petStat != PetStatsEnum.THIRST) pets[index] =
            pets[index].copy(petStat = PetStatsEnum.THIRST)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1) {
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
        if (pets[index].petStat != PetStatsEnum.HUNGER) pets[index] =
            pets[index].copy(petStat = PetStatsEnum.HUNGER)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1) {
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
        if (pets[index].petStat != PetStatsEnum.ACTIVITY) pets[index] =
            pets[index].copy(petStat = PetStatsEnum.ACTIVITY)
        else pets[index] = pets[index].copy(petStat = PetStatsEnum.NONE)
        if (index != -1) {
            _successUiState.update {
                it.copy(
                    pets = pets
                )
            }
        }
    }
}