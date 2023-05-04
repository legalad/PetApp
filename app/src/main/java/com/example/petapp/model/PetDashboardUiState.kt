package com.example.petapp.model

import com.example.petapp.data.PetDashboardView
import com.example.petapp.ui.components.PetStatsEnum

data class PetDashboardUiState(
    val petDashboard: PetDashboardView,
    val petStat: PetStatsEnum = PetStatsEnum.NONE
)
