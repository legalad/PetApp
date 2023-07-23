package com.example.petapp.ui.petdetails.fooddashboard

import com.example.android.datastore.UserPreferences
import com.example.petapp.data.PetMealEntity
import com.example.petapp.model.PetMealUiState

sealed interface PetFoodDashboardUiState {
    data class Success (
        val petName: String = "",
        val petMeals: List<PetMealUiState> = emptyList(),
        val selectedMeals: List<PetMealEntity> = emptyList(),
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC,
            ) : PetFoodDashboardUiState
    object Loading : PetFoodDashboardUiState
    data class Error (val errorMessage: String) : PetFoodDashboardUiState
}
