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
import com.example.petapp.model.util.toListDateEntryList
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
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
) : ViewModel(), MarkerVisibilityChangeListener {
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
                        weightHistoryList = pets.toListDateEntryList(),
                        chartEntryModelProducer = ChartEntryModelProducer(
                            entryCollections = listOf(
                                pets.mapIndexed { index, petWeightEntity ->
                                    ChartDateEntry(
                                        localDate = petWeightEntity.measurementDate,
                                        y = Formatters.getWeightValue(petWeightEntity.value, unit)
                                            .toFloat(),
                                        x = index.toFloat()
                                    )
                                })
                        ),
                        selectedDateEntry = ChartDateEntry(localDate = pets.last().measurementDate, y = pets.last().value.toFloat(), x = 0f),
                        persistentMarkerX = (pets.size - 1).toFloat()
                    )
                }
            }.collect()
        }
        viewModelScope.launch {
            _successUiState.update {
                it.copy(petName = petsDashboardRepository.getPetDetails(petId = petId).firstOrNull()?.name ?: "",
                petIdString = petId)
            }
        }
        uiState = PetDetailsWeightDashboardUiState.Success()
    }

    override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
        super.onMarkerShown(marker, markerEntryModels)
        _successUiState.update {
            it.copy(
                persistentMarkerX = markerEntryModels[0].entry.x,
                selectedDateEntry = markerEntryModels[0].entry as ChartDateEntry
            )
        }
    }

    fun onChartIconClicked() {
        var dataDisplayedType = DataDisplayedType.LINE_CHART
        if (_successUiState.value.dataDisplayedType == dataDisplayedType) dataDisplayedType = DataDisplayedType.LIST

        _successUiState.update {
            it.copy(
                dataDisplayedType = dataDisplayedType
            )
        }
    }


    fun getPetId(): String {
        return petId
    }

    /*private val axisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { i, _ ->
        "${i.toInt()} Lorem ipsum"
    }*/
}
