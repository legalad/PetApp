package com.example.petapp.model.util

import android.content.Context
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import com.example.petapp.model.DimensionUnit
import com.example.petapp.model.WeightUnit
import com.example.petapp.ui.petdetails.weightdashboard.ChartDateEntry
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class Formatters {
    companion object {
        fun getFormattedAgeString(instant: Instant, context: Context): String {
            val age = Period.between(
                instant.atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now(ZoneId.systemDefault())
            )
            return if (age.years > 1) ("${age.years} " + context.getString(R.string.util_time_years))
            else if (age.years == 1) ("${age.years} " + context.getString(R.string.util_time_year))
            else if (age.months > 1) ("${age.months} " + context.getString(R.string.util_time_months))
            else if (age.months == 1) ("${age.months} " + context.getString(R.string.util_time_month))
            else if (age.days == 1) ("${age.days} " + context.getString(R.string.util_time_day))
            else ("${age.days} " + context.getString(R.string.util_time_days))

        }

        fun getFormattedWeightString(
            weight: Double?,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return weight?.let {
                if (unit == UserPreferences.Unit.METRIC) ("${"%.2f".format(it)} " + context.getString(
                    R.string.util_unit_weight_kg
                ))
                else ("${"%.2f".format(it * 2.20462262)} " + context.getString(R.string.util_unit_weight_pounds))
            } ?: "-.-"
        }

        fun getFormattedWeightUnitString(
            weight: Double?,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return weight?.let {
                if (unit == UserPreferences.Unit.METRIC){
                    if (weight >= 1) ("${"%.2f".format(it)} " + context.getString(
                        R.string.util_unit_weight_kg
                    ))
                    else ("${"%.0f".format(weight * 1000)} " + context.getString(R.string.util_unit_weight_g))
                }
                else ("${"%.2f".format(it)} " + context.getString(R.string.util_unit_weight_pounds))
            } ?: "-.-"
        }

        fun getFormattedDimensionString(
            value: Double?,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return value?.let {
                if (unit == UserPreferences.Unit.METRIC) {
                    if (value >= 1) ("${"%.2f".format(value)} " + context.getString(R.string.util_unit_dimension_meters))
                    else ("${"%.0f".format(value * 100)} " + context.getString(R.string.util_unit_dimension_centimeter))
                } else {
                    if (value * 3.2808 >= 1) ("${"%.2f".format(value * 3.2808)} " + context.getString(
                        R.string.util_unit_dimension_foot
                    ))
                    else ("${"%.2f".format(value * 39.3700787)} " + context.getString(R.string.util_unit_dimension_inch))
                }
            } ?: "-.-"
        }

        fun getFormattedDimensionUnitString(
            value: Double?,
            unit: UserPreferences.Unit,
            context: Context
        ): String {
            return value?.let {
                if (unit == UserPreferences.Unit.METRIC) {
                    if (value >= 1) ("${"%.2f".format(value)} " + context.getString(R.string.util_unit_dimension_meters))
                    else ("${"%.0f".format(value * 100)} " + context.getString(R.string.util_unit_dimension_centimeter))
                } else {
                    if (value >= 1) ("${"%.2f".format(value)} " + context.getString(
                        R.string.util_unit_dimension_foot
                    ))
                    else ("${"%.2f".format(value * 39.3700787)} " + context.getString(R.string.util_unit_dimension_inch))
                }
            } ?: "-.-"
        }

        fun getWeightString(value: Double, unit: UserPreferences.Unit): String {
            return if (unit == UserPreferences.Unit.METRIC) ("%.2f".format(Locale.US, value))
            else ("%.2f".format(Locale.US,value * 2.20462262))
        }

        fun getDimensionString(value: Double, unit: UserPreferences.Unit): String {
            return if (unit == UserPreferences.Unit.METRIC) ("%.2f".format(Locale.US, value))
            else ("%.2f".format(Locale.US,value * 3.2808))
        }

        fun getWeightValue(value: Double, unit: UserPreferences.Unit): Double {
            return if (unit == UserPreferences.Unit.METRIC) value
            else value * 2.20462262
        }

        fun getMetricWeightValue(value: Double, unit: UserPreferences.Unit): Double {
            return if (unit == UserPreferences.Unit.METRIC) value
            else value * 0.45359237
        }

        fun getMetricWeightValue(valueStr: String, unit: WeightUnit): Double? {
            val value = try {
                valueStr.toDouble()
            } catch (e: Exception) {
                return null
            }
            return when(unit) {
                WeightUnit.MILLIGRAMS -> value / 1000000
                WeightUnit.GRAMS -> value / 1000
                WeightUnit.KILOGRAMS -> value
                else -> getImperialLbsValue(value = value, unit = unit)?.times(0.45359237)
            }
        }
        private fun getImperialLbsValue(value: Double, unit: WeightUnit): Double? {
            return when (unit) {
                WeightUnit.OUNCE -> value / 16
                WeightUnit.POUNDS -> value
                WeightUnit.STONE -> value * 14
                else -> null
            }
        }

        fun getMetricDimensionValue(valueStr: String, unit: DimensionUnit): Double? {
            val value = try {
                valueStr.toDouble()
            } catch (e: Exception) {
                return null
            }
            return when (unit) {
                DimensionUnit.CENTIMETERS -> value / 100
                DimensionUnit.METERS -> value
                else -> getImperialFtValue(value = value, unit = unit)?.times(0.3048)
            }
        }

        private fun getImperialFtValue(value: Double, unit: DimensionUnit): Double? {
            return when (unit) {
                DimensionUnit.FOOTS -> value
                DimensionUnit.INCHES -> value / 12
                else -> return null
            }
        }

        fun getMetricDimensionValue(value: Double, unit: UserPreferences.Unit): Double {
            return if (unit == UserPreferences.Unit.METRIC) value
            else value * 0.3048
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

        fun waterLastChangedFormatter(duration: Duration?, context: Context) : String{
            return ("Last changed: " + (duration?.toHours()?.toString()?.plus(" hours ago") ?: "never"))
        }

    }
}