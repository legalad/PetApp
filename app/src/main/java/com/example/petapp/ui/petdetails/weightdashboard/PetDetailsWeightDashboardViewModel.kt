package com.example.petapp.ui.petdetails.weightdashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.Async
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import com.example.petapp.model.util.toListDateEntryList
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetDetailsWeightDashboardViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    settingsDataRepository: UserSettingsDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), MarkerVisibilityChangeListener {
    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _successUiState = MutableStateFlow(PetDetailsWeightDashboardUiState.Success())

    private val _asyncData = combine(
        petsDashboardRepository.getPetWeightHistory(petId = petId),
        settingsDataRepository.getUnit(),
        petsDashboardRepository.getPetDetails(petId = petId)
    ) { pets, unit, details->
        _successUiState.update {
            it.copy(
                petName = details.name,
                petIdString = details.petId.toString(),
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
            )
        }
        if (pets.isNotEmpty()) {
            _successUiState.update {
                it.copy(
                    selectedDateEntry = ChartDateEntry(
                        localDate = pets.last().measurementDate,
                        y = Formatters.getWeightValue(pets.last().value, unit = unit).toFloat(),
                        x = (pets.size - 1).toFloat()
                    ),
                    persistentMarkerX = (pets.size - 1).toFloat()
                )
            }
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetDetailsWeightDashboardUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetDetailsWeightDashboardUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetDetailsWeightDashboardUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetDetailsWeightDashboardUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetDetailsWeightDashboardUiState.Loading
        )


    override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
        super.onMarkerShown(marker, markerEntryModels)
        _successUiState.update {
            it.copy(
                persistentMarkerX = markerEntryModels[markerEntryModels.lastIndex].entry.x,
                selectedDateEntry = markerEntryModels[markerEntryModels.lastIndex].entry as ChartDateEntry
            )
        }
    }

    fun onChartIconClicked() {
        var dataDisplayedType = DataDisplayedType.LINE_CHART
        if (_successUiState.value.dataDisplayedType == dataDisplayedType) dataDisplayedType =
            DataDisplayedType.LIST

        _successUiState.update {
            it.copy(
                dataDisplayedType = dataDisplayedType
            )
        }
    }

    fun onDropdownMenuIconClicked() {
        _successUiState.update {
            it.copy(
                topAppBarMenuExpanded = !it.topAppBarMenuExpanded
            )
        }
    }

    fun dropdownMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                topAppBarMenuExpanded = false
            )
        }
    }

    fun getSelectedWeightId(): String? {
        return try {
            _successUiState.value.weightHistoryList[_successUiState.value.selectedDateEntry.x.toInt()].id.toString()
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun deleteWeightItem() {
        viewModelScope.launch(Dispatchers.IO) {
            getSelectedWeightId()?.let { id ->
                petsDashboardRepository.getWeight(id)?.let {
                    petsDashboardRepository.deletePetWeight(it)
                }
            }
        }
    }

    fun getPetId(): String {
        return petId
    }
}
