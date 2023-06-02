package com.example.petapp.ui.petdetails

import com.example.android.datastore.UserPreferences
import com.example.petapp.data.PetDetailsView
import com.example.petapp.data.PetMealEntity
import java.time.Duration
import java.time.Instant
import java.util.*

sealed interface PetDetailsUiState {
    data class Success(
        val pet: PetDetailsView = PetDetailsView(UUID.randomUUID(), "", Instant.now(), 0.0,0.0,0.0,0.0),
        val petMeals: List<PetMealEntity> = emptyList(),
        val lastWaterChanged: Duration? = null,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC
    ) : PetDetailsUiState
    object Loading: PetDetailsUiState
    data class Error (val errorMessage: String) : PetDetailsUiState
}