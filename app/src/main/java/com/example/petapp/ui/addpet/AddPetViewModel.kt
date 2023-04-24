package com.example.petapp.ui.addpet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.DateFormat


@OptIn(ExperimentalMaterial3Api::class)
class AddPetViewModel : ViewModel(), AddPetOperations {

    var uiState: AddPetUiState by mutableStateOf(AddPetUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(AddPetUiState.Success())
    val successUiState: StateFlow<AddPetUiState.Success> = _successUiState

    init {
        uiState = AddPetUiState.Success()
        /*
        TODO path depends on repo result (when repo will be add)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getSomething()
                uiState = AddPetUiState.Success
            } catch (e: Exception) {
                AddPetUiState.Error
            }
        }
        */
    }

    override fun onNameFieldValueChanged(name: String) {
        _successUiState.update {
            it.copy(nameFieldValue = name)
        }
    }

    override fun onNameFieldCancelClicked() {
        _successUiState.update {
            it.copy(nameFieldValue = "")
        }
    }

    override fun onDatePickerTextFieldValueChanged() {
        TODO("Not yet implemented")
    }

    override fun onDatePickerTextFieldClicked() {
        _successUiState.update {
            it.copy(datePickerOpenDialog = !it.datePickerOpenDialog)
        }
    }

    override fun datePickerOnDismissRequest() {
        _successUiState.update {
            it.copy(datePickerConfirmEnabled = false)
        }
    }

    override fun datePickerOnConfirmedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerTextFieldValue = DateFormat.getDateInstance(DateFormat.SHORT).format(it.datePickerState.selectedDateMillis),
                datePickerOpenDialog = false)
        }
    }

    override fun datePickerOnDismissedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerOpenDialog = false
            )
        }
    }

    override fun speciesMenuOnExpandedChanged(value: Boolean) {
        _successUiState.update {
            it.copy(
                speciesMenuExpanded = value
            )
        }
    }

    override fun speciesMenuOnDropdownMenuItemClicked(item: String) {
        _successUiState.update {
            it.copy(
                speciesMenuSelectedOptionText = item,
                speciesMenuExpanded = false
            )
        }
    }

    override fun speciesMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                speciesMenuExpanded = false
            )
        }
    }

    override fun breedMenuOnExpandedChanged(value: Boolean) {
        _successUiState.update {
            it.copy(
                breedMenuExpanded = value
            )
        }
    }

    override fun breedMenuOnDropdownMenuItemClicked(item: String) {
        _successUiState.update {
            it.copy(
                breedMenuSelectedOptionText = item,
                breedMenuExpanded = false
            )
        }
    }

    override fun breedMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                breedMenuExpanded = false
            )
        }
    }

    override fun onCancelButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onNavigateButtonClicked(stage: AddPetScreenStage) {
        _successUiState.update {
            it.copy(screenStage = stage)
        }
    }

    override fun onDoneButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(weightFieldValue = value)
        }
    }

    override fun onWeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(weightFieldValue = "")
        }
    }

    override fun onWeightFieldFocusCleared() {
        TODO("Not yet implemented")
    }

    override fun onHeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(heightFieldValue = value)
        }
    }

    override fun onHeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(heightFieldValue = "")
        }
    }

    override fun onHeightFieldFocusCleared() {
        TODO("Not yet implemented")
    }

    override fun onLengthFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(lengthFieldValue = value)
        }
    }

    override fun onLengthFieldCancelClicked() {
        _successUiState.update {
            it.copy(lengthFieldValue = "")
        }
    }

    override fun onLengthFieldFocusCleared() {
        TODO("Not yet implemented")
    }

    override fun onCircuitFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(circuitFieldValue = value)
        }
    }

    override fun onCircuitFieldCancelClicked() {
        _successUiState.update {
            it.copy(circuitFieldValue = "")
        }
    }

    override fun onCircuitFieldFocusCleared() {
        TODO("Not yet implemented")
    }

    override fun onDescriptionTextFieldValueChanged(description: String) {
        _successUiState.update {
            it.copy(descriptionFieldValue = description)
        }
    }
}