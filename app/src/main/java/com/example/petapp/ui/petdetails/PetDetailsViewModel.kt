package com.example.petapp.ui.petdetails

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material3.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.*
import com.example.petapp.di.CameraFile
import com.example.petapp.model.HandleCameraEvents
import com.example.petapp.model.PetStatsFormatter
import com.example.petapp.model.util.Contstans
import com.example.petapp.model.util.Formatters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
open class PetDetailsViewModel @Inject constructor(
    settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    @CameraFile private val outputDirectory: File,
    private val application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel(), PetStatsFormatter, HandleCameraEvents {

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    private val _successUiState =
        MutableStateFlow(PetDetailsUiState.Success(outputDirectory = outputDirectory))

    private val _asyncData = combine(
        petsDashboardRepository.getPetDetails(petId = petId),
        petsDashboardRepository.getPetMeals(petId = petId),
        petsDashboardRepository.getPetLastWaterChanged(petId = petId),
        settingsDataRepository.getUnit()
    ) { details, meals, water, unit ->
        _successUiState.update { success ->
            success.copy(
                pet = details,
                petMeals = meals,
                lastWaterChanged = water?.let {
                    Duration.between(
                        it.measurementDate,
                        Instant.now()
                    )
                },
                unit = unit
            )
        }
    }
        .map { Async.Success(_successUiState.value) }
        .catch<Async<PetDetailsUiState.Success>> { emit(Async.Error("Error")) }

    val uiState: StateFlow<PetDetailsUiState> =
        combine(_asyncData, _successUiState) { async, success ->
            when (async) {
                Async.Loading -> PetDetailsUiState.Loading
                is Async.Success -> success
                is Async.Error -> PetDetailsUiState.Error("Error")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = PetDetailsUiState.Loading
        )

    override fun getPetAgeFormattedString(instant: Instant): String {
        return Formatters.getFormattedAgeString(
            instant = instant,
            context = application.applicationContext
        )
    }

    override fun getPetWeightFormattedString(weight: Double?): String {
        return Formatters.getFormattedWeightString(
            weight = weight,
            unit = _successUiState.value.unit,
            context = application.applicationContext
        )
    }

    override fun getPetDimensionsFormattedString(value: Double?): String {
        return Formatters.getFormattedDimensionString(
            value = value,
            unit = _successUiState.value.unit,
            context = application.applicationContext
        )
    }

    fun onWaterRefillIconClicked() {
        viewModelScope.launch {
            petsDashboardRepository.addPetWaterChangeData(
                PetWaterEntity(
                    id = UUID.randomUUID(),
                    pet_id = UUID.fromString(petId),
                    measurementDate = Instant.now()
                )
            )
        }
    }

    override fun handleImageCapture(uri: Uri) {
        Log.i("info", "Image capture: $uri")
        hideCamera()
        updatePetImageUri(uri)
    }

    private fun hideCamera() {
        _successUiState.update {
            it.copy(
                shouldShowCamera = false
            )
        }
    }

    override fun handleShowCamera() {
        _successUiState.update {
            it.copy(
                shouldShowCamera = true
            )
        }
    }

    fun onSelectImage(uri: Uri?) {
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION

        uri?.let {
            application.applicationContext.contentResolver.takePersistableUriPermission(
                it,
                takeFlags
            )
            updatePetImageUri(it)
        }

    }

    private fun updatePetImageUri(uri: Uri?) {
        Log.i("info", uri.toString() + " viewmodel")
        viewModelScope.launch(Dispatchers.IO) {
            uri?.let {
                petsDashboardRepository.getPet(petId = petId)?.copy(imageUri = uri)?.let {
                    petsDashboardRepository.updatePetGeneral(
                        it
                    )
                }
            }
        }
    }

    fun onDropdownMenuItemClicked() {
        _successUiState.update {
            it.copy(
                topBarMenuExpanded = !it.topBarMenuExpanded
            )
        }
    }

    fun dropdownMenuOnDismissRequest() {
        _successUiState.update {
            it.copy(
                topBarMenuExpanded = false
            )
        }
    }

    fun deletePet() {
        viewModelScope.launch(Dispatchers.IO) {
            petsDashboardRepository.getPet(_successUiState.value.pet?.petId.toString())?.let {
                petsDashboardRepository.deletePet(it)
            }
        }
    }

}