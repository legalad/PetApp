package com.example.petapp.model.util

import android.content.Context
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.ui.petdetails.weightdashboard.ChartDateEntry
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class Formatters {
    companion object {
        fun getFormattedAgeString(instant: Instant, context: Context): String {
            val age = Period.between(
                instant.atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now(ZoneId.systemDefault())
            )
            return if (age.years > 1) ("${age.years} " + context.getString(R.string.age_years_old))
            else if (age.years == 1) ("${age.years} " + context.getString(R.string.age_year_old))
            else if (age.months > 1) ("${age.months} " + context.getString(R.string.age_months_old))
            else if (age.months == 1) ("${age.months} " + context.getString(R.string.age_month_old))
            else if (age.days == 1) ("${age.days} " + context.getString(R.string.age_day_old))
            else ("${age.days} " + context.getString(R.string.age_days_old))

        }

        fun getFormattedWeightString(
            weight: Double,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return if (unit == UserPreferences.Unit.METRIC) ("${"%.2f".format(weight)} " + context.getString(
                R.string.unit_kg
            ))
            else ("${"%.2f".format(weight * 0.45359237)} " + context.getString(R.string.unit_lbs))
        }

        fun getFormattedDimensionString(
            value: Double,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return if (unit == UserPreferences.Unit.METRIC) {
                if (value >= 1) ("${"%.2f".format(value)} " + context.getString(R.string.unit_meter))
                else ("${"%.0f".format(value * 100)} " + context.getString(R.string.unit_centimeter))
            } else {
                if (value * 3.2808 >= 1) ("${"%.2f".format(value * 3.2808)} " + context.getString(
                    R.string.unit_foot
                ))
                else ("${"%.2f".format(value * 39.3700787)} " + context.getString(R.string.inch))
            }
        }

        fun getWeightString(value: Double, unit: UserPreferences.Unit): String {
            return if (unit == UserPreferences.Unit.METRIC) ("%.2f".format(value))
            else ("%.2f".format(value * 0.45359237))
        }

        fun getWeightValue(value: Double, unit: UserPreferences.Unit): Double {
            return if (unit == UserPreferences.Unit.METRIC) value
            else value * 0.45359237
        }

        fun getDimensionValue(value: Double, unit: UserPreferences.Unit): Double {
            return if (unit == UserPreferences.Unit.METRIC) value
            else value * 3.2808
        }

        fun getAxisValueFormatterWithDate() :  AxisValueFormatter<AxisPosition.Horizontal.Bottom>{
            return AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, chartValues ->
                try {
                    (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? ChartDateEntry)
                        ?.localDate
                        ?.run {

                            DateTimeFormatter.ofPattern("dd.MM").withZone(ZoneId.systemDefault()).withLocale(
                                Locale.getDefault()).format(Instant.ofEpochMilli(toEpochMilli())).toString()
                        }
                        .orEmpty()
                } catch (e: NoSuchElementException) {
                    ""
                }
            }
        }
    }
}