package com.example.petapp.ui.dashboard

import com.example.petapp.data.PetGeneralEntity
import com.example.android.datastore.UserPreferences.Unit

sealed interface DashboardUiState {
    data class Success(
        val pets: List<PetGeneralEntity> = emptyList(),
        val isLoading: Boolean = true,
        val unit: Unit = Unit.METRIC
    ): DashboardUiState
    object Loading : DashboardUiState
    data class Error(val errorMessage: String) : DashboardUiState
}
