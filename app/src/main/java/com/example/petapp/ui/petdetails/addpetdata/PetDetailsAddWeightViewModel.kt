package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import androidx.compose.material3.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.Async
import com.example.petapp.data.PetWeightEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.WeightUnit
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
class PetDetailsAddWeightViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])
    private val weightId: String? = savedStateHandle["weightId"]

    private val _successUiState = MutableStateFlow(PetDetailsAddWeightUiState.Success())

    private val _asyncData = settingsDataRepository.getUnit().map { unit ->
        _successUiState.update {
            it.copy(
                unit = unit,
                weightFieldValuePlaceholder = when (unit) {
                    UserPreferences.Unit.METRIC -> R.string.util_unit_weight_kg
                    UserPreferences.Unit.IMPERIAL -> R.string.util_unit_weight_pounds
                    UserPreferences.Unit.UNRECOGNIZED -> R.string.util_unit_weight_kg
                },
                selectedWeightUnit = when (unit) {
                    UserPreferences.Unit.METRIC -> WeightUnit.KILOGRAMS
                    UserPreferences.Unit.IMPERIAL -> WeightUnit.POUNDS
                    UserPreferences.Unit.UNRECOGNIZED -> WeightUnit.KILOGRAMS
                }
            )
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetDetailsAddWeightUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetDetailsAddWeightUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetDetailsAddWeightUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetDetailsAddWeightUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetDetailsAddWeightUiState.Loading
        )

    init {
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {
                    it.copy(
                        unit = unit,
                        weightFieldValuePlaceholder = when (unit) {
                            UserPreferences.Unit.METRIC -> R.string.util_unit_weight_kg
                            UserPreferences.Unit.IMPERIAL -> R.string.util_unit_weight_pounds
                            UserPreferences.Unit.UNRECOGNIZED -> R.string.util_unit_weight_kg
                        },
                        selectedWeightUnit = when (unit) {
                            UserPreferences.Unit.METRIC -> WeightUnit.KILOGRAMS
                            UserPreferences.Unit.IMPERIAL -> WeightUnit.POUNDS
                            UserPreferences.Unit.UNRECOGNIZED -> WeightUnit.KILOGRAMS
                        }
                    )
                }
            }
        }
        //todo
        weightId?.let { weightId ->
            viewModelScope.launch(Dispatchers.IO) {
                val pet = petsDashboardRepository.getWeight(id = weightId)
                pet?.let { petWeight ->
                    _successUiState.update {
                        it.copy(
                            datePickerTextFieldValue = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                                .withLocale(
                                    Locale.getDefault()
                                ).withZone(ZoneId.systemDefault())
                                .format(petWeight.measurementDate),
                            datePickerState = DatePickerState(
                                initialSelectedDateMillis = petWeight.measurementDate.toEpochMilli(),
                                initialDisplayedMonthMillis = petWeight.measurementDate.toEpochMilli(),
                                yearRange = DatePickerDefaults.YearRange,
                                initialDisplayMode = DisplayMode.Picker
                            ),
                            timePickerState = TimePickerState(
                                initialHour = petWeight.measurementDate.atZone(ZoneId.systemDefault()).hour, //check
                                initialMinute = petWeight.measurementDate.atZone(ZoneId.systemDefault()).minute,
                                is24Hour = true
                            ),
                            weightFieldValue = Formatters.getWeightString(
                                petsDashboardRepository.getWeight(
                                    weightId
                                )?.value ?: 0.0, _successUiState.value.unit
                            )
                        )
                    }
                }
            }
        }
    }

    fun getPetId(): String {
        return petId
    }

    fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(
                weightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value),
                isWeightChanged = true
            )
        }
    }

    fun onWeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(weightFieldValue = "")
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

        Formatters.getMetricWeightValue(
            _successUiState.value.weightFieldValue,
            _successUiState.value.selectedWeightUnit
        ).apply { output = false }?.let { value ->
            output = true
            if (weightId.isNullOrEmpty()) {
                viewModelScope.launch(Dispatchers.IO) {
                    petsDashboardRepository.addPetWeight(
                        PetWeightEntity(
                            id = UUID.randomUUID(),
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
            } else {
                weightId?.let {
                    viewModelScope.launch(Dispatchers.IO) {
                        petsDashboardRepository.updateWeight(
                            PetWeightEntity(
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
                    return true
                }
                output = false
            }
        } ?: _successUiState.update {
            it.copy(
                weightErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_weight,
                isWeightValid = false
            )
        }
        return output
    }


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

    fun onWeightUnitPickerOnExpandedChange(value: Boolean) {
        _successUiState.update {
            it.copy(
                isWeightUnitPickerExpanded = value
            )
        }
    }

    fun onWeightUnitPickerOnDismissRequest() {
        _successUiState.update {
            it.copy(
                isWeightUnitPickerExpanded = false
            )
        }
    }

    fun onWeightUnitPickerDropdownMenuItemClicked(ordinal: Int) {
        _successUiState.update {
            it.copy(
                selectedWeightUnit = WeightUnit.values()[ordinal],
                isWeightUnitPickerExpanded = false
            )
        }
    }


    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }
}