package com.example.petapp.ui.petdetails.addpetdata

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.R
import com.example.petapp.data.PetWeightEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
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
class PetDetailsAddWeightViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

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
                    it.copy(unit = unit)
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
        if (_successUiState.value.weightFieldValue.isNotEmpty())
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
                            .withMinute(_successUiState.value.timePickerState.minute).toInstant(),
                        value = _successUiState.value.weightFieldValue.toDouble()
                    )
                )
            }
        else {
            _successUiState.update {
                it.copy(
                    weightErrorMessage = R.string.pet_form_weight_error_message,
                    isWeightValid = false
                )
            }
            output = false
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


    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }


}