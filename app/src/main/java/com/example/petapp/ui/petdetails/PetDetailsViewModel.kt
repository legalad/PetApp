package com.example.petapp.ui.petdetails

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.PetWaterEntity
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.di.CameraExecutor
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
import java.util.concurrent.ExecutorService
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalMaterial3Api::class)
@HiltViewModel
open class PetDetailsViewModel @Inject constructor(
    private val settingsDataRepository: UserSettingsDataRepository,
    private val petsDashboardRepository: PetsDashboardRepository,
    @CameraFile private val outputDirectory: File,
    private val application: Application,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), PetStatsFormatter,  HandleCameraEvents{

    private val petId: String = checkNotNull(savedStateHandle["petId"])

    var uiState: PetDetailsUiState by mutableStateOf(PetDetailsUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(PetDetailsUiState.Success(outputDirectory = outputDirectory))

    val successUiState: StateFlow<PetDetailsUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value)

    init {

        viewModelScope.launch {
            combine(
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
            }.collect()
        }
        uiState = PetDetailsUiState.Success(outputDirectory = outputDirectory)
    }


    override fun getPetAgeFormattedString(instant: Instant): String {
        return Formatters.getFormattedAgeString(instant= instant, context = application.applicationContext)
    }

    override fun getPetWeightFormattedString(weight: Double): String {
        return Formatters.getFormattedWeightString(weight = weight, unit = _successUiState.value.unit, context = application.applicationContext)
    }

    override fun getPetDimensionsFormattedString(value: Double): String {
        return Formatters.getFormattedDimensionString(value = value, unit = _successUiState.value.unit, context = application.applicationContext)
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

    fun hideCamera() {
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

    fun onSelectImage (uri: Uri?) {
        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION

        uri?.let {
            application.applicationContext.contentResolver.takePersistableUriPermission(it, takeFlags)
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
}