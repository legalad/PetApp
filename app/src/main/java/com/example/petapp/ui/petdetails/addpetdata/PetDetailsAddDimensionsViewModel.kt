package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.DimensionUnit
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import com.example.petapp.model.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class PetDetailsAddDimensionsViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    private val settingsDataRepository: UserSettingsDataRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val petId: String = checkNotNull(savedStateHandle["petId"])
    private val heightId: String? = savedStateHandle["heightId"]
    private val lengthId: String? = savedStateHandle["lengthId"]
    private val circuitId: String? = savedStateHandle["circuitId"]


    var uiState: PetDetailsAddDimensionsUiState by mutableStateOf(PetDetailsAddDimensionsUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsAddDimensionsUiState.Success())

    val successUiState: StateFlow<PetDetailsAddDimensionsUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value
        )

    init {
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {
                    when (unit) {
                        UserPreferences.Unit.METRIC -> it.copy(
                            unit = unit,
                            defaultFiledValuePlaceholder = R.string.util_unit_dimension_meters,
                            selectedHeightUnit = DimensionUnit.METERS,
                            selectedLengthUnit = DimensionUnit.METERS,
                            selectedCircuitUnit = DimensionUnit.METERS,
                            selectedUpdatedUnit = DimensionUnit.METERS
                        )
                        UserPreferences.Unit.IMPERIAL -> it.copy(
                            unit = unit,
                            defaultFiledValuePlaceholder = R.string.util_unit_dimension_foot,
                            selectedHeightUnit = DimensionUnit.FOOTS,
                            selectedLengthUnit = DimensionUnit.FOOTS,
                            selectedCircuitUnit = DimensionUnit.FOOTS,
                            selectedUpdatedUnit = DimensionUnit.FOOTS
                        )
                        UserPreferences.Unit.UNRECOGNIZED -> it.copy(
                            unit = unit,
                            defaultFiledValuePlaceholder = R.string.util_unit_dimension_meters,
                            selectedHeightUnit = DimensionUnit.METERS,
                            selectedLengthUnit = DimensionUnit.METERS,
                            selectedCircuitUnit = DimensionUnit.METERS,
                            selectedUpdatedUnit = DimensionUnit.METERS
                        )
                    }
                }
            }
        }
        heightId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val pet = petsDashboardRepository.getHeight(id = id)
                pet?.let { petHeight ->
                    _successUiState.update {
                        it.copy(
                            datePickerTextFieldValue = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(
                                    Locale.getDefault()
                                ).withZone(ZoneId.systemDefault())
                                .format(petHeight.measurementDate),
                            datePickerState = DatePickerState(
                                initialSelectedDateMillis = petHeight.measurementDate.toEpochMilli(),
                                initialDisplayedMonthMillis = petHeight.measurementDate.toEpochMilli(),
                                yearRange = DatePickerDefaults.YearRange,
                                initialDisplayMode = DisplayMode.Picker
                            ),
                            timePickerState = TimePickerState(
                                initialHour = petHeight.measurementDate.atZone(ZoneId.systemDefault()).hour, //check
                                initialMinute = petHeight.measurementDate.atZone(ZoneId.systemDefault()).minute,
                                is24Hour = true
                            ),
                            updatedDimensionFieldValue = Formatters.getDimensionString(
                                petsDashboardRepository.getHeight(
                                    id
                                )?.value ?: 0.0, _successUiState.value.unit
                            ),
                            updatedFieldLeadingIcon = R.drawable.baseline_height_24,
                            updatedFieldLabel = R.string.components_forms_text_field_label_pet_height
                        )
                    }
                }
            }
        }
        lengthId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val pet = petsDashboardRepository.getLength(id = id)
                pet?.let { petLength ->
                    _successUiState.update {
                        it.copy(
                            datePickerTextFieldValue = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(
                                    Locale.getDefault()
                                ).withZone(ZoneId.systemDefault())
                                .format(petLength.measurementDate),
                            datePickerState = DatePickerState(
                                initialSelectedDateMillis = petLength.measurementDate.toEpochMilli(),
                                initialDisplayedMonthMillis = petLength.measurementDate.toEpochMilli(),
                                yearRange = DatePickerDefaults.YearRange,
                                initialDisplayMode = DisplayMode.Picker
                            ),
                            timePickerState = TimePickerState(
                                initialHour = petLength.measurementDate.atZone(ZoneId.systemDefault()).hour, //check
                                initialMinute = petLength.measurementDate.atZone(ZoneId.systemDefault()).minute,
                                is24Hour = true
                            ),
                            updatedDimensionFieldValue = Formatters.getDimensionString(
                                petsDashboardRepository.getLength(
                                    id
                                )?.value ?: 0.0, _successUiState.value.unit
                            ),
                            updatedFieldLeadingIcon = R.drawable.width_24,
                            updatedFieldLabel = R.string.components_forms_text_field_label_pet_length
                        )
                    }
                }
            }
        }
        circuitId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val pet = petsDashboardRepository.getCircuit(id = id)
                pet?.let { petCircuit ->
                    _successUiState.update {
                        it.copy(
                            datePickerTextFieldValue = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(
                                    Locale.getDefault()
                                ).withZone(ZoneId.systemDefault())
                                .format(petCircuit.measurementDate),
                            datePickerState = DatePickerState(
                                initialSelectedDateMillis = petCircuit.measurementDate.toEpochMilli(),
                                initialDisplayedMonthMillis = petCircuit.measurementDate.toEpochMilli(),
                                yearRange = DatePickerDefaults.YearRange,
                                initialDisplayMode = DisplayMode.Picker
                            ),
                            timePickerState = TimePickerState(
                                initialHour = petCircuit.measurementDate.atZone(ZoneId.systemDefault()).hour, //check
                                initialMinute = petCircuit.measurementDate.atZone(ZoneId.systemDefault()).minute,
                                is24Hour = true
                            ),
                            updatedDimensionFieldValue = Formatters.getDimensionString(
                                petsDashboardRepository.getCircuit(
                                    id
                                )?.value ?: 0.0, _successUiState.value.unit
                            ),
                            updatedFieldLeadingIcon = R.drawable.restart_alt_24,
                            updatedFieldLabel = R.string.components_forms_text_field_label_pet_circuit
                        )
                    }
                }
            }
        }
        uiState = PetDetailsAddDimensionsUiState.Success()
    }

    fun getPetId(): String {
        return petId
    }

    fun onHeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(heightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value))
        }
    }

    fun onHeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(heightFieldValue = "")
        }
    }

    fun onHeightUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isHeightUnitPickerExpanded = value
            )
        }
    }

    fun onHeightUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isHeightUnitPickerExpanded = false
            )
        }
    }

    fun onHeightUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedHeightUnit = DimensionUnit.values()[ordinal],
                isHeightUnitPickerExpanded = false
            )
        }
    }

    fun onLengthFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(lengthFieldValue = Validators.validateNumberToTwoDecimalPlaces(value))
        }
    }

    fun onLengthFieldCancelClicked() {
        _successUiState.update {
            it.copy(lengthFieldValue = "")
        }
    }

    fun onLengthUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isLengthUnitPickerExpanded = value
            )
        }
    }

    fun onLengthUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isLengthUnitPickerExpanded = false
            )
        }
    }

    fun onLengthUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedLengthUnit = DimensionUnit.values()[ordinal],
                isLengthUnitPickerExpanded = false
            )
        }
    }

    fun onCircuitFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(circuitFieldValue = Validators.validateNumberToTwoDecimalPlaces(value))
        }
    }

    fun onCircuitFieldCancelClicked() {
        _successUiState.update {
            it.copy(circuitFieldValue = "")
        }
    }

    fun onCircuitUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isCircuitUnitPickerExpanded = value
            )
        }
    }

    fun onCircuitUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isCircuitUnitPickerExpanded = false
            )
        }
    }

    fun onCircuitUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedCircuitUnit = DimensionUnit.values()[ordinal],
                isCircuitUnitPickerExpanded = false
            )
        }
    }

    fun onUpdatedFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(updatedDimensionFieldValue = Validators.validateNumberToTwoDecimalPlaces(value))
        }
    }

    fun onUpdatedFieldCancelClicked() {
        _successUiState.update {
            it.copy(updatedDimensionFieldValue = "")
        }
    }

    fun onUpdatedUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isUpdatedUnitPickerExpanded = value
            )
        }
    }

    fun onUpdatedUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isUpdatedUnitPickerExpanded = false
            )
        }
    }

    fun onUpdatedUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedUpdatedUnit = DimensionUnit.values()[ordinal],
                isUpdatedUnitPickerExpanded = false
            )
        }
    }

    fun onDatePickerTextFieldClicked() {
        _successUiState.update {
            it.copy(datePickerOpenDialog = !it.datePickerOpenDialog)
        }
    }

    fun onDatePickerTextFieldValueChanged(value: String) {

    }

    fun datePickerOnDismissRequest() {
        _successUiState.update {
            it.copy(datePickerOpenDialog = false)
        }
    }

    fun datePickerOnConfirmedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerTextFieldValue = DateFormat.getDateInstance(DateFormat.SHORT)
                    .format(it.datePickerState.selectedDateMillis),
                datePickerOpenDialog = false
            )
        }
    }

    fun datePickerOnDismissedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerOpenDialog = false
            )
        }
    }

    fun onFocusCleared() {
        _successUiState.update {
            it.copy(hideKeyboard = false)
        }
    }

    fun onDoneButtonClicked(): Boolean {
        var output = true
        if (_successUiState.value.heightFieldValue.isNotEmpty() || _successUiState.value.lengthFieldValue.isNotEmpty() || _successUiState.value.circuitFieldValue.isNotEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                petsDashboardRepository.addPetDimensions(
                    petHeightEntity = Formatters.getMetricDimensionValue(_successUiState.value.heightFieldValue, _successUiState.value.selectedHeightUnit)?.let {
                        PetHeightEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = it
                        )
                    },
                    petLengthEntity = Formatters.getMetricDimensionValue(_successUiState.value.lengthFieldValue, _successUiState.value.selectedLengthUnit)?.let {
                        PetLengthEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = it
                        )
                    },
                    petCircuitEntity = Formatters.getMetricDimensionValue(_successUiState.value.circuitFieldValue, _successUiState.value.selectedCircuitUnit)?.let {
                        PetCircuitEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = it
                        )
                    }
                )
            }
        else if (_successUiState.value.updatedDimensionFieldValue.isNotEmpty()) {
            Formatters.getMetricDimensionValue(_successUiState.value.updatedDimensionFieldValue, _successUiState.value.selectedUpdatedUnit)?.let { value ->
                viewModelScope.launch {
                    heightId?.let {
                        petsDashboardRepository.updateDimension(
                            PetHeightEntity(
                                id = UUID.fromString(it),
                                pet_id = UUID.fromString(petId),
                                measurementDate = Instant.ofEpochMilli(
                                    _successUiState.value.datePickerState.selectedDateMillis
                                        ?: Instant.now().toEpochMilli()
                                ).atZone(
                                    ZoneId.systemDefault()
                                ).withHour(_successUiState.value.timePickerState.hour)
                                    .withMinute(_successUiState.value.timePickerState.minute)
                                    .toInstant(),
                                value = value
                            )
                        )
                    }
                    lengthId?.let {
                        petsDashboardRepository.updateDimension(
                            PetLengthEntity(
                                id = UUID.fromString(it),
                                pet_id = UUID.fromString(petId),
                                measurementDate = Instant.ofEpochMilli(
                                    _successUiState.value.datePickerState.selectedDateMillis
                                        ?: Instant.now().toEpochMilli()
                                ).atZone(
                                    ZoneId.systemDefault()
                                ).withHour(_successUiState.value.timePickerState.hour)
                                    .withMinute(_successUiState.value.timePickerState.minute)
                                    .toInstant(),
                                value = value
                            )
                        )
                    }
                    circuitId?.let {
                        petsDashboardRepository.updateDimension(
                            PetCircuitEntity(
                                id = UUID.fromString(it),
                                pet_id = UUID.fromString(petId),
                                measurementDate = Instant.ofEpochMilli(
                                    _successUiState.value.datePickerState.selectedDateMillis
                                        ?: Instant.now().toEpochMilli()
                                ).atZone(
                                    ZoneId.systemDefault()
                                ).withHour(_successUiState.value.timePickerState.hour)
                                    .withMinute(_successUiState.value.timePickerState.minute)
                                    .toInstant(),
                                value = value
                            )
                        )
                    }
                }
            }

        } else {
            _successUiState.update {
                it.copy(
                    isFormValid = false
                )
            }
            output = false
        }
        return output
    }

    private fun buildInstant() = Instant.ofEpochMilli(
        _successUiState.value.datePickerState.selectedDateMillis
            ?: Instant.now().toEpochMilli()
    ).atZone(
        ZoneId.systemDefault()
    ).withHour(_successUiState.value.timePickerState.hour)
        .withMinute(_successUiState.value.timePickerState.minute).toInstant()


    fun onTimePickerTextFieldClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = !it.showTimePicker
            )
        }
    }

    fun onTimePickerDialogCancelClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = false
            )
        }
    }

    fun onTimePickerDialogConfirmClicked() {
        _successUiState.update {
            it.copy(
                showTimePicker = false
            )
        }
    }

    fun onTimePickerDialogSwitchIconClicked() {
        _successUiState.update {
            it.copy(
                showingPicker = !it.showingPicker
            )
        }
    }

    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }
}