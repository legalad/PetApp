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
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class PetDetailsAddDimensionsViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
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
            initialValue = _successUiState.value)

    init {
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {it.copy(unit = unit)
                }
            }
        }
        uiState = PetDetailsAddDimensionsUiState.Success()
    }

    fun getPetId(): String {
        return petId
    }

    fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(weightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value),
                isWeightChanged = true)
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

    fun datePickerOnDismissRequest() {
        _successUiState.update {
            it.copy(datePickerOpenDialog = false)
        }
    }

    fun datePickerOnConfirmedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerTextFieldValue = DateFormat.getDateInstance(DateFormat.SHORT).format(it.datePickerState.selectedDateMillis),
                datePickerOpenDialog = false)
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

    fun onDoneButtonClicked() : Boolean  {
        var output: Boolean = true
        if (_successUiState.value.weightFieldValue.isNotEmpty())
        viewModelScope.launch (Dispatchers.IO){
                petsDashboardRepository.addPetWeight(
                    PetWeightEntity(
                        id = UUID.randomUUID(),
                        pet_id = UUID.fromString(petId),
                        measurementDate = Instant.ofEpochMilli(_successUiState.value.datePickerState.selectedDateMillis ?: Instant.now().toEpochMilli()),
                        value = _successUiState.value.weightFieldValue.toDouble()
                    )
                )
            }
         else {
            _successUiState.update {
                it.copy(weightErrorMessage = R.string.pet_form_weight_error_message, isWeightValid = false)
            }
            output = false
        }
        return output
    }

    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }




}