package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.PetWeightEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
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

    var uiState: PetDetailsAddWeightUiState by mutableStateOf(PetDetailsAddWeightUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsAddWeightUiState.Success())

    val successUiState: StateFlow<PetDetailsAddWeightUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value
        )

    init {
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {
                    it.copy(
                        unit = unit,
                        weightFieldValuePlaceholder = when (unit) {
                            UserPreferences.Unit.METRIC -> R.string.util_unit_weight_kg
                            UserPreferences.Unit.IMPERIAL -> R.string.util_unit_weight_lbs
                            UserPreferences.Unit.UNRECOGNIZED -> R.string.util_unit_weight_kg
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
                            ),
                        )
                    }
                }
            }
        }
        uiState = PetDetailsAddWeightUiState.Success()
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
        val value =
            getMetricWeightValue(_successUiState.value.weightFieldValue, _successUiState.value.unit)
        value.apply { output = false }?.let { value ->
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
            }
            output = false
        }
    } ?: _successUiState.update {
            it.copy(
                weightErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_weight,
                isWeightValid = false
            )
        }
        Log.e("info", value.toString())
        Log.e("info", output.toString())
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


fun hideKeyboard() {
    _successUiState.update {
        it.copy(hideKeyboard = true)
    }
}

private fun getMetricWeightValue(
    valueStr: String,
    unit: UserPreferences.Unit
): Double? {
    if (valueStr.isNotEmpty()) {
        val value = try {
            valueStr.toDouble()
        } catch (e: TypeCastException) {
            Log.e("info", e.message.toString())
            return null
        }
        return when (unit) {
            UserPreferences.Unit.METRIC -> value
            UserPreferences.Unit.IMPERIAL -> Formatters.getMetricWeightValue(
                value,
                unit
            )
            UserPreferences.Unit.UNRECOGNIZED -> value
        }
    } else return null
}
}