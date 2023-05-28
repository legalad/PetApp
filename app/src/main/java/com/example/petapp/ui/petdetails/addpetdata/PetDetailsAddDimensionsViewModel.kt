package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.*
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
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

    var uiState: PetDetailsAddDimensionsUiState by mutableStateOf(PetDetailsAddDimensionsUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsAddDimensionsUiState.Success())

    val successUiState: StateFlow<PetDetailsAddDimensionsUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value
        )

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
                    petHeightEntity = if (_successUiState.value.heightFieldValue.isNotEmpty())
                        PetHeightEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = _successUiState.value.heightFieldValue.toDouble()
                        ) else null,
                    petLengthEntity = if (_successUiState.value.lengthFieldValue.isNotEmpty())
                        PetLengthEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = _successUiState.value.lengthFieldValue.toDouble()
                        ) else null,
                    petCircuitEntity = if (_successUiState.value.circuitFieldValue.isNotEmpty())
                        PetCircuitEntity(
                            id = UUID.randomUUID(),
                            pet_id = UUID.fromString(petId),
                            measurementDate = buildInstant(),
                            value = _successUiState.value.circuitFieldValue.toDouble()
                        ) else null
                )
            }
        else {
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
