package com.example.petapp.ui.petdetails.addpetdata

import androidx.annotation.StringRes
import androidx.compose.material3.*
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


sealed interface PetDetailsAddWeightUiState {
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
        val weightFieldValue: String = "",
        @StringRes val weightFieldValuePlaceholder: Int = R.string.util_unit_weight_kg,
        val isWeightChanged: Boolean = false,
        val isWeightValid: Boolean = true,
        val weightErrorMessage: Int = R.string.util_blank,
        val hideKeyboard: Boolean = false,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC,
        ) : PetDetailsAddWeightUiState
    object Loading: PetDetailsAddWeightUiState
    data class Error (val errorMessage: String) : PetDetailsAddWeightUiState
}