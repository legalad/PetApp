package com.example.petapp.model.util

import android.content.Context
import com.example.android.datastore.UserPreferences
import com.example.petapp.R
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

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
                else ("${"%.2f".format(value * 100)} " + context.getString(R.string.unit_centimeter))
            } else {
                if (value * 3.2808 >= 1) ("${"%.2f".format(value * 3.2808)} " + context.getString(
                    R.string.unit_foot
                ))
                else ("${"%.2f".format(value * 39.3700787)} " + context.getString(R.string.inch))
            }
        }
    }
}