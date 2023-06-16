package com.example.petapp.ui.petdetails

import android.net.Uri
import androidx.compose.material3.*
import com.example.android.datastore.UserPreferences
import com.example.petapp.data.PetDetailsView
import com.example.petapp.data.PetMealEntity
import com.example.petapp.model.DogBreed
import com.example.petapp.model.PetGender
import com.example.petapp.model.Species
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.*

sealed interface PetDetailsUiState {
    @ExperimentalMaterial3Api data class Success constructor(
        val pet: PetDetailsView? = PetDetailsView(UUID.randomUUID(), "", Instant.now(), PetGender.MALE, Species.DOG, DogBreed.BULlDOG.ordinal, null, 0.0,0.0,0.0,0.0),
        val petMeals: List<PetMealEntity> = emptyList(),
        val lastWaterChanged: Duration? = null,
        val shouldShowCamera: Boolean = false,
        val outputDirectory: File,
        val selectedImageUri: Uri? = null,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC,
        val topBarMenuExpanded: Boolean = false
    ) : PetDetailsUiState
    object Loading: PetDetailsUiState
    data class Error (val errorMessage: String) : PetDetailsUiState
}