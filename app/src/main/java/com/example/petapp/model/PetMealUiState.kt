package com.example.petapp.model

import com.example.petapp.data.PetMealEntity

data class PetMealUiState(
    val mealEntity: PetMealEntity,
    val isClicked: Boolean,
    val isReminderIconClicked: Boolean
)
