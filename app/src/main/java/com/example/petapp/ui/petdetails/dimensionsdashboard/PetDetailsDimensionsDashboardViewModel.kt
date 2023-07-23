package com.example.petapp.ui.petdetails.dimensionsdashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.Async
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import com.example.petapp.model.util.toChartEntryModelProducer
import com.example.petapp.model.util.toListDateEntryList
import com.example.petapp.ui.petdetails.weightdashboard.ChartDateEntry
import com.example.petapp.ui.petdetails.weightdashboard.DataDisplayedType
import com.example.petapp.ui.petdetails.weightdashboard.ListDateEntry
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class PetDetailsDimensionsDashboardViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    settingsDataRepository: UserSettingsDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), MarkerVisibilityChangeListener {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _successUiState = MutableStateFlow(PetDetailsDimensionsDashboardUiState.Success())

    private val _asyncData = combine(
        petsDashboardRepository.getPetHeightHistory(petId = petId),
        petsDashboardRepository.getPetLengthHistory(petId = petId),
        petsDashboardRepository.getPetCircuitHistory(petId = petId),
        settingsDataRepository.getUnit(),
        petsDashboardRepository.getPetDetails(petId = petId)
    ) { height, length, circuit, unit, details ->
        _successUiState.update {
            it.copy(
                petName = details.name,
                petIdString = petId,
                unit = unit,
                heightHistoryListDateEntry = height.toListDateEntryList(),
                lengthHistoryListDateEntry = length.toListDateEntryList(),
                circuitHistoryListDateEntry = circuit.toListDateEntryList(),
                heightChartEntryModelProducer = height.toChartEntryModelProducer(unit = unit),
                lengthChartEntryModelProducer = length.toChartEntryModelProducer(unit = unit),
                circuitChartEntryModelProducer = circuit.toChartEntryModelProducer(unit = unit),
                heightSelectedDateEntry = ChartDateEntry(
                    localDate = height.lastOrNull()?.measurementDate ?: Instant.now(),
                    y = height.lastOrNull()?.value?.toFloat() ?: 0f,
                    x = 0f
                ),
                lengthSelectedDateEntry = ChartDateEntry(
                    localDate = length.lastOrNull()?.measurementDate ?: Instant.now(),
                    y = length.lastOrNull()?.value?.toFloat() ?: 0f,
                    x = 0f
                ),
                circuitSelectedDateEntry = ChartDateEntry(
                    localDate = circuit.lastOrNull()?.measurementDate ?: Instant.now(),
                    y = circuit.lastOrNull()?.value?.toFloat() ?: 0f,
                    x = 0f
                ),
                heightPersistentMarkerX = (height.size - 1).toFloat(),
                lengthPersistentMarkerX = (length.size - 1).toFloat(),
                circuitPersistentMarkerX = (circuit.size - 1).toFloat()
            )
        }
        if (height.isNotEmpty()) {
            _successUiState.update {
                it.copy(
                    heightSelectedDateEntry = ChartDateEntry(
                        localDate = height.last().measurementDate,
                        y = Formatters.getDimensionValue(height.last().value, unit = unit)
                            .toFloat(),
                        x = (height.size - 1).toFloat()
                    ),
                    heightPersistentMarkerX = (height.size - 1).toFloat()
                )
            }
        }
        if (length.isNotEmpty()) {
            _successUiState.update {
                it.copy(
                    lengthSelectedDateEntry = ChartDateEntry(
                        localDate = length.last().measurementDate,
                        y = Formatters.getDimensionValue(length.last().value, unit = unit)
                            .toFloat(),
                        x = (length.size - 1).toFloat()
                    ),
                    lengthPersistentMarkerX = (length.size - 1).toFloat()
                )
            }
        }
        if (circuit.isNotEmpty()) {
            _successUiState.update {
                it.copy(
                    circuitSelectedDateEntry = ChartDateEntry(
                        localDate = circuit.last().measurementDate,
                        y = Formatters.getDimensionValue(circuit.last().value, unit = unit)
                            .toFloat(),
                        x = (circuit.size - 1).toFloat()
                    ),
                    circuitPersistentMarkerX = (circuit.size - 1).toFloat()
                )
            }
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetDetailsDimensionsDashboardUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetDetailsDimensionsDashboardUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetDetailsDimensionsDashboardUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetDetailsDimensionsDashboardUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetDetailsDimensionsDashboardUiState.Loading
        )

    override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {
        super.onMarkerShown(marker, markerEntryModels)
        when (_successUiState.value.displayedDimension) {
            DisplayedDimension.HEIGHT -> _successUiState.update {
                it.copy(
                    heightPersistentMarkerX = markerEntryModels[0].entry.x,
                    heightSelectedDateEntry = markerEntryModels[0].entry as ChartDateEntry
                )
            }
            DisplayedDimension.LENGTH -> _successUiState.update {
                it.copy(
                    lengthPersistentMarkerX = markerEntryModels[0].entry.x,
                    lengthSelectedDateEntry = markerEntryModels[0].entry as ChartDateEntry
                )
            }
            DisplayedDimension.CIRCUIT -> _successUiState.update {
                it.copy(
                    circuitPersistentMarkerX = markerEntryModels[0].entry.x,
                    circuitSelectedDateEntry = markerEntryModels[0].entry as ChartDateEntry
                )
            }
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

    fun onDimensionIconClicked() {
        val displayedDimension = when (_successUiState.value.displayedDimension) {
            DisplayedDimension.HEIGHT -> DisplayedDimension.LENGTH
            DisplayedDimension.LENGTH -> DisplayedDimension.CIRCUIT
            DisplayedDimension.CIRCUIT -> DisplayedDimension.HEIGHT
        }

        _successUiState.update {
            it.copy(
                displayedDimension = displayedDimension
            )
        }
    }

    fun getSelectedHeightId(): String? {
        return try {
            _successUiState.value.heightHistoryListDateEntry[_successUiState.value.heightSelectedDateEntry.x.toInt()].id.toString()
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun getSelectedLengthId(): String? {
        return try {
            _successUiState.value.lengthHistoryListDateEntry[_successUiState.value.lengthSelectedDateEntry.x.toInt()].id.toString()
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun getSelectedCircuitId(): String? {
        return try {
            _successUiState.value.circuitHistoryListDateEntry[_successUiState.value.circuitSelectedDateEntry.x.toInt()].id.toString()
        } catch (e: IndexOutOfBoundsException) {
            null
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

    fun deleteSelectedDataItem() {
        viewModelScope.launch(Dispatchers.IO) {
            when (_successUiState.value.displayedDimension) {
                DisplayedDimension.HEIGHT -> getSelectedHeightId()?.let { id ->
                    petsDashboardRepository.getHeight(id)?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                }
                DisplayedDimension.LENGTH -> getSelectedLengthId()?.let { id ->
                    petsDashboardRepository.getLength(id)?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                }
                DisplayedDimension.CIRCUIT -> getSelectedCircuitId()?.let { id ->
                    petsDashboardRepository.getCircuit(id)?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                }
            }
        }
    }

    fun onDimensionItemClicked(item: ListDateEntry) {
        val selectedDimensionItem = _successUiState.value.selectedDimensionsItems
        if (selectedDimensionItem.isNotEmpty()) {
            val outputList = selectedDimensionItem.find {
                it.id == item.id
            }?.let {
                selectedDimensionItem.minus(it)
            } ?: selectedDimensionItem.plus(item)
            _successUiState.update {
                it.copy(
                    selectedDimensionsItems = outputList,
                )
            }
            _successUiState.update {
                when (_successUiState.value.displayedDimension) {
                    DisplayedDimension.HEIGHT -> it.copy(
                        heightHistoryListDateEntry = it.heightHistoryListDateEntry.map { height -> if(height.id == item.id) height.copy(isClicked = !height.isClicked) else height }
                    )
                    DisplayedDimension.LENGTH -> it.copy(
                        lengthHistoryListDateEntry = it.lengthHistoryListDateEntry.map { length -> if(length.id == item.id) length.copy(isClicked = !length.isClicked) else length }
                    )
                    DisplayedDimension.CIRCUIT -> it.copy(
                        circuitHistoryListDateEntry = it.circuitHistoryListDateEntry.map { circuit -> if(circuit.id == item.id) circuit.copy(isClicked = !circuit.isClicked) else circuit }
                    )
                }
            }
        }
    }

    fun onDimensionItemLongClicked(item: ListDateEntry) {
        val selectedDimensionItem = _successUiState.value.selectedDimensionsItems
        val outputList = selectedDimensionItem.find {
            it.id == item.id
        }?.let {
            selectedDimensionItem.minus(it)
        } ?: selectedDimensionItem.plus(item)
        _successUiState.update {
            it.copy(
                selectedDimensionsItems = outputList,
            )
        }
        _successUiState.update {
            when (_successUiState.value.displayedDimension) {
                DisplayedDimension.HEIGHT -> it.copy(
                    heightHistoryListDateEntry = it.heightHistoryListDateEntry.map { height -> if(height.id == item.id) height.copy(isClicked = !height.isClicked) else height }
                )
                DisplayedDimension.LENGTH -> it.copy(
                    lengthHistoryListDateEntry = it.lengthHistoryListDateEntry.map { length -> if(length.id == item.id) length.copy(isClicked = !length.isClicked) else length }
                )
                DisplayedDimension.CIRCUIT -> it.copy(
                    circuitHistoryListDateEntry = it.circuitHistoryListDateEntry.map { circuit -> if(circuit.id == item.id) circuit.copy(isClicked = !circuit.isClicked) else circuit }
                )
            }
        }
    }

    fun clearSelectedDimensionItems (delay: Long) {
        viewModelScope.launch {
            delay(delay)
            _successUiState.update { success ->
                success.copy(
                    selectedDimensionsItems = emptyList(),
                    heightHistoryListDateEntry = success.heightHistoryListDateEntry.map { it.copy(isClicked = false) },
                    lengthHistoryListDateEntry = success.lengthHistoryListDateEntry.map { it.copy(isClicked = false) },
                    circuitHistoryListDateEntry = success.circuitHistoryListDateEntry.map { it.copy(isClicked = false) }
                )
            }
        }
    }

    fun deletePetDimensions() {
        viewModelScope.launch(Dispatchers.IO) {
            _successUiState.value.selectedDimensionsItems.forEach {item ->
                when (_successUiState.value.displayedDimension) {
                    DisplayedDimension.HEIGHT -> petsDashboardRepository.getHeight(item.id.toString())?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                    DisplayedDimension.LENGTH -> petsDashboardRepository.getLength(item.id.toString())?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                    DisplayedDimension.CIRCUIT -> petsDashboardRepository.getCircuit(item.id.toString())?.let {
                        petsDashboardRepository.deletePetDimension(it)
                    }
                }

            }
            clearSelectedDimensionItems(0)
        }
    }
}