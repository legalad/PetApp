package com.example.petapp.model

import java.time.Instant

interface PetStatsFormatter {
    fun getPetAgeFormattedString(instant: Instant): String
    fun getPetWeightFormattedString(weight: Double?): String
    fun getPetDimensionsFormattedString(value: Double?): String
}