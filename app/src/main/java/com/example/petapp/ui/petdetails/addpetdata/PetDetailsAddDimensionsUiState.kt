package com.example.petapp.ui.petdetails.addpetdata

import androidx.compose.material3.*
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.model.DimensionUnit
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

sealed interface PetDetailsAddDimensionsUiState {
    data class Success @OptIn(ExperimentalMaterial3Api::class) constructor(
        val datePickerTextFieldValue: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(
            Locale.getDefault()).withZone(ZoneId.systemDefault()).format(Instant.now()),
        val datePickerOpenDialog: Boolean = false,
        val datePickerState: DatePickerState = DatePickerState(
            initialSelectedDateMillis = Instant.now().toEpochMilli(),
            initialDisplayedMonthMillis = Instant.now().toEpochMilli(),
            yearRange = DatePickerDefaults.YearRange,
            initialDisplayMode = DisplayMode.Picker
        ),
        val datePickerConfirmEnabled: Boolean = true,
        val timePickerState: TimePickerState = TimePickerState(
            initialHour = LocalTime.now().hour,
            initialMinute = LocalTime.now().minute,
            is24Hour = true
        ),
        val showTimePicker: Boolean = false,
        val showingPicker: Boolean = true,
        val heightFieldValue: String = "",
        val lengthFieldValue: String = "",
        val circuitFieldValue: String = "",
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
        val defaultFiledValuePlaceholder: Int = R.string.util_unit_dimension_meters,
        val updatedFieldLeadingIcon: Int = R.drawable.baseline_height_24,
        val updatedFieldLabel: Int = R.string.components_forms_text_field_label_pet_height,
        val updatedDimensionFieldValue: String = "",
        val updatedDimensionErrorMessage: Int = R.string.util_blank,
        val isUpdatedUnitPickerExpanded: Boolean = false,
        val selectedUpdatedUnit: DimensionUnit = DimensionUnit.METERS,
        val isUpdatedDimensionValid: Boolean = true,
        val isFormValid: Boolean = true,
        val hideKeyboard: Boolean = false,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC
    ) : PetDetailsAddDimensionsUiState
    object Loading: PetDetailsAddDimensionsUiState
    data class Error (val errorMessage: String) : PetDetailsAddDimensionsUiState
}