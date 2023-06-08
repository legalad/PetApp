package com.example.petapp.model.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.data.*
import com.example.petapp.model.PetDashboardUiState
import com.example.petapp.model.PetDetailsUiState
import com.example.petapp.model.PetStatProgressIndicatorEntry
import com.example.petapp.ui.petdetails.weightdashboard.ChartDateEntry
import com.example.petapp.ui.petdetails.weightdashboard.ListDateEntry
import com.example.petapp.ui.settings.SettingsUiState
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.Duration
import java.time.Instant

fun UserPreferences.toSettingsUiState(): SettingsUiState.Success {
    return SettingsUiState.Success(
        language = language,
        unit = unit
    )
}

fun Map<PetDashboardView, List<PetMealEntity>>.toPetDashboardUiState(): List<PetDashboardUiState> {
    return map {
        PetDashboardUiState(
            petDashboard = PetDashboardView(
                petId = it.key.petId,
                name = it.key.name,
                birthDate = it.key.birthDate,
                waterLastChanged = it.key.waterLastChanged,
                weight = it.key.weight,
                imageUri = it.key.imageUri
            ),
            petMeals = it.value,
            waterStat = it.key.waterLastChanged?.toPetStatProgressIndicatorEntry(24)?: PetStatProgressIndicatorEntry(1f, Color.Red),
            mealStat = PetStatProgressIndicatorEntry(1.0f, Color.Green)
        )
    }
}

fun Instant.toPetStatProgressIndicatorEntry(hoursLimit: Int) : PetStatProgressIndicatorEntry{
    val duration = Duration.between(Instant.ofEpochMilli(toEpochMilli()), Instant.now()).toHours() / hoursLimit.toFloat()
    Log.d("CZAS", duration.toString())
    return if (duration > 1) {
        PetStatProgressIndicatorEntry(
            value = 0.0f,
            color = Color.Red
        )
    } else if (duration > 0.75f) {
        PetStatProgressIndicatorEntry(
            value = 1f - duration,
            color = Color.Red
        )
    } else if (duration > 0.5f) {
        PetStatProgressIndicatorEntry(
            value = 1f - duration,
            color = Color.Yellow
        )
    } else {
        PetStatProgressIndicatorEntry(
            value = 1f - duration,
            color = Color.Green
        )
    }
}
fun PetDetailsView.toPetDetailsUiState(): PetDetailsUiState {
    return PetDetailsUiState(
        petDetailsView = PetDetailsView(
            petId = petId,
            name = name,
            birthDate = birthDate,
            weight = weight,
            height = height,
            length = length,
            circuit = circuit,
            imageUri = imageUri
        )
    )
}


@JvmName("weightToDateEntryList")
fun List<PetWeightEntity>.toListDateEntryList(): List<ListDateEntry> {
    var tmpWeight = 0.0
    return map {
        val changeValue = it.value - tmpWeight
        val changeIconId: Int
        val changeIconColor: Color
        if (changeValue > 0.0) {
            changeIconId = R.drawable.round_arrow_drop_up_24
            changeIconColor = Color.Green
        } else if (changeValue == 0.0) {
            changeIconId = R.drawable.round_horizontal_rule_24
            changeIconColor = Color.Transparent
        } else {
            changeIconId = R.drawable.round_arrow_drop_down_24
            changeIconColor = Color.Red
        }
        tmpWeight = it.value
        ListDateEntry(
            id = it.id,
            localDate = it.measurementDate,
            changeValue = changeValue,
            changeIconId = changeIconId,
            changeIconColor = changeIconColor,
            value = it.value
        )
    }
}

