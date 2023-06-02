package com.example.petapp.model

import androidx.compose.ui.graphics.Color
import com.example.petapp.data.PetDashboardView
import com.example.petapp.data.PetMealEntity
import com.example.petapp.ui.components.PetStatsEnum

data class PetDashboardUiState(
    val petDashboard: PetDashboardView,
    val petMeals: List<PetMealEntity>,
    val petStat: PetStatsEnum = PetStatsEnum.NONE,
    val waterStat: PetStatProgressIndicatorEntry,
    val mealStat: PetStatProgressIndicatorEntry
)

data class PetStatProgressIndicatorEntry(
    val value: Float,
    val color: Color
)
