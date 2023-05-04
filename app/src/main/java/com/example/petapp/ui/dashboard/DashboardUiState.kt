package com.example.petapp.ui.dashboard

import com.example.android.datastore.UserPreferences.Unit
import com.example.petapp.model.PetDashboardUiState

sealed interface DashboardUiState {
    data class Success(
        val pets: List<PetDashboardUiState> = emptyList(),
        val isLoading: Boolean = true,
        val unit: Unit = Unit.METRIC
    ) : DashboardUiState

    object Loading : DashboardUiState
    data class Error(val errorMessage: String) : DashboardUiState
}