@JvmName("heightToDateEntryList")
fun List<PetHeightEntity>.toListDateEntryList(): List<ListDateEntry> {
    var tmpHeight = 0.0
    return map {
        val changeValue = it.value - tmpHeight
        val changeIconId: Int
        val changeIconColor: Color
        if (changeValue > 0.0) {
            changeIconId = R.drawable.round_arrow_drop_up_24
            changeIconColor = Color.Green
        } else if (changeValue == 0.0) {
            changeIconId = R.drawable.round_horizontal_rule_24
            changeIconColor = Color.Transparent
        } else {
            changeIconId = R.drawable.round_arrow_drop_down_24
            changeIconColor = Color.Red
        }
        tmpHeight = it.value
        ListDateEntry(
            id = it.id,
            localDate = it.measurementDate,
            changeValue = changeValue,
            changeIconId = changeIconId,
            changeIconColor = changeIconColor,
            value = it.value
        )
    }
}

@JvmName("heightToChartEntryModelProducer")
fun List<PetHeightEntity>.toChartEntryModelProducer(unit: UserPreferences.Unit): ChartEntryModelProducer {
    return ChartEntryModelProducer(listOf(mapIndexed { index, petHeightEntity ->
        ChartDateEntry(
            localDate = petHeightEntity.measurementDate,
            y = Formatters.getDimensionValue(petHeightEntity.value, unit = unit).toFloat(),
            x = index.toFloat()
        )
    }))
}

@JvmName("lengthToChartEntryModelProducer")

fun List<PetLengthEntity>.toChartEntryModelProducer(unit: UserPreferences.Unit): ChartEntryModelProducer {
    return ChartEntryModelProducer(listOf(mapIndexed { index, petLengthEntity ->
        ChartDateEntry(
            localDate = petLengthEntity.measurementDate,
            y = Formatters.getDimensionValue(petLengthEntity.value, unit = unit).toFloat(),
            x = index.toFloat()
        )
    }))
}

@JvmName("circuitToChartEntryModelProducer")

fun List<PetCircuitEntity>.toChartEntryModelProducer(unit: UserPreferences.Unit): ChartEntryModelProducer {
    return ChartEntryModelProducer(listOf(mapIndexed { index, petCircuitEntity ->
        ChartDateEntry(
            localDate = petCircuitEntity.measurementDate,
            y = Formatters.getDimensionValue(petCircuitEntity.value, unit = unit).toFloat(),
            x = index.toFloat()
        )
    }))
}

@JvmName("lengthToDateEntryList")
fun List<PetLengthEntity>.toListDateEntryList(): List<ListDateEntry> {
    var tmpLength = 0.0
    return map {
        val changeValue = it.value - tmpLength
        val changeIconId: Int
        val changeIconColor: Color
        if (changeValue > 0.0) {
            changeIconId = R.drawable.round_arrow_drop_up_24
            changeIconColor = Color.Green
        } else if (changeValue == 0.0) {
            changeIconId = R.drawable.round_horizontal_rule_24
            changeIconColor = Color.Transparent
        } else {
            changeIconId = R.drawable.round_arrow_drop_down_24
            changeIconColor = Color.Red
        }
        tmpLength = it.value
        ListDateEntry(
            id = it.id,
            localDate = it.measurementDate,
            changeValue = changeValue,
            changeIconId = changeIconId,
            changeIconColor = changeIconColor,
            value = it.value
        )
    }
}

@JvmName("circuitToDateEntryList")
fun List<PetCircuitEntity>.toListDateEntryList(): List<ListDateEntry> {
    var tmpCircuit = 0.0
    return map {
        val changeValue = it.value - tmpCircuit
        val changeIconId: Int
        val changeIconColor: Color
        if (changeValue > 0.0) {
            changeIconId = R.drawable.round_arrow_drop_up_24
            changeIconColor = Color.Green
        } else if (changeValue == 0.0) {
            changeIconId = R.drawable.round_horizontal_rule_24
            changeIconColor = Color.Transparent
        } else {
            changeIconId = R.drawable.round_arrow_drop_down_24
            changeIconColor = Color.Red
        }
        tmpCircuit = it.value
        ListDateEntry(
            id = it.id,
            localDate = it.measurementDate,
            changeValue = changeValue,
            changeIconId = changeIconId,
            changeIconColor = changeIconColor,
            value = it.value
        )
    }
}