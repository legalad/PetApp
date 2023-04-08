package com.example.petapp.model

import androidx.annotation.DrawableRes
import java.util.Date

data class Pet(
    @DrawableRes val petIconId: Int?,
    val name: String,
    val species: String,
    val birthDate: Date,
    val weight: Double
)
