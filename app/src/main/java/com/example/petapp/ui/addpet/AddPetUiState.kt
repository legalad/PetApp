package com.example.petapp.ui.addpet

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import java.text.DateFormat
import java.util.Calendar

sealed interface AddPetScreenStage {
    object General: AddPetScreenStage
    object Dimensions: AddPetScreenStage
    object Final: AddPetScreenStage
}

sealed interface AddPetUiState {
    data class Success @OptIn(ExperimentalMaterial3Api::class) constructor(
        val nameFieldValue: String = "",
        val datePickerTextFieldValue: String = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().time),
        val datePickerOpenDialog: Boolean = false,
        val datePickerState: DatePickerState = DatePickerState(
            initialSelectedDateMillis = Calendar.getInstance().timeInMillis,
            initialDisplayedMonthMillis = Calendar.getInstance().timeInMillis,
            yearRange = DatePickerDefaults.YearRange,
            initialDisplayMode = DisplayMode.Picker
        ),
        val datePickerConfirmEnabled: Boolean = true,
        val speciesMenuOptions: List<String> = listOf("Dog", "Cat", "Fish", "Parrot"),
        val speciesMenuExpanded: Boolean = false,
        val speciesMenuSelectedOptionText: String = "",
        val breedMenuEnabled: Boolean = false,
        val breedMenuOptions: List<String> = emptyList(),
        val breedMenuExpanded: Boolean = false,
        val breedMenuSelectedOptionText: String = "",
        val weightFieldValue: String = "",
        val heightFieldValue: String = "",
        val lengthFieldValue: String = "",
        val circuitFieldValue: String = "",
        val descriptionFieldValue: String = "",
        val screenStage: AddPetScreenStage = AddPetScreenStage.General
    ) : AddPetUiState
    object Loading : AddPetUiState
    data class Error (val errorMessage: String) : AddPetUiState
}