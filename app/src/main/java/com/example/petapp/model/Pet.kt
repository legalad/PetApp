package com.example.petapp.model

import androidx.annotation.DrawableRes
import java.time.Instant

data class Pet(
    @DrawableRes val petIconId: Int?,
    val name: String,
    val species: String,
    val breed: String,
    val birthTimestamp: Instant,
    val weight: Double,
    val petMeals: List<PetMeal>,
    val petThirst: List<PetThirst>,
    val petActivities: List<PetActivity>
)
