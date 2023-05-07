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
import com.example.petapp.model.util.Formatters
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
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
            initialValue = _successUiState.value
        )

    init {
        viewModelScope.launch {
            combine(
                petsDashboardRepository.getPetWeightHistory(petId = petId),
                settingsDataRepository.getUnit()
            ) { pets, unit ->
                _successUiState.update {
                    it.copy(
                        unit = unit,
                        weightHistoryList = pets,
                        chartEntryModelProducer = ChartEntryModelProducer(
                            entryCollections = listOf(
                                pets.mapIndexed { index, petWeightEntity ->
                                    DateEntry(
                                        localDate = petWeightEntity.measurementDate,
                                        y = Formatters.getWeightValue(petWeightEntity.value, unit)
                                            .toFloat(),
                                        x = index.toFloat()
                                    )
                                })
                        )
                    )
                }
            }.collect()
        }
           /* petsDashboardRepository.getPetWeightHistory(petId = petId).collect { petHistory ->
                _successUiState.update {
                    it.copy(
                        weightHistoryList = petHistory,
                        chartEntryModelProducer = ChartEntryModelProducer(
                            entryCollections = listOf(
                                petHistory.mapIndexed { index, petWeightEntity ->
                                    DateEntry(
                                        localDate = petWeightEntity.measurementDate,
                                        y = Formatters.getWeightValue(petWeightEntity.value, successUiState.value.unit).toFloat(),
                                        x = index.toFloat()
                                    )
                                })
                        )
                    )
                }
            }
        }*/
        /*viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {
                    it.copy(unit = unit)
                }
            }
        }*/
        uiState = PetDetailsWeightDashboardUiState.Success()
    }


    fun getPetId(): String {
        return petId
    }

    /*private val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { i, _ ->
        "${i.toInt()} Lorem ipsum"
    }*/
}
