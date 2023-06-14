package com.example.petapp.ui.addpet

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.petapp.R
import com.example.petapp.model.Breed
import com.example.petapp.model.DimensionUnit
import com.example.petapp.model.Species
import com.example.petapp.model.WeightUnit
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

sealed interface AddPetScreenStage {
    object General: AddPetScreenStage
    object Dimensions: AddPetScreenStage
    object Final: AddPetScreenStage
}

sealed interface AddPetUiState {
    data class Success @OptIn(ExperimentalMaterial3Api::class) constructor(
        val nameFieldValue: String = "",
        val datePickerTextFieldValue: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(
            Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now())/*DateFormat.getDateInstance(DateFormat.SHORT).format(Instant.now().epochSecond)*/,
        val datePickerOpenDialog: Boolean = false,
        val datePickerState: DatePickerState = DatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli(),
            initialDisplayedMonthMillis = Instant.now().toEpochMilli(),
            yearRange = DatePickerDefaults.YearRange,
            initialDisplayMode = DisplayMode.Picker
        ),
        val datePickerConfirmEnabled: Boolean = true,
        val speciesMenuOptions: List<Species> = Species.values().toList(),
        val speciesMenuExpanded: Boolean = false,
        val speciesMenuSelectedOption: Species = Species.NONE,
        val breedMenuEnabled: Boolean = true,
        val breedMenuOptions: List<Breed> = emptyList(),
        val breedMenuExpanded: Boolean = false,
        val breedMenuSelectedOption: Breed? = null,
        val weightFieldValue: String = "",
        val heightFieldValue: String = "",
        val lengthFieldValue: String = "",
        val circuitFieldValue: String = "",
        val descriptionFieldValue: String = "",
        val screenStage: AddPetScreenStage = AddPetScreenStage.General,
        val hideKeyboard: Boolean = false,
        val isNameValid: Boolean = true,
        val isNameChanged: Boolean = false,
        val isNameInputChanged: Boolean = false,
        val nameErrorMessage: Int = R.string.util_blank,
        val isGenderValid: Boolean = true,
        val isGenderChanged: Boolean = false,
        val isBirthDateValid: Boolean = true,
        val isBirthDateChanged: Boolean = false,
        val birtDateErrorMessage: Int = R.string.util_blank,
        val isSpeciesValid: Boolean = true,
        val isSpeciesChanged: Boolean = false,
        val speciesErrorMessage: Int = R.string.util_blank,
        val isBreedValid: Boolean = false,
        val breedErrorMessage: Int = R.string.util_blank,
        val isWeightValid: Boolean = true,
        val isWeightChanged: Boolean = false,
        val weightErrorMessage: Int = R.string.util_blank,
        val isWeightUnitPickerExpanded: Boolean = false,
        val weightUnitList: List<com.example.petapp.model.Unit> = WeightUnit.values().toList(),
        val selectedWeightUnit: WeightUnit = WeightUnit.KILOGRAMS,
        val isHeightValid: Boolean = true,
        val heightErrorMessage: Int = R.string.util_blank,
        val dimensionUnitList: List<com.example.petapp.model.Unit> = DimensionUnit.values().toList(),
        val isHeightUnitPickerExpanded: Boolean = false,
        val selectedHeightUnit: DimensionUnit = DimensionUnit.METERS,
        val isLengthValid: Boolean = true,
        val lengthErrorMessage: Int = R.string.util_blank,
        val isLengthUnitPickerExpanded: Boolean = false,
        val selectedLengthUnit: DimensionUnit = DimensionUnit.METERS,
        val isCircuitValid: Boolean = true,
        val circuitErrorMessage: Int = R.string.util_blank,
        val isCircuitUnitPickerExpanded: Boolean = false,
        val selectedCircuitUnit: DimensionUnit = DimensionUnit.METERS,
        val maleIconChecked: Boolean = false,
        val femaleIconChecked: Boolean = false

    ) : AddPetUiState
    object Loading : AddPetUiState
    data class Error (val errorMessage: String) : AddPetUiState
}