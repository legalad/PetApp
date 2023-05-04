package com.example.petapp.model.util

import com.example.android.datastore.UserPreferences
import com.example.petapp.data.PetDashboardView
import com.example.petapp.data.PetDetailsView
import com.example.petapp.model.PetDashboardUiState
import com.example.petapp.model.PetDetailsUiState
import com.example.petapp.ui.settings.SettingsUiState

fun UserPreferences.toSettingsUiState(): SettingsUiState.Success {
    return SettingsUiState.Success(
        language = language,
        unit = unit
    )
}

fun PetDashboardView.toPetDashboardUiState(): PetDashboardUiState {
    return PetDashboardUiState(
        petDashboard = PetDashboardView(
            petId = petId,
            name = name,
            birthDate = birthDate,
            weight = weight
        )
    )
}

fun List<PetDashboardView>.toPetDashboardUiStateList(): List<PetDashboardUiState> {
    return map { it.toPetDashboardUiState() }
}

fun PetDetailsView.toPetDetailsUiState(): PetDetailsUiState {
    return PetDetailsUiState(
        petDetailsView = PetDetailsView(
            petId = petId,
            name = name,
            birthDate = birthDate,
            weight = weight,
            height = height,
            length = length,
            circuit = circuit
        )
    )
}