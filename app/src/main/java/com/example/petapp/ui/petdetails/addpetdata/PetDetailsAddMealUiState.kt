package com.example.petapp.ui.petdetails.addpetdata

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.MealType
import com.example.petapp.model.WeightUnit
import java.time.LocalTime

sealed interface PetDetailsAddMealUiState {
    data class Success @OptIn(ExperimentalMaterial3Api::class) constructor(
        val mealTypeMenuEnabled: Boolean = true,
        val mealTypeMenuOptions: List<MealType> = MealType.values().toList(),
        val mealTypeMenuExpanded: Boolean = false,
        val mealTypeMenuSelectedOption: MealType = MealType.NONE,
        val foodTypeRadioOptions: List<FoodTypeEnum> = FoodTypeEnum.values().toList(),
        val foodTypeRadioSelectedOption: FoodTypeEnum = FoodTypeEnum.WET,
        val isMealTypeValid: Boolean = true,
        val mealTypeErrorMessage: Int = R.string.util_blank,
        val foodMenuEnabled: Boolean = true,
        val foodMenuOptions: List<String> = emptyList(),
        val foodMenuExpanded: Boolean = false,
        val foodMenuSelectedOptionText: String = "",
        val timePickerState: TimePickerState = TimePickerState(
            initialHour = LocalTime.now().hour,
            initialMinute = LocalTime.now().minute,
            is24Hour = true
        ),
        val showTimePicker: Boolean = false,
        val showingPicker: Boolean = true,
        val weightFieldValue: String = "",
        @StringRes val weightFieldValuePlaceholder: Int = R.string.util_unit_weight_kg,
        val isWeightUnitPickerExpanded: Boolean = false,
        val weightUnitList: List<com.example.petapp.model.Unit> = WeightUnit.values().toList(),
        val selectedWeightUnit: WeightUnit = WeightUnit.KILOGRAMS,
        val isWeightChanged: Boolean = false,
        val isWeightValid: Boolean = true,
        val weightErrorMessage: Int = R.string.util_blank,
        val hideKeyboard: Boolean = false,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC
    ) : PetDetailsAddMealUiState
    object Loading: PetDetailsAddMealUiState
    data class Error (val errorMessage: String) : PetDetailsAddMealUiState
}

enum class FoodTypeEnum (@StringRes val nameId: Int) {
    WET (R.string.util_enums_food_type_wet),
    DRY (R.string.util_enums_food_type_dry),
    OTHER (R.string.util_enums_food_type_other)
}