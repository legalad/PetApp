package com.example.petapp.ui.petdetails

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.PetWaterEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.PetStatsFormatter
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.inject.Inject

@HiltViewModel
open class PetDetailsViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), PetStatsFormatter {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    var uiState: PetDetailsUiState by mutableStateOf(PetDetailsUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsUiState.Success())

    val successUiState: StateFlow<PetDetailsUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value)

    init {
        /*viewModelScope.launch {
            petsDashboardRepository.getPetDetails(petId = petId).collect { pet ->
                _successUiState.update { it.copy(pet = pet) }
            }
        }
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {it.copy(unit = unit)
                }
            }
        }
        viewModelScope.launch {
            petsDashboardRepository.getPetLastWaterChanged(petId = petId).collect { lastChanged ->
                _successUiState.update { success ->
                    success.copy(
                        lastWaterChanged = lastChanged?.let { Duration.between(it.measurementDate, Instant.now())
                        }
                    )
                }
            }
        }*/

        viewModelScope.launch {
            combine(
                petsDashboardRepository.getPetDetails(petId = petId),
                petsDashboardRepository.getPetMeals(petId = petId),
                petsDashboardRepository.getPetLastWaterChanged(petId = petId),
                settingsDataRepository.getUnit()
            ) { details, meals, water, unit ->
                _successUiState.update { success ->
                    success.copy(
                        pet = details,
                        petMeals = meals,
                        lastWaterChanged = water?.let {
                            Duration.between(
                                it.measurementDate,
                                Instant.now()
                            )
                        },
                        unit = unit
                    )
                }
            }.collect()
        }
        uiState = PetDetailsUiState.Success()
    }

    override fun getPetAgeFormattedString(instant: Instant): String {
        return Formatters.getFormattedAgeString(instant= instant, context = application.applicationContext)
    }

    override fun getPetWeightFormattedString(weight: Double): String {
        return Formatters.getFormattedWeightString(weight = weight, unit = _successUiState.value.unit, context = application.applicationContext)
    }

    override fun getPetDimensionsFormattedString(value: Double): String {
        return Formatters.getFormattedDimensionString(value = value, unit = _successUiState.value.unit, context = application.applicationContext)
    }

    fun onWaterRefillIconClicked() {
        viewModelScope.launch {
            petsDashboardRepository.addPetWaterChangeData(
                PetWaterEntity(
                    id = UUID.randomUUID(),
                    pet_id = UUID.fromString(petId),
                    measurementDate = Instant.now()
                )
            )
        }
    }
}