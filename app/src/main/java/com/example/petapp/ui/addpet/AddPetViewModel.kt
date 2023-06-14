package com.example.petapp.ui.addpet

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.*
import com.example.petapp.model.util.Formatters
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
    private val settingsDataRepository: UserSettingsDataRepository,
    private val dashboardRepository: PetsDashboardRepository
) : ViewModel(), AddPetDataValidation {

    var uiState: AddPetUiState by mutableStateOf(AddPetUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(AddPetUiState.Success())
    val successUiState: StateFlow<AddPetUiState.Success> = _successUiState

    init {
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {
                    when (unit) {
                        UserPreferences.Unit.METRIC -> it.copy(
                            selectedHeightUnit = DimensionUnit.METERS,
                            selectedLengthUnit = DimensionUnit.METERS,
                            selectedCircuitUnit = DimensionUnit.METERS,
                            selectedWeightUnit = WeightUnit.KILOGRAMS
                        )
                        UserPreferences.Unit.IMPERIAL -> it.copy(
                            selectedHeightUnit = DimensionUnit.FOOTS,
                            selectedLengthUnit = DimensionUnit.FOOTS,
                            selectedCircuitUnit = DimensionUnit.FOOTS,
                            selectedWeightUnit = WeightUnit.POUNDS
                        )
                        UserPreferences.Unit.UNRECOGNIZED -> it.copy(
                            selectedHeightUnit = DimensionUnit.METERS,
                            selectedLengthUnit = DimensionUnit.METERS,
                            selectedCircuitUnit = DimensionUnit.METERS,
                            selectedWeightUnit = WeightUnit.KILOGRAMS
                        )
                    }
                }
            }
        }
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
                datePickerTextFieldValue = DateFormat.getDateInstance(DateFormat.SHORT)
                    .format(it.datePickerState.selectedDateMillis),
                datePickerOpenDialog = false,
                isBirthDateChanged = true
            )
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

    fun onMaleIconButtonClicked(value: Boolean) {
        _successUiState.update {
            it.copy(
                maleIconChecked = value,
                femaleIconChecked = false,
                isGenderChanged = true
            )
        }
        validateGender()
    }

    fun onFemaleIconButtonClicked(value: Boolean) {
        _successUiState.update {
            it.copy(
                femaleIconChecked = value,
                maleIconChecked = false,
                isGenderChanged = true
            )
        }
        validateGender()
    }

    fun speciesMenuOnDropdownMenuItemClicked(item: Int) {
        val selectedSpecies = Species.values()[item]
        _successUiState.update {
            it.copy(
                speciesMenuSelectedOption = selectedSpecies,
                speciesMenuExpanded = false,
                breedMenuSelectedOption = selectedSpecies.breeds.lastOrNull(),
                breedMenuOptions = selectedSpecies.breeds,
                isSpeciesChanged = true,
                isSpeciesValid = true
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

    fun breedMenuOnDropdownMenuItemClicked(item: Int) {
        //TODO handle exceptions
        val value: Breed? = when (_successUiState.value.speciesMenuSelectedOption) {
            Species.DOG -> DogBreed.values()[item]
            Species.CAT -> CatBreed.values()[item]
            Species.FISH -> null
            Species.BIRD -> null
            Species.REPTILE -> null
            Species.RABBIT -> null
            Species.HAMSTER -> null
            Species.GUINEA_PIG -> null
            Species.FERRET -> null
            Species.NONE -> null
        }
        _successUiState.update {
            it.copy(
                breedMenuSelectedOption = value,
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
        validateGender()
        validateBirthDate()
        validateSpecies()
        validateWeight()

        if (uiStateValue.isNameValid && uiStateValue.isBirthDateValid && uiStateValue.isSpeciesValid && uiStateValue.isNameChanged && uiStateValue.isBirthDateChanged && uiStateValue.isSpeciesChanged && uiStateValue.isWeightValid && uiStateValue.isWeightChanged && uiStateValue.isGenderValid && uiStateValue.isGenderChanged) {
            onNavigateButtonClicked(stage = AddPetScreenStage.Dimensions)
        }
    }

    fun onDimensionsDoneButtonClicked() {
        val uiStateValue = _successUiState.value

        if (uiStateValue.isWeightValid && uiStateValue.isWeightChanged
            && uiStateValue.isHeightValid
            && uiStateValue.isLengthValid
            && uiStateValue.isCircuitValid
        ) {
            onNavigateButtonClicked(stage = AddPetScreenStage.Final)
        }
    }

    fun onDoneButtonClicked(): Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            val petUUID: UUID = UUID.randomUUID()
            dashboardRepository.addNewPet(
                PetGeneralEntity(
                    id = petUUID,
                    name = successUiState.value.nameFieldValue,
                    gender = if (_successUiState.value.maleIconChecked) PetGender.MALE else PetGender.FEMALE,
                    species = successUiState.value.speciesMenuSelectedOption,
                    breed = _successUiState.value.breedMenuSelectedOption?.toString(),
                    birthDate = Instant.ofEpochMilli(
                        successUiState.value.datePickerState.selectedDateMillis ?: Instant.now()
                            .toEpochMilli()
                    ),
                    description = successUiState.value.descriptionFieldValue,
                    imageUri = null
                ),
                Formatters.getMetricWeightValue(
                    _successUiState.value.weightFieldValue,
                    _successUiState.value.selectedWeightUnit
                )?.let {
                    PetWeightEntity(
                        id = UUID.randomUUID(),
                        pet_id = petUUID,
                        measurementDate = Instant.now(),
                        value = it
                    )
                },
                Formatters.getMetricDimensionValue(
                    _successUiState.value.heightFieldValue,
                    _successUiState.value.selectedHeightUnit
                )?.let {
                    PetHeightEntity(
                        id = UUID.randomUUID(),
                        pet_id = petUUID,
                        measurementDate = Instant.now(),
                        value = it
                    )
                },
                Formatters.getMetricDimensionValue(
                    _successUiState.value.lengthFieldValue,
                    _successUiState.value.selectedLengthUnit
                )?.let {
                    PetLengthEntity(
                        id = UUID.randomUUID(),
                        pet_id = petUUID,
                        measurementDate = Instant.now(),
                        value = it
                    )
                },
                Formatters.getMetricDimensionValue(
                    _successUiState.value.circuitFieldValue,
                    _successUiState.value.selectedCircuitUnit
                )?.let {
                    PetCircuitEntity(
                        id = UUID.randomUUID(),
                        pet_id = petUUID,
                        measurementDate = Instant.now(),
                        value = it
                    )
                }
            )
        }
        return true
    }

    fun onWeightFieldValueChanged(value: String) {
        _successUiState.update {
            it.copy(
                weightFieldValue = Validators.validateNumberToTwoDecimalPlaces(value),
                isWeightChanged = true
            )
        }
        validateWeight()
    }

    fun onWeightFieldCancelClicked() {
        _successUiState.update {
            it.copy(weightFieldValue = "")
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
                it.copy(
                    isNameValid = true,
                    nameErrorMessage = R.string.util_blank
                )
            }
            R.string.util_blank
        } else {
            _successUiState.update {
                it.copy(
                    isNameValid = false,
                    nameErrorMessage = R.string.components_forms_text_field_supporting_text_error_message_name_empty
                )
            }
            R.string.components_forms_text_field_supporting_text_error_message_name_empty
        }

    }

    override fun validateGender() {
        if ((_successUiState.value.maleIconChecked || _successUiState.value.femaleIconChecked) && _successUiState.value.isGenderChanged) {
            _successUiState.update {
                it.copy(isGenderValid = true)
            }
        } else {
            _successUiState.update {
                it.copy(
                    isGenderValid = false
                )
            }
        }
    }

    override fun validateBirthDate() {

        //add validation

        if (!_successUiState.value.isBirthDateChanged) {
            _successUiState.update {
                it.copy(
                    birtDateErrorMessage = R.string.components_forms_date_picker_supporting_text_error_message_birth,
                    isBirthDateValid = false
                )
            }
        } else {
            _successUiState.update {
                it.copy(
                    isBirthDateValid = true,
                    birtDateErrorMessage = R.string.util_blank
                )
            }
        }

    }

    override fun validateSpecies() {
        Log.e("info", _successUiState.value.speciesMenuSelectedOption.name)
        if (_successUiState.value.speciesMenuSelectedOption != Species.NONE) {
            _successUiState.update {
                it.copy(isSpeciesValid = true)
            }
            Log.e("info", "true")
        } else {
            _successUiState.update {
                it.copy(
                    isSpeciesValid = false,
                    speciesErrorMessage = R.string.components_forms_menus_supporting_text_error_message_species
                )
            }
            Log.e("info", "false")
        }
    }

    override fun validateBreed() {
        TODO("Not yet implemented")
    }

    override fun validateWeight() {
        val value = _successUiState.value.weightFieldValue
        if (value.isNotEmpty()) {
            _successUiState.update {
                it.copy(
                    isWeightValid = true,
                    weightErrorMessage = R.string.util_blank
                )
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