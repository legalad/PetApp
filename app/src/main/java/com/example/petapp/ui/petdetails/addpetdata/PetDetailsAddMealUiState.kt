package com.example.petapp.ui.petdetails.addpetdata

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.example.petapp.R
import com.example.petapp.data.MealType
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
        val mealTypeErrorMessage: Int = R.string.blank,
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
    ) : PetDetailsAddMealUiState
    object Loading: PetDetailsAddMealUiState
    data class Error (val errorMessage: String) : PetDetailsAddMealUiState
}

enum class FoodTypeEnum (@StringRes val nameId: Int) {
    WET (R.string.food_type_wet),
    DRY (R.string.food_type_dry),
    OTHER (R.string.food_type_other)
}