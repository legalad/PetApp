package com.example.petapp.ui.petdetails.weightdashboard

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.Instant
import java.util.UUID

sealed interface PetDetailsWeightDashboardUiState {
    data class Success(
        val petName: String = "",
        val petIdString: String = "",
        val weightHistoryList: List<ListDateEntry> = emptyList(),
        val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val selectedDateEntry: ChartDateEntry = ChartDateEntry(Instant.now(), 0f, 0f),
        val persistentMarkerX: Float = 10f,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC,
        val dataDisplayedType: DataDisplayedType = DataDisplayedType.LINE_CHART,
        val topAppBarMenuExpanded: Boolean = false,
    ) : PetDetailsWeightDashboardUiState
    object Loading : PetDetailsWeightDashboardUiState
    data class Error (val errorMessage: String) : PetDetailsWeightDashboardUiState
}

class ChartDateEntry(
    val localDate: Instant,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float): ChartEntry {
        return ChartDateEntry(localDate, x, y)
    }
}

class ListDateEntry(
    val id: UUID,
    val localDate: Instant,
    val changeValue: Double,
    @DrawableRes val changeIconId: Int,
    val changeIconColor: Color,
    val value: Double
)

enum class DataDisplayedType (@DrawableRes val chartIconId: Int) {
    LINE_CHART (R.drawable.round_list_alt_24),
    LIST (R.drawable.round_insert_chart_outlined_24)
}