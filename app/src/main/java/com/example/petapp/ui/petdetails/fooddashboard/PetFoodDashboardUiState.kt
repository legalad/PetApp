package com.example.petapp.ui.petdetails.fooddashboard

import com.example.petapp.data.PetMealEntity

sealed interface PetFoodDashboardUiState {
    data class Success (
        val petName: String = "",
        val petMeals: List<PetMealEntity> = emptyList()
            ) : PetFoodDashboardUiState
    object Loading : PetFoodDashboardUiState
    data class Error (val errorMessage: String) : PetFoodDashboardUiState
}
