package com.example.petapp.ui.dashboard

import com.example.petapp.data.PetGeneralEntity

sealed interface DashboardUiState {
    data class Success(
        val pets: List<PetGeneralEntity> = emptyList()
    ): DashboardUiState
    object Loading : DashboardUiState
    data class Error(val errorMessage: String) : DashboardUiState
}
