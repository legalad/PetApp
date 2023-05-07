package com.example.petapp.ui.petdetails.weightdashboard

import com.example.android.datastore.UserPreferences
import com.example.petapp.data.PetWeightEntity
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.Instant

sealed interface PetDetailsWeightDashboardUiState {
    data class Success(
        val weightHistoryList: List<PetWeightEntity> = emptyList(),
        val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC
    ) : PetDetailsWeightDashboardUiState
    object Loading : PetDetailsWeightDashboardUiState
    data class Error (val errorMessage: String) : PetDetailsWeightDashboardUiState
}

class DateEntry(
    val localDate: Instant,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float): ChartEntry {
        return DateEntry(localDate, x, y)
    }

}