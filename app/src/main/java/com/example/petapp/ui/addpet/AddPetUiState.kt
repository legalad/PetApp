package com.example.petapp.ui.addpet

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.petapp.R
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
        val speciesMenuOptions: List<String> = listOf("Dog", "Cat", "Fish", "Parrot"),
        val speciesMenuExpanded: Boolean = false,
        val speciesMenuSelectedOptionText: String = "",
        val breedMenuEnabled: Boolean = true,
        val breedMenuOptions: List<String> = emptyList(),
        val breedMenuExpanded: Boolean = false,
        val breedMenuSelectedOptionText: String = "",
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
        val nameErrorMessage: Int = R.string.blank,
        val isBirthDateValid: Boolean = true,
        val isBirthDateChanged: Boolean = false,
        val birtDateErrorMessage: Int = R.string.blank,
        val isSpeciesValid: Boolean = false,
        val speciesErrorMessage: Int = R.string.blank,
        val isBreedValid: Boolean = false,
        val breedErrorMessage: Int = R.string.blank,
        val isWeightValid: Boolean = true,
        val isWeightChanged: Boolean = false,
        val weightErrorMessage: Int = R.string.blank,
        val isHeightValid: Boolean = true,
        val heightErrorMessage: Int = R.string.blank,
        val isLengthValid: Boolean = true,
        val lengthErrorMessage: Int = R.string.blank,
        val isCircuitValid: Boolean = true,
        val circuitErrorMessage: Int = R.string.blank

    ) : AddPetUiState
    object Loading : AddPetUiState
    data class Error (val errorMessage: String) : AddPetUiState
}