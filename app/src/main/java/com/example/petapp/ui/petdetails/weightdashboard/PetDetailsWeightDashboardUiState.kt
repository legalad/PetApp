package com.example.petapp.ui.petdetails.weightdashboard

import com.example.petapp.data.PetWeightEntity
import com.example.android.datastore.UserPreferences

sealed interface PetDetailsWeightDashboardUiState {
    data class Success(
        val weightHistoryList: List<PetWeightEntity> = emptyList(),
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC
    ) : PetDetailsWeightDashboardUiState
    object Loading : PetDetailsWeightDashboardUiState
    data class Error (val errorMessage: String) : PetDetailsWeightDashboardUiState
}