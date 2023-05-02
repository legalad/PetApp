package com.example.petapp.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.PetsDashboardRepository
import com.example.petapp.data.UserSettingsDataRepository
import com.example.petapp.model.util.Contstans
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val petsDashboardRepository: PetsDashboardRepository,
    private val settingsDataRepository: UserSettingsDataRepository
) : ViewModel() {
    var uiState: DashboardUiState by mutableStateOf(DashboardUiState.Loading)
        private set

    private val _successUiState = MutableStateFlow(DashboardUiState.Success())
    val successUiState: StateFlow<DashboardUiState.Success> = _successUiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = _successUiState.value)

    /*petsDashboardRepository.getPets().combine(_successUiState){
        repository, state ->
        DashboardUiState.Success(
            pets = repository
        )
    }
        .catch { uiState = DashboardUiState.Error(it.message?: "Error") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(Contstans.TIMEOUT_MILLIS),
            initialValue = DashboardUiState.Success()
        )*/

    init {
        viewModelScope.launch {
            petsDashboardRepository.getPets().collect { pets ->
                _successUiState.update { it.copy(pets = pets) }
            }
        }
        viewModelScope.launch {
            settingsDataRepository.getUnit().collect { unit ->
                _successUiState.update {it.copy(unit = unit)
                }
            }
        }
        uiState = DashboardUiState.Success()


    }

    fun getPetAgeFormattedString(instant: Instant): Pair<String,  Int> {
        val age = Period.between(
            instant.atZone(ZoneId.systemDefault()).toLocalDate(),
            LocalDate.now(ZoneId.systemDefault()))

        return if (age.years > 1) Pair("${age.years} ", R.string.age_years_old)
        else if (age.years == 1) Pair("${age.years} ", R.string.age_year_old)
        else if (age.months > 1) Pair("${age.months} ", R.string.age_months_old)
        else if (age.months == 1) Pair("${age.months} ", R.string.age_month_old)
        else if (age.days == 1) Pair("${age.days} ", R.string.age_day_old)
        else Pair("${age.days} ", R.string.age_days_old)

    }

    fun getPetWeightFormattedString(weight: Double): Pair<String, Int> {
        return if (_successUiState.value.unit == UserPreferences.Unit.METRIC) Pair("$weight ", R.string.unit_kg)
        else Pair("${"%.2f".format(weight * 0.45359237)} ", R.string.unit_lbs)
    }
}