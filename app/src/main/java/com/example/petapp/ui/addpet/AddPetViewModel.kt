package com.example.petapp.ui.addpet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.Species
import com.example.petapp.model.util.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.time.Instant
import java.util.*
import javax.inject.Inject


@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val dashboardRepository: PetsDashboardRepository
) : ViewModel(), AddPetDataValidation {

    var uiState: AddPetUiState by mutableStateOf(AddPetUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(AddPetUiState.Success())
    val successUiState: StateFlow<AddPetUiState.Success> = _successUiState

    init {
        uiState = AddPetUiState.Success()

    }

    fun onNameFieldValueChanged(name: String) {
        val value: String = if (name.isNotEmpty()) name
            .trimStart()
            .replace("\\s\\s".toRegex(), " ")
            .replaceFirst(name[0], name[0].uppercaseChar())
        else name

        validateName(value)

        _successUiState.update {
            it.copy(
                nameFieldValue = value,
                isNameChanged = true
            )
        }
    }

    fun onNameFieldCancelClicked() {
        _successUiState.update {
            it.copy(nameFieldValue = "", isNameChanged = false)
        }
    }

    fun onDatePickerTextFieldValueChanged(value: String) {

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
                datePickerOpenDialog = false,
                isBirthDateChanged = true)
        }
        validateBirthDate()
    }

    fun datePickerOnDismissedButtonClicked() {
        _successUiState.update {
            it.copy(
                datePickerOpenDialog = false
            )
        }
    }

    fun speciesMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                speciesMenuExpanded = !it.speciesMenuExpanded
            )
        }
    }

    fun speciesMenuOnDropdownMenuItemClicked(item: String) {
        _successUiState.update {
            it.copy(
                speciesMenuSelectedOptionText = item,
                speciesMenuExpanded = false
            )
        }
    }

    fun speciesMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                speciesMenuExpanded = false
            )
        }
    }

    fun breedMenuOnExpandedChanged() {
        _successUiState.update {
            it.copy(
                breedMenuExpanded = !it.breedMenuExpanded
            )
        }
    }

    fun breedMenuOnDropdownMenuItemClicked(item: String) {
        _successUiState.update {
            it.copy(
                breedMenuSelectedOptionText = item,
                breedMenuExpanded = false
            )
        }
    }

    fun breedMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                breedMenuExpanded = false
            )
        }
    }

    fun onCancelButtonClicked() {
        TODO("Not yet implemented")
    }


    fun onNavigateButtonClicked(stage: AddPetScreenStage) {
        _successUiState.update {
            it.copy(screenStage = stage)
        }
    }

    fun onGeneralDoneButtonClicked() {
        val uiStateValue = _successUiState.value

        validateName(uiStateValue.nameFieldValue)
        validateBirthDate()
        //cant validate species is bug in expanded value changed
        validateSpecies()

        //cant validate species is bug in expanded value changed
        if(uiStateValue.isNameValid && uiStateValue.isBirthDateValid /*&& uiStateValue.isSpeciesValid*/ && uiStateValue.isNameChanged && uiStateValue.isBirthDateChanged) {
            onNavigateButtonClicked(stage = AddPetScreenStage.Dimensions)
        }
    }

    fun onDimensionsDoneButtonClicked() {
        val uiStateValue = _successUiState.value
        validateWeight()

        if (uiStateValue.isWeightValid && uiStateValue.isWeightChanged
            && uiStateValue.isHeightValid
            && uiStateValue.isLengthValid
            && uiStateValue.isCircuitValid
        ) {
            onNavigateButtonClicked(stage = AddPetScreenStage.Final)
        }
    }

    fun onDoneButtonClicked() : Boolean {
        //future validation
        viewModelScope.launch(Dispatchers.IO) {
            val petUUID: UUID = UUID.randomUUID()
            dashboardRepository.addNewPet(
                PetGeneralEntity(
                    id = petUUID,
                    name = successUiState.value.nameFieldValue,
                    species = Species.CAT,
                    breed = "Tajski",
                    birthDate = Instant.ofEpochMilli(successUiState.value.datePickerState.selectedDateMillis?: Instant.now().toEpochMilli()),
                    description = successUiState.value.descriptionFieldValue,
                    imageUri = null
                ),
                PetWeightEntity(
                    id = UUID.randomUUID(),
                    pet_id = petUUID,
                    measurementDate = Instant.now(),
                    value = successUiState.value.weightFieldValue.toDouble() //handle possible errors
                ),
                if (successUiState.value.heightFieldValue.isNotEmpty()) PetHeightEntity(
                    id = UUID.randomUUID(),
                    pet_id = petUUID,
                    measurementDate = Instant.now(),
                    value = successUiState.value.heightFieldValue.toDouble()
                ) else null,
                if (successUiState.value.lengthFieldValue.isNotEmpty()) PetLengthEntity(
                    id = UUID.randomUUID(),
                    pet_id = petUUID,
                    measurementDate = Instant.now(),
                    value = successUiState.value.lengthFieldValue.toDouble()
                ) else null,
                if (successUiState.value.circuitFieldValue.isNotEmpty()) PetCircuitEntity(
                    id = UUID.randomUUID(),
                    pet_id = petUUID,
                    measurementDate = Instant.now(),
                    value = successUiState.value.circuitFieldValue.toDouble()
                ) else null
            )
        }
        return true
    }

    fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(weightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value),
                    isWeightChanged = true)
        }
        validateWeight()
    }

    fun onWeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(weightFieldValue = "")
        }
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

    fun onDescriptionTextFieldValueChanged(description: String) {
        _successUiState.update {
            it.copy(descriptionFieldValue = description)
        }
    }

    fun hideKeyboard() {
        _successUiState.update {
            it.copy(hideKeyboard = true)
        }
    }

    fun onFocusCleared() {
        _successUiState.update {
            it.copy(hideKeyboard = false)
        }
    }

    override fun validateName(value: String): Int {

        return if (value.isNotEmpty()) {
            _successUiState.update {
                it.copy(isNameValid = true,
                        nameErrorMessage = R.string.util_blank)
            }
            R.string.util_blank
        } else {
            _successUiState.update {
                it.copy(isNameValid = false,
                        nameErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_name_empty)
            }
            R.string.components_forms_text_field_supporting_text_error_message_name_empty
        }

    }

    override fun validateBirthDate() {

        //add validation

        if (!_successUiState.value.isBirthDateChanged) {
            _successUiState.update {
                it.copy(
                    birtDateErrorMessage = R.string.components_forms_date_picker_supporting_text_error_message_birth,
                    isBirthDateValid = false)
            }
        } else {
            _successUiState.update {
                it.copy(isBirthDateValid = true,
                        birtDateErrorMessage = R.string.util_blank)
            }
        }

    }

    override fun validateSpecies() {
        if (_successUiState.value.speciesMenuSelectedOptionText.isNotEmpty()) {
            _successUiState.update {
                it.copy(isSpeciesValid = true)
            }
        } else {
            _successUiState.update {
                it.copy(isSpeciesValid = false,
                    speciesErrorMessage = R.string.components_forms_menus_supporting_text_error_message_species)
            }
        }
    }

    override fun validateBreed() {
        TODO("Not yet implemented")
    }

    override fun validateWeight() {
    val value = _successUiState.value.weightFieldValue
    if (value.isNotEmpty()) {
            _successUiState.update {
                it.copy(isWeightValid = true,
                    weightErrorMessage = R.string.util_blank)
            }
        } else {
        _successUiState.update {
            it.copy(
                isWeightValid = false,
                weightErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_weight
            )
        }
    }
    }


    override fun validateHeight() {

    }

    override fun validateLength() {
        TODO("Not yet implemented")
    }

    override fun validateCircuit() {
        TODO("Not yet implemented")
    }

    override fun validateDescription() {
        TODO("Not yet implemented")
    }
}