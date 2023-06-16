package com.example.petapp.ui.petdetails.dimensionsdashboard

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.ui.petdetails.weightdashboard.ChartDateEntry
import com.example.petapp.ui.petdetails.weightdashboard.DataDisplayedType
import com.example.petapp.ui.petdetails.weightdashboard.ListDateEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.Instant

sealed interface PetDetailsDimensionsDashboardUiState {
    data class Success(
        val petName: String = "",
        val petIdString: String = "",
        val heightHistoryListDateEntry: List<ListDateEntry> = emptyList(),
        val lengthHistoryListDateEntry: List<ListDateEntry> = emptyList(),
        val circuitHistoryListDateEntry: List<ListDateEntry> = emptyList(),
        val heightChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val lengthChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val circuitChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer(),
        val heightSelectedDateEntry: ChartDateEntry = ChartDateEntry(Instant.now(), 0f, 0f),
        val lengthSelectedDateEntry: ChartDateEntry = ChartDateEntry(Instant.now(), 0f, 0f),
        val circuitSelectedDateEntry: ChartDateEntry = ChartDateEntry(Instant.now(), 0f, 0f),
        val heightPersistentMarkerX: Float = 10f,
        val lengthPersistentMarkerX: Float = 10f,
        val circuitPersistentMarkerX: Float = 10f,
        val unit: UserPreferences.Unit = UserPreferences.Unit.METRIC,
        val dataDisplayedType: DataDisplayedType = DataDisplayedType.LINE_CHART,
        val displayedDimension: DisplayedDimension = DisplayedDimension.HEIGHT,
        val topAppBarMenuExpanded: Boolean = false
        ) : PetDetailsDimensionsDashboardUiState
    object Loading : PetDetailsDimensionsDashboardUiState
    data class Error (val errorMessage: String) : PetDetailsDimensionsDashboardUiState
}

enum class DisplayedDimension (@StringRes val dimensionName: Int, @DrawableRes val dimensionIconId: Int) {
    HEIGHT (dimensionName = R.string.components_forms_text_field_label_pet_height, dimensionIconId = R.drawable.baseline_height_24),
    LENGTH (dimensionName = R.string.components_forms_text_field_label_pet_length, dimensionIconId = R.drawable.width_24),
    CIRCUIT(dimensionName = R.string.components_forms_text_field_label_pet_circuit, dimensionIconId = R.drawable.restart_alt_24)
}