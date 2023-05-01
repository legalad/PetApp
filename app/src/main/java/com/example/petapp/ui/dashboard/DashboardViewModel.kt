package com.example.petapp.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository
) : ViewModel() {
    var uiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(DashboardUiState.Success())
    val successUiState: StateFlow<DashboardUiState.Success> = petsDashboardRepository.getPets().combine(_successUiState){
        repository, uiState ->
        DashboardUiState.Success(
            pets = repository
        )
    }
        .also { uiState = DashboardUiState.Success() }
        .catch { uiState = DashboardUiState.Error(it.message?: "Error") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = DashboardUiState.Success()
        )


}